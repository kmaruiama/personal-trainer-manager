import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { IonContent, IonButton, IonImg } from "@ionic/angular/standalone";
import { SharedScheduleNodesComponent } from "../../../../../schedule-nodes/shared-schedule-nodes.component";
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AlertController } from '@ionic/angular';

@Component({
  selector: 'app-customer-schedule',
  templateUrl: './customer-schedule.component.html',
  styleUrls: ['./customer-schedule.component.scss'],
  imports: [IonButton, IonContent, SharedScheduleNodesComponent, CommonModule],
  standalone: true
})

//Sim isso é uma cópia do schedule normal apenas adicionando a função de botar novos agendamentos
//Pensei em transformar toda a página em um componente padrão e só importar aqui, mas ia ficar muito
//confuso e como só são dois usos em toda UI deixa WET mesmo

export class CustomerScheduleComponent  implements OnInit {
  authToken: string = localStorage.getItem('authToken') || '';

  protected customerId: number = 0;
  protected schedules: Schedule[] = [];
  constructor(private router: Router, private http: HttpClient, private alertController: AlertController) { }

  ngOnInit() {
    const navigation = this.router.getCurrentNavigation();
    this.customerId = navigation?.extras.state?.['id'] || null;

    if (this.customerId !== null) {
      this.fetchCustomerSchedule(this.customerId, this.authToken)?.subscribe(
        (data)=> this.convertServerResponse(data),
        (error) => {
          console.log("deu ruim");
        }
      )
    }
  }

  fetchCustomerSchedule(customerId: number, authToken: string){
    const url = `http://localhost:8080/api/schedule/customer`;
    const headers = new HttpHeaders().set('Authorization', `Bearer ${authToken}`);
    if (authToken !== ""){
      return this.http.get<ScheduleGetDto[]>(url, { headers, params: { id: customerId.toString() } });
    }
    else return null;
  }

  convertServerResponse(scheduleGetDto : ScheduleGetDto[]){
    this.schedules = scheduleGetDto.map((schedule) =>({
      customerId: schedule.customerId,
      workoutName: schedule.workoutName,
      scheduleId: schedule.scheduleId,
      customerName: schedule.customerName,
      scheduleHourStart: this.trimSeconds(schedule.hourStart),
      scheduleHourEnd: this.trimSeconds(schedule.hourEnd),
      dayOfTheWeek: schedule.dayOfTheWeek,
      deleteFlag: false
    }))
    this.schedules.sort((a, b) => a.dayOfTheWeek - b.dayOfTheWeek);
  }

  returnDayOfTheWeek(index: number){
    const semana : string [] = ["Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado", "Domingo"]
    return semana[index];
  }

  trimSeconds(hours: string){
    return hours.substring(0, 5);
  }
  addNewSchedule(customerId: number){
    this.router.navigate(['/customer/schedule/add'], {
      state : { customerId : customerId }
    });
  }

  deleteSchedule(scheduleId: number){
    const schedule : Schedule | undefined= this.schedules.find(s => s.scheduleId === scheduleId);
    if (schedule){
      schedule.deleteFlag = true;

    }
  }
  filteredSchedules(): Schedule[]{
    return this.schedules.filter(schedule=>!schedule.deleteFlag);
  }

  persistChanges(){
    for(let i = 0; i<this.schedules.length; i++){
      if(this.schedules[i].deleteFlag === true){
        this.deleteScheduleNode(this.schedules[i].scheduleId);
      }
    }
  }

  deleteScheduleNode(id: number){
    const headers = { Authorization: `Bearer ${this.authToken}` };
    this.http
      .delete(`http://localhost:8080/api/schedule/delete?id=${id}`, {
        headers,
      }).subscribe(
        (response) => {
          console.log(`sucesso ao deletar agendamento com id ${id}`)
        },
        (error) => {
          console.error(`erro ao deletar agendamento com id ${id}`);
        }
      );

  }
}

type Schedule  = {
  customerName: string,
  scheduleHourStart: string,
  scheduleHourEnd: string,
  workoutName: string
  scheduleId: number,
  dayOfTheWeek: number,
  customerId: number,
  deleteFlag: boolean
  //boa sacada deixar o deleteflag só no frontend, depois é só iterar através do array de
  //schedules e ir deletando. Fazer isso no programa de treino tb ao inves de reiniciar a pagina
  //e mandar a requisicao toda hora!!!
}

interface ScheduleGetDto {
  workoutName: string,
  customerName: string,
  hourStart: string,
  hourEnd: string,
  dayOfTheWeek: number,
  scheduleId: number,
  customerId: number
}
