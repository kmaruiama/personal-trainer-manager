import { IonTitle, IonCard, IonContent, IonCardSubtitle } from '@ionic/angular/standalone';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-customer-report',
  templateUrl: './customer-report.component.html',
  styleUrls: ['./customer-report.component.scss'],
  standalone: true,
  imports: [IonTitle, IonCard, IonContent, IonCardSubtitle]
})
export class CustomerReportComponent implements OnInit {

  customerId: number = 0;
  weight: number | null = null;
  bodyFatPercentage: number | null = null;
  height: number | number | null = null;

  constructor(private router: Router, private http: HttpClient) {}

  ngOnInit() {
    const navigation = this.router.getCurrentNavigation();
    this.customerId = navigation?.extras.state?.['id'] || null;

    if (this.customerId != 0) {
      this.fetchLastInput(this.customerId);
    }
  }

  fetchLastInput(customerId: number) {
    const authToken = localStorage.getItem('authToken');

    if (authToken) {
      const headers = { Authorization: `Bearer ${authToken}` };

      this.http.get(`http://localhost:8080/api/weight/last?customerId=${customerId}`, { headers })
        .subscribe({
          next: (response: any) => {
            this.weight = response.weight;
            this.bodyFatPercentage = response.bodyFatPercentage;
            this.height = response.height;
          },
        });
    } else {
      console.error('No auth token found');
      alert('Autenticação necessária para acessar as informações.');
    }
  }

  goToUpdateMass(customerId: number) {
    this.router.navigate(['/customer/report/update'], { state: { id: customerId } });
  }

  goToMassChart(customerId: number) {
    this.router.navigate(['/customer/report/chart'], { state: { id: customerId } });
  }

  goToAllInputs(customerId: number){

  }

}
