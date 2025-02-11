import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-workout',
  templateUrl: './workout.component.html',
  styleUrls: ['./workout.component.scss'],
  standalone: true
})
export class WorkoutComponent  implements OnInit {

  authToken: string = localStorage.getItem('authToken') || '';

  private workoutDto : WorkoutDto = {
    programId: 0,
    id : 0,
    name: "",
    customerId: 0,
    exerciseDtoList: []
  }

  constructor(
    private router : Router,
    private http: HttpClient,

  ) { }

  ngOnInit() {
    const workoutId = localStorage.getItem('workoutIdReloadParameter');

    if (workoutId === null) {
      const navigation = this.router.getCurrentNavigation();
      this.workoutDto = navigation?.extras.state?.['workout'] || null;
    } else {
      this.fetchWorkoutFromDatabase(workoutId);
    }
  }

  fetchWorkoutFromDatabase(id: string) {
    const headers = { Authorization: `Bearer ${this.authToken}` };
    this.http
      .get<WorkoutDto>(`http://localhost:8080/api/workout`, {
        headers,
        params: { id: id },
      })
      .subscribe(
        (workoutDto) => {
          localStorage.removeItem('workoutIdReloadParameter');
          this.convertServerResponseIntoProgramData(workoutDto);
        },
        (error) => {
          console.error('Error connecting to the database:', error);
        }
      );
  }

  convertServerResponseIntoProgramData(workoutDto: WorkoutDto) {
    this.workoutDto.name = workoutDto.name;
    this.workoutDto.customerId = workoutDto.customerId;
    this.workoutDto.id = workoutDto.id;
    this.workoutDto.exerciseDtoList = workoutDto.exerciseDtoList
      .filter((exercise) => exercise.setDtoList.length > 0)
      .map((exercise) => ({
        id: exercise.id,
        name: exercise.name,
        setDtoList: exercise.setDtoList
      }));
    console.log(this.workoutDto);
  }
}

type WorkoutDto = {
  programId : number;
  id : number;
  name: string;
  customerId : number
  exerciseDtoList : ExerciseDto[];
}

type ExerciseDto = {
  id : number;
  name : string;
  setDtoList : SetDto [];
}

type SetDto = {
  id : number;
  repetitions : number;
  weight : number
}
