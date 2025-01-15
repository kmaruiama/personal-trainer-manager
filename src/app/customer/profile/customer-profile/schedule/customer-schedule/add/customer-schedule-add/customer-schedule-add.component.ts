import { Router } from '@angular/router';
import { IonContent, IonRadioGroup, IonItem, IonLabel, IonRadio, IonButton, IonDatetime, IonTitle, IonText, IonInput } from '@ionic/angular/standalone';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { AlertController } from '@ionic/angular';



@Component({
  selector: 'app-customer-schedule-add',
  templateUrl: './customer-schedule-add.component.html',
  styleUrls: ['./customer-schedule-add.component.scss'],
  imports: [IonInput, IonText, IonTitle, IonRadio, IonContent, IonRadioGroup, IonItem, IonLabel, FormsModule, IonRadio, ReactiveFormsModule, IonButton, CommonModule],
  standalone: true
})

//nao vou usar o "time" do ion-datetime pq achei a interface feia
export class CustomerScheduleAddComponent implements OnInit{
  form: FormGroup;
  workouts: workout[] = [];
  authToken: null | string = '';
  customerId: number = 0;
  timeInput: string = '';
  timeError: string = '';

  constructor(private formBuilder: FormBuilder, private router: Router, private http: HttpClient, private alertController: AlertController) {
    this.form = this.formBuilder.group({
      dayOfTheWeek: [0, Validators.required],
      workoutId: [0, Validators.required],
      hourStart: ['', Validators.required],
      hourEnd: ['', Validators.required],
    });
  }

  validateSchedule(){
    const hourStart = this.form.get('hourStart')?.value;
    const hourEnd = this.form.get('hourEnd')?.value;
    const workoutId = this.form.get('workoutId')?.value;
    const dayOfTheWeek = this.form.get('dayOfTheWeek')?.value;
    if(!this.validateHours(hourStart) && this.validateHours(hourEnd)){
      return;
    }
    if (this.customerId === 0){
      return;
    }
    if (workoutId === 0){
      return;
    }
    if(dayOfTheWeek === 0){
      return;
    }
    this.postSchedule(hourStart, hourEnd, workoutId, dayOfTheWeek)

  }

  postSchedule(hourStart: number, hourEnd: number, workoutId: number, dayOfTheWeek: number){
    const payload = {
      workoutId : workoutId,
      customerId: this.customerId,
      dayOfTheWeek: dayOfTheWeek,
      hourStart: hourStart,
      hourEnd: hourEnd
    }

    const headers = { Authorization: `Bearer ${this.authToken}` };

    this.http
      .post(`http://localhost:8080/api/schedule/new`, { headers, body: payload,})
      .subscribe(
        (response) => {
          console.log('Agendamento realizado com sucesso', response);
        },
        (error) => {
          this.showErrorAlert("Erro ao agendar o treino");
          console.error('Erro ao deletar o treino:', error);
        }
      );
  }
//tive que improvisar isso pq nao tinha nenhuma biblioteca simples pra fazer a validacao de horario
  validateHours(time: string): boolean {
    if (time.length !== 5) {
      return false;
    }

    let hrColon = time.substring(2, 3);
    if (hrColon !== ':') {
      return false;
    }

    let hours: string = time.substring(0, 2);
    let minutes: string = time.substring(3, 5);

    let hoursNumber: number = parseInt(hours);
    let minutesNumber: number = parseInt(minutes);

    if (hoursNumber < 0 || hoursNumber > 23) {
      return false;
    }
    if (minutesNumber < 0 || minutesNumber > 59) {
      return false;
    }

    return true;
  }


  ngOnInit(){
    this.authToken = localStorage.getItem('authToken');
    const navigation = this.router.getCurrentNavigation();
    this.customerId = navigation?.extras.state?.['customerId'] || null;
    if (this.customerId !== 0) {
      if (this.authToken !== null) {
        this.getCurrentProgramBlueprint(this.customerId, this.authToken).subscribe(
          (data) => this.convertServerResponseIntoProgramData(data),
          () => this.router.navigate(['/customer/profile'])
        );
      }
    }
  }

  getCurrentProgramBlueprint(customerId: number, authToken: string) {
      const url = `http://localhost:8080/api/workout/program`;
      const headers = new HttpHeaders().set('Authorization', `Bearer ${authToken}`);
      return this.http.get<ProgramDto>(url, { headers, params: { id: customerId.toString() } });
    }

    convertServerResponseIntoProgramData(data: ProgramDto) {
      this.workouts = data.workoutDtoList.map((workout) => ({
        customerId: this.customerId,
        workoutId: workout.id,
        workoutName: workout.name,
      }));
    }

    async showErrorAlert(message: string) {
      const alert = await this.alertController.create({
        header: 'Erro',
        message: message,
        buttons: ['OK'],
      });
    }
}

interface newSchedule{
  workoutId: number,
  customerId: number,
  dayOfTheWeek: number,
  hourStart: string,
  hourEnd: string,
}

interface workout{
  customerId: number,
  workoutId: number,
  workoutName: string
}

interface ProgramDto {
  workoutDtoList: {
    customerId: number;
    id: number;
    name: string;
  }[];
}
