import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { IonImg, IonCard, IonLabel, IonContent, IonTitle, IonInput, IonButton} from '@ionic/angular/standalone';

@Component({
  selector: 'app-add-workout-blueprint',
  templateUrl: './add-workout-blueprint.component.html',
  styleUrls: ['./add-workout-blueprint.component.scss'],
  imports: [ IonButton, IonInput, IonContent, IonLabel, IonCard, IonImg, CommonModule, FormsModule],
  standalone: true,
})

export class AddWorkoutBlueprintComponent implements OnInit {
  authToken: string = localStorage.getItem('authToken') || '';

  workout: Workout = {
    programId: 0,
    customerId: 0,
    workoutName: "",
    exercises: [],
  };

  constructor(private router: Router, private http: HttpClient) {}

  ngOnInit() {
    const navigation = this.router.getCurrentNavigation();
    this.workout.customerId = navigation?.extras.state?.['customerId'] || null;
    this.workout.programId = navigation?.extras.state?.['programId'] || null;
  }

  addNewExercise() {
    const exercise: Exercise = {
      name: "",
      sets: 0,
      reps: 0,
      setId: [],
    };
    this.workout.exercises.push(exercise);
  }

  removeExercise(exercise: Exercise) {
    const index = this.workout.exercises.indexOf(exercise);
    if (index >= 0) {
      this.workout.exercises.splice(index, 1);
    }
  }

  submitNewWorkout() {
    const payload = {
      programId: this.workout.programId,
      id: null,
      name: this.workout.workoutName,
      customerId: this.workout.customerId,
      exerciseDtoList: this.workout.exercises.map((exercise) => ({
        id: null,
        name: exercise.name,
        setDtoList: Array(exercise.sets)
          .fill(null)
          .map(() => ({
            id: null,
            repetitions: exercise.reps,
            weight: 0,
          })),
      })),
    };

    //escrever as checagens de campo
    console.log(payload);

    let breaker: boolean = false;
    if (this.workout.workoutName === ""){
      breaker = true;
    }

    if(this.workout.exercises.length === 0){
      breaker = true;
    }

    for (let i: number = 0; i < this.workout.exercises.length; i++){
      if (this.workout.exercises[i].name === "" || this.workout.exercises[i].sets === 0){
        breaker = true;
      }
    }


    if(breaker){
      console.log("colocar aviso de treino inicializado de forma errada aqui");
    }
    else{
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
  }
}

type Workout = {
  programId: number;
  customerId: number;
  workoutName: string;
  exercises: Exercise[];
};

type Exercise = {
  name: string;
  sets: number;
  reps: number;
  setId: number[];
};
