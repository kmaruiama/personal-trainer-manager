import { IonCard, IonImg, IonCardSubtitle, IonContent, IonCardContent, IonTitle } from '@ionic/angular/standalone';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AlertController } from '@ionic/angular';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-customer-profile',
  templateUrl: './customer-profile.component.html',
  styleUrls: ['./customer-profile.component.scss'],
  standalone: true,
  imports: [IonTitle, IonCardContent, IonContent, IonImg, IonCard, IonCardSubtitle, CommonModule]
})
export class CustomerProfileComponent implements OnInit {
  authToken: string = localStorage.getItem('authToken') || '';
  customerId: number = 0;
  customerName: string = "";
  nextWorkout: string = "";
  lastWorkout: string [] = [];
  noPreviousWorkoutsFound: boolean = true;

  constructor(private router: Router, private http: HttpClient, private alertController: AlertController) {}

  goToEditCustomer(customerId: number) {
    this.router.navigate(['/customer/edit'], {
      state: { id: customerId }
    });
  }


  showDeleteCustomer(){
    this.showConfirmationDeleteAlert();
  }

  goToCustomerProgramBluePrint(customerId: number){
    this.router.navigate(['/customer/blueprint'],{
      state: { id: customerId },
    });
  }

  goToCustomerReport(customerId: number){
    this.router.navigate(['/customer/report'],{
      state: { id: customerId },
    });
  }

  goToCustomerInfo(customerId: number){
    this.router.navigate(['/customer/info'], {
      state: { id: customerId },
    });
  }

  goToCustomerSchedule(customerId: number){
    this.router.navigate(['/customer/schedule'], {
      state: { id: customerId },
    });
  }
  goToWorkout(workoutId: number){

  }
  ngOnInit() {
    const navigation = this.router.getCurrentNavigation();
    this.customerId = navigation?.extras.state?.['id'] || null;
    this.customerName = navigation?.extras.state?.['name']||null;

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
  }

  getScheduleProfile(customerId: number, authToken: string) {
    const url = `http://localhost:8080/api/schedule/profile`;
    const headers = new HttpHeaders().set('Authorization', `Bearer ${authToken}`);
    return this.http.get<string[]>(url, { headers, params: { id: customerId.toString() } });
  }

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

  deleteCustomer() {
    const headers = { Authorization: `Bearer ${this.authToken}` };
    this.http
      .delete(`http://localhost:8080/api/customer/profile/delete?id=${this.customerId}`, {
        headers,
      })
      .subscribe(
        (response) => {
          this.router.navigate(['/customer']);
        },
        (error) => {
          this.showErrorAlert("Erro ao deletar o cliente");
        }
      );
  }

  checkLastWorkoutsEmpty() : boolean {
    if(this.lastWorkout.length === 0){
      return true;
    }
    else{
      this.noPreviousWorkoutsFound = false;
      return false;
    }
  }

  async showErrorAlert(message: string) {
    const alert = await this.alertController.create({
      header: 'Erro',
      message: message,
      buttons: ['OK'],
    });

    await alert.present();
  }


}
