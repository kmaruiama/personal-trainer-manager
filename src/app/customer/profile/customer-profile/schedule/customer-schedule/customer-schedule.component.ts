import { Component, OnInit } from '@angular/core';
import { IonContent, IonButton } from "@ionic/angular/standalone";
import { SharedScheduleNodesComponent } from "../../../../../schedule-nodes/shared-schedule-nodes.component";
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-customer-schedule',
  templateUrl: './customer-schedule.component.html',
  styleUrls: ['./customer-schedule.component.scss'],
  imports: [IonButton, IonContent, SharedScheduleNodesComponent, CommonModule],
  standalone: true
})
export class CustomerScheduleComponent  implements OnInit {


  addNewSchedule(){

  }
  deleteSchedule(){

  }
  schedules = [
    { customerName: 'Jane Smith', scheduleHour: '3:00 PM', workoutName: 'HIIT Training' },
    { customerName: 'John Doe', scheduleHour: '4:30 PM', workoutName: 'Pilates' },
    { customerName: 'Alice Johnson', scheduleHour: '6:00 PM', workoutName: 'Yoga' }
  ];


  constructor() { }

  ngOnInit() {}

}

interface schedule {
  customerName: string,
  scheduleHourStart: string,
  scheduleHourEnd: string,
  workoutName: string
  scheduleId: number,
  customerId: number
  deleteflag: boolean
  //boa sacada deixar o deleteflag só no frontend, depois é só iterar através do array de
  //schedules e ir deletando. Fazer isso no programa de treino tb ao inves de reiniciar a pagina
  //e mandar a requisicao toda hora!!!
}
