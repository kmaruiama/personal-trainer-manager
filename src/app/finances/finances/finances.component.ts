import {
  IonContent,
  IonTitle,
  IonCardSubtitle,
  IonCard, IonButton } from '@ionic/angular/standalone';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { PaymentNodesComponent } from '../../payment-nodes/payment-nodes/payment-nodes.component';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-finances',
  templateUrl: './finances.component.html',
  styleUrls: ['./finances.component.scss'],
  imports: [IonButton,
    IonCard,
    IonContent,
    IonTitle,
    IonCardSubtitle,
    PaymentNodesComponent,
    CommonModule,
  ],
  standalone: true,
})
export class FinancesComponent implements OnInit {
  authToken: string = localStorage.getItem('authToken') || '';
  monthlyEstimatedRevenue: number = 0;

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit() {
    if (this.authToken !== ''){
      this.fetchMonthlyEstimatedRevenue(this.authToken)?.subscribe(
        (data) => {
          this.monthlyEstimatedRevenue = data;
        },
        (error) => {
          console.log('deu ruim');
        }
      );
    }
  }

  fetchMonthlyEstimatedRevenue(authToken: string) {
    const url = `http://localhost:8080/api/payment/monthlyRevenue`;
    const headers = new HttpHeaders().set(
      'Authorization',
      `Bearer ${authToken}`
    );
    if (authToken !== '') {
      return this.http.get<number>(url, { headers });
    } else return null;
  }

  goToEditPayments(){
    this.router.navigate(['finances/edit']);
  }

  goToClientList(){
    this.router.navigate(['finances/list'])
  }

}

interface PaymentGetDto {
  paymentId: number;
  price: number;
  dueDate: string;
  paymentType: string;
  customerName: string;
  payed: boolean
}
