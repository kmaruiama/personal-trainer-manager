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
    customerId: 0,
    workoutName: "",
    exercises: [],
  };

  constructor(private router: Router, private http: HttpClient) {}

  ngOnInit() {
    const navigation = this.router.getCurrentNavigation();
    this.workout.customerId = navigation?.extras.state?.['customerId'] || null;
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
      programId: null,
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

    for (let i: number = 0; i < this.workout.exercises.length; i++){
      if (this.workout.exercises[i].name === "" || this.workout.exercises[i].sets === 0){
        breaker = true;
      }
    }

    if(breaker){

    }
    else{

    }
  }
}

type Workout = {
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
