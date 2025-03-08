import { map } from 'rxjs';
import { IonCard, IonImg, IonCardSubtitle, IonContent, IonCardContent, IonTitle, IonAlert } from '@ionic/angular/standalone';
import { Component, OnInit, SimpleChange } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AlertController } from '@ionic/angular';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-customer-profile',
  templateUrl: './customer-profile.component.html',
  styleUrls: ['./customer-profile.component.scss'],
  standalone: true,
  imports: [IonAlert, IonCardContent, IonContent, IonImg, IonCard, IonCardSubtitle, CommonModule]
})
export class CustomerProfileComponent implements OnInit {

  authToken: string = localStorage.getItem('authToken') || '';
  customerId: number = 0;
  customerName: string = "";

  //relacionado aos ultimos treinos
  lastWorkout: string [] = [];
  noPreviousWorkoutsFound: boolean = true;

  //relacionados ao proximo treino
  nextWorkoutButtonClicked : boolean = false;

  nextWorkouts: NextWorkoutDto [] = [];
  selectedWorkout: NextWorkoutDto = {name: "", blueprintId: 0, noBlueprintId: 0}

  nextWorkoutButton: Array<{ text: string; handler: () => void }> = [];

  referenceChoiceTrigger : boolean = false;
  multipleWorkoutMode : boolean = false;

  blueprintAsReference : boolean = false;

  public nextWorkoutButtons = [
    {
      text: "Não",
      handler: () => {
        this.goToWorkout(this.selectedWorkout.blueprintId);
      }
    }
  ]


  constructor(private router: Router, private http: HttpClient, private alertController: AlertController) {}

  ngOnInit() {
    const navigation = this.router.getCurrentNavigation();
    this.customerId = navigation?.extras.state?.['id'] || null;
    this.customerName = navigation?.extras.state?.['name'] || null;

    if (this.customerId) {
      const authToken = localStorage.getItem('authToken');
      if (authToken) {
        this.getScheduleProfile(this.customerId, authToken).subscribe(
          (data) => {
            this.lastWorkout = data;
          },
          (error) => {
            this.router.navigate(['/customer/']);
          }
        );
      }
    }
    this.fetchNextWorkout(this.authToken, this.customerId);
  }

  //botoes
  showDeleteCustomer() {
    this.showConfirmationDeleteAlert();
  }

  goTo(customerId: number, url: string) {
    this.router.navigate([`/customer/${url}`], {
      state: { id: customerId },
    });
  }

  goToWorkout(workoutId: number) {
    this.router.navigate(['workout'], {
      state: { id: workoutId },
    });
  }

  //ultimos treinos realizados
  getScheduleProfile(customerId: number, authToken: string) {
    const url = `http://localhost:8080/api/schedule/profile`;
    const headers = new HttpHeaders().set('Authorization', `Bearer ${authToken}`);
    return this.http.get<string[]>(url, { headers, params: { id: customerId.toString() } });
  }

  checkLastWorkoutsEmpty(): boolean {
    if (this.lastWorkout.length === 0) {
      return true;
    } else {
      this.noPreviousWorkoutsFound = false;
      return false;
    }
  }

  //buscar proximo treino
  fetchNextWorkout(authToken: string, customerId: number) {
    const headers = { Authorization: `Bearer ${authToken}` };
    this.http.get<NextWorkoutDto[]>(`http://localhost:8080/api/workout/next?customerId=${customerId}`, { headers }).subscribe({
      next: (data) => {
        this.nextWorkouts = data;
        this.selectNextWorkoutMode();
      },
      error: (error) => {
        console.error("Erro", error);
      }
    });
  }

  selectNextWorkoutMode() : void {
    if (this.nextWorkouts.length > 1) {
      this.multipleWorkoutMode = true;
    }
    else {
      this.selectedWorkout = this.nextWorkouts[0];
      this.pushesYesSelectionIfPreviousWorkoutExists();
      this.referenceChoiceTrigger = true;
    }
  }

  pushesYesSelectionIfPreviousWorkoutExists(){
    if (this.selectedWorkout.noBlueprintId != null){
      this.nextWorkoutButton.push({
        text: "Sim",
        handler: () => {
        this.goToWorkout(this.selectedWorkout.noBlueprintId);
      }
      })
    }
  }

  initializeNextWorkoutButtonNames(): void {
    this.nextWorkoutButton = this.nextWorkouts.map((workout) => ({
      text: workout.name,
      handler: () => {
        this.selectedWorkout = workout;
        this.pushesYesSelectionIfPreviousWorkoutExists();
        this.referenceChoiceTrigger = true;
      }
    }));
  }


  nextWorkoutClicker () : void {
    this.nextWorkoutButtonClicked = true;
  }

  closeNextWorkoutSelection() : void {
    this.nextWorkoutButtonClicked = false;
  }

  //alertas (mover pro ion dps)
  async showConfirmationDeleteAlert() {
    const alert = await this.alertController.create({
      header: 'DELETAR CLIENTE',
      message: 'VOCÊ TEM CERTEZA? TODAS AS INFORMAÇÕES RELACIONADAS SERÃO EXCLUÍDAS.',
      buttons: [
        {
          text: 'CANCELAR',
          role: 'cancel',
        },
        {
          text: 'OK',
          handler: () => {
            this.deleteCustomer();
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
    await alert.present();
  }

  //deletar cliente
  deleteCustomer() {
    const headers = { Authorization: `Bearer ${this.authToken}` };
    this.http
      .delete(`http://localhost:8080/api/customer/profile/delete?id=${this.customerId}`, { headers })
      .subscribe(
        (response) => {
          this.router.navigate(['/customer']);
        },
        (error) => {
          this.showErrorAlert("Erro ao deletar o cliente");
        }
      );
  }
}

type NextWorkoutDto = {
  name : string;
  noBlueprintId : number;
  blueprintId : number;
}
