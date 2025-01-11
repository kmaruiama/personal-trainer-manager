import { Component, OnInit } from '@angular/core';
import { IonContent } from "@ionic/angular/standalone";
import { SharedScheduleNodesComponent } from "../../../../../schedule-nodes/shared-schedule-nodes.component";

@Component({
  selector: 'app-customer-schedule',
  templateUrl: './customer-schedule.component.html',
  styleUrls: ['./customer-schedule.component.scss'],
  imports: [IonContent, SharedScheduleNodesComponent],
  standalone: true
})
export class CustomerScheduleComponent  implements OnInit {

  constructor() { }

  ngOnInit() {}

}
