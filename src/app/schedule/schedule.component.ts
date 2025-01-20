import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { IonContent, IonButton, IonImg } from "@ionic/angular/standalone";
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AlertController } from '@ionic/angular';
import { SharedScheduleNodesComponent } from '../schedule-nodes/shared-schedule-nodes.component';


@Component({
  selector: 'app-schedule',
  templateUrl: './schedule.component.html',
  styleUrls: ['./schedule.component.scss'],
  standalone: true,
  imports: [IonButton, IonContent, SharedScheduleNodesComponent, CommonModule]
})

export class ScheduleComponent implements OnInit{
  protected schedules: Schedule[] = [];
  constructor(private router: Router, private http: HttpClient, private alertController: AlertController) { }
  authToken: string = localStorage.getItem('authToken') || '';

  ngOnInit(){
    this.fetchTrainerSchedule(this.authToken)?.subscribe(
      (data)=> this.convertServerResponse(data),
      (error) => {
        console.log("deu ruim");
      }
    )
  }

  fetchTrainerSchedule(authToken: string){
    const url = `http://localhost:8080/api/schedule/list`;
    const headers = new HttpHeaders().set('Authorization', `Bearer ${authToken}`);
    if (authToken !== ''){
      return this.http.get<ScheduleGetDto[]>(url, {headers});
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

  trimSeconds(hours: string){
    return hours.substring(0, 5);
  }

  returnDayOfTheWeek(index: number){
    const semana : string [] = ["Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado", "Domingo"]
    return semana[index];
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
