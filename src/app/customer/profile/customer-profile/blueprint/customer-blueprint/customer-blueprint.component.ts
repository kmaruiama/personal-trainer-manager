import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-customer-blueprint',
  templateUrl: './customer-blueprint.component.html',
  styleUrls: ['./customer-blueprint.component.scss'],
  standalone: true,
  imports: []
})
export class CustomerBlueprintComponent implements OnInit {
  private customerId: number = 0;
  private customerName: string = "";
  private nomeDoPrograma: string = "";
  private workouts: Workout[] = [];

  constructor(private router: Router, private http: HttpClient) {}

  ngOnInit() {
    const navigation = this.router.getCurrentNavigation();
    this.customerId = navigation?.extras.state?.['id'] || null;
    this.customerName = navigation?.extras.state?.['name'] || null;

    if (this.customerId) {
      const authToken = localStorage.getItem('authToken');
      if (authToken) {
        this.getCurrentProgramBlueprint(this.customerId, authToken).subscribe(
          (data) => this.convertServerResponseIntoProgramData(data),
          () => this.router.navigate(['/customer/profile'])
        );
      }
    }
  }

  getCurrentProgramBlueprint(customerId: number, authToken: string) {
    const url = `http://localhost:8080/api/workout`;
    const headers = new HttpHeaders().set('Authorization', `Bearer ${authToken}`);
    return this.http.get<ProgramDto>(url, { headers, params: { id: customerId.toString() } });
  }

  convertServerResponseIntoProgramData(data: ProgramDto) {
    this.nomeDoPrograma = data.name;
    this.workouts = data.workoutGetDtoList.map((workout) => ({
      workoutName: workout.name,
      exercises: workout.exerciseGetDtoList
        .filter((exercise) => exercise.setGetDtoList.length > 0)
        .map((exercise) => ({
          name: exercise.name,
          sets: exercise.setGetDtoList.length,
          reps: exercise.setGetDtoList[0]?.repetitions || 0
        })),
      deleteFlag: workout.deleteFlag
    }));
    console.log(this.workouts);
  }
}

type Workout = {
  workoutName: string;
  exercises: Exercise[];
  deleteFlag: boolean;
};

type Exercise = {
  name: string;
  sets: number;
  reps: number;
};


//preciso aprender a manipular json de forma mais eficiente, que coisa feia
interface ProgramDto {
  name: string;
  workoutGetDtoList: {
    name: string;
    exerciseGetDtoList: {
      name: string;
      setGetDtoList: {
        repetitions: number;
      }[];
    }[];
    deleteFlag: boolean;
  }[];
}

//basicamente tem 2 coisas rolando aqui, a resposta do servidor com os nomes dos dtos e a a conversao deles
//pra workouts[] & exercises[]
