import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import {
  IonImg,
  IonCard,
  IonLabel,
  IonContent,
  IonTitle,
  IonInput,
  IonButton,
} from '@ionic/angular/standalone';

@Component({
  selector: 'app-edit-workout-blueprint',
  templateUrl: './edit-workout-blueprint.component.html',
  styleUrls: ['./edit-workout-blueprint.component.scss'],
  imports: [
    IonButton,
    IonInput,
    IonTitle,
    IonContent,
    IonLabel,
    IonCard,
    IonImg,
    CommonModule,
    FormsModule,
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
    //meio bobinho fazer isso mas economiza uma consulta no banco de dados
    const workoutId = localStorage.getItem('workoutIdReloadParameter');

    if (workoutId === null) {
      const navigation = this.router.getCurrentNavigation();
      this.workout = navigation?.extras.state?.['workout'] || null;
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
          console.log(workoutDto);
          localStorage.removeItem('workoutIdReloadParameter');
          this.convertServerResponseIntoProgramData(workoutDto);
        },
        (error) => {
          console.error('Error connecting to the database:', error);
        }
      );
  }

  convertServerResponseIntoProgramData(workoutDto: WorkoutDto) {
    this.workout.workoutName = workoutDto.name;
    this.workout.customerId = workoutDto.customerId;
    this.workout.id = workoutDto.id;
    this.workout.exercises = workoutDto.exerciseDtoList
      .filter((exercise) => exercise.setDtoList.length > 0)
      .map((exercise) => ({
        id: exercise.id,
        name: exercise.name,
        sets: exercise.setDtoList.length,
        reps: exercise.setDtoList[0]?.repetitions || 0,
        setId: exercise.setDtoList.map((set) => set.id),
      }));
  }

  addNewExercise() {
    const exercise: Exercise = {
      id: -1,
      name: '',
      sets: 0,
      reps: 0,
      setId: [],
    };
    this.workout.exercises.push(exercise);
  }

  removeExercise(exercise: Exercise) {
    const index = this.workout.exercises.indexOf(exercise);
    if (exercise.id !== -1) {
      this.workout.exercises.splice(index, 1);
      this.deleteExerciseFromDatabase(exercise.id);
    }
    if (index >= 0) {
      this.workout.exercises.splice(index, 1);
    }
  }

  deleteExerciseFromDatabase(id: number) {
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
          localStorage.setItem(
            'workoutIdReloadParameter',
            this.workout.id.toString()
          );
          location.reload();
        },
        (error) => {
          console.error('Erro ao deletar exercício do programa:', error);
        }
      );
  }

  submitEditedWorkout() {
    const payload = {
      programId: null,
      id: this.workout.id,
      name: this.workout.workoutName,
      customerId: this.workout.customerId,
      exerciseDtoList: this.workout.exercises.map((exercise) => ({
        id: exercise.id,
        name: exercise.name,
        setDtoList: Array(exercise.sets) //declara um array de .set posicoes
          .fill(null)
          .map((_, index) => ({
            /*para nao somar o numero de sets que ja existem na db com os do frontend, mapeio os ids da
            tabela de sets do backend e jogo aqui, ou menos -1 caso adiciione mais do que existem lá*/
            id: exercise.setId?.[index] ?? -1,
            repetitions: exercise.reps,
            //o peso nao importa no programa
            weight: 0,
          })),
      })),
    };

    let breaker: boolean = false;
    if (this.workout.workoutName === '') {
      breaker = true;
    }

    if (this.workout.exercises.length === 0) {
      breaker = true;
    }

    for (let i: number = 0; i < this.workout.exercises.length; i++) {
      if (
        this.workout.exercises[i].name === '' ||
        this.workout.exercises[i].sets === 0
      ) {
        breaker = true;
      }
    }

    if (breaker) {
      console.log('colocar aviso de treino inicializado de forma errada aqui');
    } else {
      const headers = { Authorization: `Bearer ${this.authToken}` };
      this.http
        .patch(`http://localhost:8080/api/workout`, payload, { headers })
        .subscribe(
          (response) => {
            localStorage.setItem(
              'workoutIdReloadParameter',
              this.workout.id.toString()
            );
            location.reload();
          },
          (error) => {
            console.error('Erro ao editar novo treino', error);
          }
        );
    }
  }

  //criar os inicializadores dos treinos
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
  setId: number[];
};

type WorkoutDto = {
  customerId: number;
  id: number;
  name: string;
  exerciseDtoList: {
    id: number;
    name: string;
    setDtoList: {
      repetitions: number;
      id: number;
    }[];
  }[];
};
