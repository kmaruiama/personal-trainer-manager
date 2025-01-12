import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { IonContent, IonButton } from "@ionic/angular/standalone";
import { SharedScheduleNodesComponent } from "../../../../../schedule-nodes/shared-schedule-nodes.component";
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-customer-schedule',
  templateUrl: './customer-schedule.component.html',
  styleUrls: ['./customer-schedule.component.scss'],
  imports: [IonButton, IonContent, SharedScheduleNodesComponent, CommonModule],
  standalone: true
})
export class CustomerScheduleComponent  implements OnInit {
  authToken: string = localStorage.getItem('authToken') || '';

  private customerId: number = 0;
  protected schedules: Schedule[] = [];
  constructor(private router: Router, private http: HttpClient) { }

  ngOnInit() {
    const navigation = this.router.getCurrentNavigation();
    this.customerId = navigation?.extras.state?.['id'] || null;

    if (this.customerId != 0) {
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
      //converter isso em horário normal
      scheduleHourStart: this.formatServerHours(schedule.dateStart),
      scheduleHourEnd: this.formatServerHours(schedule.dateEnd),
      dayOfTheWeek: this.formatServerWeek(schedule.dateStart),
      deleteflag: false
    }))
  }

  formatServerHours(rawDate: string): string {
    const date = new Date(rawDate);
    const hour = date.getHours().toString().padStart(2, '0');
    const minute = date.getMinutes().toString().padStart(2, '0');
    return `${hour}:${minute}`;
  }

  formatServerWeek(rawDate: string): number {
    const date = new Date(rawDate);
    return date.getDay();
  }

  addNewSchedule(){

  }

  deleteSchedule(){

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
  deleteflag: boolean
  //boa sacada deixar o deleteflag só no frontend, depois é só iterar através do array de
  //schedules e ir deletando. Fazer isso no programa de treino tb ao inves de reiniciar a pagina
  //e mandar a requisicao toda hora!!!
}

interface ScheduleGetDto {
  workoutName: string,
  customerName: string,
  dateStart: string,
  dateEnd: string,
  scheduleId: number,
  customerId: number
}
