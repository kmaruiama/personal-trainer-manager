import { IonContent, IonRadioGroup, IonItem, IonLabel, IonRadio, IonButton } from '@ionic/angular/standalone';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-customer-schedule-add',
  templateUrl: './customer-schedule-add.component.html',
  styleUrls: ['./customer-schedule-add.component.scss'],
  imports: [IonRadio, IonContent, IonRadioGroup, IonItem, IonLabel, IonRadio, ReactiveFormsModule, IonButton],
  standalone: true
})
export class CustomerScheduleAddComponent{
  form: FormGroup;

  constructor(private formBuilder : FormBuilder){
    this.form = this.formBuilder.group({
      dayOfTheWeek: [0]
    })
  }
  testForm(){

  }
}

interface newSchedule{
  workoutId: number,
  customerId: number,
  dayOfTheWeek: number,
  hourStart: string,
  hourEnd: string,
}
