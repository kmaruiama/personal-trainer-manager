import { IonCard, IonImg, IonCardSubtitle, IonContent, IonCardContent } from '@ionic/angular/standalone';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AlertController } from '@ionic/angular';

@Component({
  selector: 'app-customer-profile',
  templateUrl: './customer-profile.component.html',
  styleUrls: ['./customer-profile.component.scss'],
  standalone: true,
  imports: [IonCardContent, IonContent, IonImg, IonCard, IonCardSubtitle]
})
export class CustomerProfileComponent implements OnInit {
  authToken: string = localStorage.getItem('authToken') || '';
  customerId: number = 0;
  customerName: string = "";
  nextWorkout: string = "";
  lastWorkout1: string = "";
  lastWorkout2: string = "";
  lastWorkout3: string = "";

  constructor(private router: Router, private http: HttpClient, private alertController: AlertController) {}

  goToEditCustomer(customerId: number) {
    this.router.navigate(['/customer/edit'], {
      state: { id: customerId },
    });
  }


  showDeleteCustomer(){
    this.showConfirmationDeleteAlert();
  }

  goToCustomerProgramBluePrint(){

  }

  goToCustomerReport(customerId: number){
    this.router.navigate(['/customer/report'],{
      state: { id: customerId },
    }
    )
  }

  goToCustomerInfo(customerId: number){
    this.router.navigate(['/customer/info'], {
      state: { id: customerId },
    });
  }

  goToCustomerSchedule(){

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
            this.nextWorkout = data[0];
            this.lastWorkout1 = data[1];
            this.lastWorkout2 = data[2];
            this.lastWorkout3 = data[3];
          },
          (error) => {
            console.error('Error fetching schedule profile:', error);
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
          console.log('Customer deleted successfully:', response);
          this.router.navigate(['/customer']);
        },
        (error) => {
          console.error('Error deleting customer:', error);
        }
      );
  }

}
