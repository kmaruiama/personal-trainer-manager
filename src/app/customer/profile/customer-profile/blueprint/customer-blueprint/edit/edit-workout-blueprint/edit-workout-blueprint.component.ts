import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import {
  IonImg,
  IonCard,
  IonLabel,
  IonContent,
  IonTitle,
  IonInput,
} from '@ionic/angular/standalone';

@Component({
  selector: 'app-edit-workout-blueprint',
  templateUrl: './edit-workout-blueprint.component.html',
  styleUrls: ['./edit-workout-blueprint.component.scss'],
  imports: [
    IonInput,
    IonTitle,
    IonContent,
    IonLabel,
    IonCard,
    IonImg,
    CommonModule,
  ],
  standalone: true,
})

//aqui editamos o treino planejado (incluso exclusao e mudança de exercícios)
export class EditWorkoutBlueprintComponent implements OnInit {
  authToken: string = localStorage.getItem('authToken') || '';

  workout: Workout = {
    customerId: 0,
    id: 0,
    workoutName: '',
    exercises: [],
  };

  constructor(private router: Router, private http: HttpClient) {}

  ngOnInit() {
    const navigation = this.router.getCurrentNavigation();
    this.workout = navigation?.extras.state?.['workout'] || null;
  }

  addNewExercise() {
    const exercise: Exercise = {
      id: -1,
      name: 'Novo exercício',
      sets: 0,
      reps: 0,
    };
    this.workout.exercises.push(exercise);
  }

  removeExercise(exercise: Exercise) {
    const index = this.workout.exercises.indexOf(exercise);
    if (exercise.id !== -1) {
      this.deleteExerciseFromDatabase(exercise.id);
    }
    if (index >= 0) {
      this.workout.exercises.splice(index, 1);
    }
  }

  deleteExerciseFromDatabase(id : number) {
    const payload = {
      customerId: this.workout.customerId,
      workoutId: this.workout.id,
      exerciseId: id,
      setId: null,
      //nivel 1: deletando treinos
      //nivel 2: deletando exercicios
      //nivel 3: deletando séries
      treeDeletionLevel: 2,
    };

    const headers = { Authorization: `Bearer ${this.authToken}` };

    this.http
      .delete(`http://localhost:8080/api/workout/blueprint`, {
        headers: headers,
        body: payload,
      })
      .subscribe(
        (response) => {
          console.log('Sucesso ao deletar exercício do programa:', response);
        },
        (error) => {
          console.error('Erro ao deletar exercício do programa:', error);
        }
      );
  }
}

type Workout = {
  customerId: number;
  id: number;
  workoutName: string;
  exercises: Exercise[];
};

type Exercise = {
  id: number;
  name: string;
  sets: number;
  reps: number;
};
