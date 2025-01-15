import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { IonContent, IonTitle, IonInput, IonItem, IonLabel, IonButton, IonCard, IonImg } from "@ionic/angular/standalone";
import { CommonModule } from '@angular/common';
import { AlertController } from '@ionic/angular';

@Component({
  selector: 'app-customer-blueprint',
  templateUrl: './customer-blueprint.component.html',
  styleUrls: ['./customer-blueprint.component.scss'],
  standalone: true,
  imports: [IonImg, IonCard, IonLabel, IonTitle, IonContent, CommonModule]
})
export class CustomerBlueprintComponent implements OnInit {
  customerId: number = 0;
  programId: number = 0;
  private customerName: string = "";
  protected nomeDoPrograma: string = "";
  protected workouts: Workout[] = [];
  private authToken: string | null= "";

  constructor(private router: Router, private http: HttpClient, private alertController: AlertController) {}

  ngOnInit() {
    this.authToken = localStorage.getItem('authToken');
    const navigation = this.router.getCurrentNavigation();
    this.customerId = navigation?.extras.state?.['id'] || null;
    this.customerName = navigation?.extras.state?.['name'] || null;

    if (this.customerId) {
      if (this.authToken) {
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
    this.programId = data.id;
    this.nomeDoPrograma = data.name;
    this.workouts = data.workoutDtoList.map((workout) => ({
      programId: data.programId,
      customerId: this.customerId,
      id: workout.id,
      workoutName: workout.name,
      exercises: workout.exerciseDtoList
        .filter((exercise) => exercise.setDtoList.length > 0)
        .map((exercise) => ({
          id: exercise.id,
          name: exercise.name,
          sets: exercise.setDtoList.length,
          reps: exercise.setDtoList[0]?.repetitions || 0,
          setId: exercise.setDtoList.map((set) => set.id)
        })),
    }));
  }

  goToAddNewWorkoutBlueprint(customerId : number, programId: number){
    this.router.navigate(['customer/blueprint/add'],
      {
        state: {
          customerId: customerId,
          programId: programId
        }
      }
    );
  }

  goToEditWorkout(workout : Workout){
    this.router.navigate(['customer/blueprint/edit'],
      {
        state: { workout: workout }
      }
    );
  }

  deleteWorkoutBlueprint(workout: Workout){
    const payload = {
      customerId: workout.customerId,
      workoutId: workout.id,
      exerciseId: null,
      setId: null,
      treeDeletionLevel: 1,
    };

    const headers = { Authorization: `Bearer ${this.authToken}` };

    this.http
      .delete(`http://localhost:8080/api/workout/blueprint`, { headers, body: payload,})
      .subscribe(
        (response) => {
          console.log('Treino deletado com sucesso', response);
        },
        (error) => {
          this.showErrorAlert("Erro ao deletar o treino");
          console.error('Erro ao deletar o treino:', error);
        }
      );
  }

  async showConfirmationDeleteAlert(workout : Workout) {
    const alert = await this.alertController.create({
      header: 'DELETAR TREINO',
      message: 'VOCÊ TEM CERTEZA? TODAS AS INFORMAÇÕES RELACIONADAS SERÃO EXCLUÍDAS.',
      buttons: [
        {
          text: 'CANCELAR',
          role: 'cancel',
        },
        {
          text: 'OK',
          handler: () => {
            this.deleteWorkoutBlueprint(workout);
          },
        },
      ],
    });
    await alert.present();
  }

  async showErrorAlert(message: string) {
    const alert = await this.alertController.create({
      header: 'Erro',
      message: message,
      buttons: ['OK'],
    });
  }
}

type Workout = {
  customerId: number;
  id: number;
  workoutName: string;
  exercises: Exercise[];
  programId: number;
};

type Exercise = {
  id: number;
  name: string;
  sets: number;
  reps: number;
  setId: number [];
};


//preciso aprender a manipular json de forma mais eficiente, que coisa feia
interface ProgramDto {
  programId: number;
  id: number;
  name: string;
  workoutDtoList: {
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
  }[];
}
