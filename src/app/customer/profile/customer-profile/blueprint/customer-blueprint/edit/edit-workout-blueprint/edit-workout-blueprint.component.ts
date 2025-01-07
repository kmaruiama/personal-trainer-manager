import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { IonImg, IonCard, IonLabel, IonContent, IonTitle, IonInput } from "@ionic/angular/standalone";

@Component({
  selector: 'app-edit-workout-blueprint',
  templateUrl: './edit-workout-blueprint.component.html',
  styleUrls: ['./edit-workout-blueprint.component.scss'],
  imports: [IonInput, IonTitle, IonContent, IonLabel, IonCard, IonImg, CommonModule],
  standalone: true
})
export class EditWorkoutBlueprintComponent  implements OnInit {
  workout : Workout = {
      id: 0,
      workoutName: '',
      exercises: [],
  }

  constructor(private router : Router) { }

  ngOnInit() {
    const navigation = this.router.getCurrentNavigation();
    this.workout = navigation?.extras.state?.['workout'] || null;
    console.log(this.workout);
  }

  addNewExercise(){
     const exercise : Exercise = {
      name: "Novo exercício",
      sets: 0,
      reps: 0,
     }
     this.workout.exercises.push(exercise);
  }

  removeExercise(){

  }
}

type Workout = {
  id: number;
  workoutName: string;
  exercises: Exercise[];
};

type Exercise = {
  name: string;
  sets: number;
  reps: number;
};
