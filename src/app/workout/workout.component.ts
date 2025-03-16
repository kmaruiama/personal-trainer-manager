import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { IonContent, IonTitle, IonLabel, IonInput, IonCard, IonImg, IonButton} from "@ionic/angular/standalone";

@Component({
  selector: 'app-workout',
  templateUrl: './workout.component.html',
  styleUrls: ['./workout.component.scss'],
  imports: [IonButton, IonImg, IonCard, IonInput, IonLabel, IonTitle, IonContent, CommonModule, FormsModule],
  standalone: true
})

export class WorkoutComponent  implements OnInit {
  authToken: string = localStorage.getItem('authToken') || '';

  workout: WorkoutDto = {
    programId : 0,
    id : 0,
    name : "",
    customerId:0,
    exerciseDtoList : [],
    blueprint : false
  }

  workoutId: number = 0;

  constructor(private router : Router,
              private http : HttpClient
  ) {

  }

  ngOnInit() {
    const navigation = this.router.getCurrentNavigation();
    this.workoutId = navigation?.extras.state?.['id'] || null;
    if (this.workoutId !== null){
      this.fetchWorkout(this.workoutId);
    }
  }

  fetchWorkout(workoutId: number){
    const headers = { Authorization: `Bearer ${this.authToken}` };
    this.http.get<WorkoutDto>(`http://localhost:8080/api/workout?id=${workoutId}`, { headers }).subscribe({
      next: (data) => {
        this.workout = data;
        this.initializeIndexes();
      },
      error: (error) => {
        console.error("Erro", error);
      }
    });
  }

  initializeIndexes() {
    for (let i = 0; i< this.workout.exerciseDtoList.length; i++){
      this.workout.exerciseDtoList[i].index = i+1
      for (let j = 0; j< this.workout.exerciseDtoList[i].setDtoList.length; j++){
        this.workout.exerciseDtoList[i].setDtoList[j].index = j+1;
      }
    }
  }

  postWorkout(){
    const payload = {
      programId: null,
      id: null,
      name: this.workout.name,
      customerId: this.workout.customerId,
      exerciseDtoList: this.workout.exerciseDtoList.map((exercise) => ({
        id: null,
        name: exercise.name,
        setDtoList: exercise.setDtoList.map((set) => ({
          id: null,
          repetitions: set.repetitions,
          weight: set.weight !== undefined ? set.weight : 0,
        })),
      })),
      blueprint: false,
    };

    const headers = { Authorization: `Bearer ${this.authToken}` };
    this.http
        .post(`http://localhost:8080/api/workout`, payload, { headers })
        .subscribe(
          (response) => {
            console.log('Treino adicionado com sucesso', response);
          },
          (error) => {
            console.error('Erro ao adicionar novo treino', error);
          }
    );
  }

  onWeightChange(event: any, set: any) {
    const newValue = event.detail.value;
    set.weight = newValue === '' ? null : newValue;
  }

  onRepetitionsChange(event: any, set: any) {
    const newValue = event.detail.value;
    set.repetitions = newValue === '' ? null : newValue;
  }

}

type WorkoutDto = {
  programId : number;
  id : number;
  name : string;
  customerId : number;
  exerciseDtoList : ExerciseDto[];
  blueprint : boolean;
}

type ExerciseDto = {
  index : number;
  id : number;
  name : string;
  setDtoList : SetDto[];
}

type SetDto = {
  index : number;
  id : number;
  repetitions : number;
  weight : number
}
