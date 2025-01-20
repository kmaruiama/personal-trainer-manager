import { routes } from './../app.routes';
import { IonContent, IonMenu, IonHeader, IonTitle, IonToolbar, IonMenuButton, IonButtons, IonButton} from '@ionic/angular/standalone';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SharedScheduleNodesComponent } from "../schedule-nodes/shared-schedule-nodes.component";
import { CommonModule} from '@angular/common';

@Component({
  selector: 'app-menu-principal',
  standalone: true,
  templateUrl: './menu-principal.component.html',
  styleUrls: ['./menu-principal.component.scss'],
  imports: [IonContent, IonMenu, IonHeader, IonTitle, IonToolbar, IonMenuButton, IonButtons, IonButton, SharedScheduleNodesComponent, CommonModule]
})


export class MenuPrincipalComponent implements OnInit {

  constructor(private router: Router) {

  }

  goToScheduleMenu() {
    this.router.navigate(['schedule']);
  }
  goToPaymentMenu() {
  }
  goToWorkoutMenu() {
  }
  goToCustomerMenu() {
    this.router.navigate(['customer']);
  }

  schedules = [
    { customerName: 'Jane Smith', scheduleHour: '3:00 PM', workoutName: 'HIIT Training' },
    { customerName: 'John Doe', scheduleHour: '4:30 PM', workoutName: 'Pilates' },
    { customerName: 'Alice Johnson', scheduleHour: '6:00 PM', workoutName: 'Yoga' }
  ];

  ngOnInit(): void {

  }
}
