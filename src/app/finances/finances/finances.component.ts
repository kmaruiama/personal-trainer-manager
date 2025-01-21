import { IonContent, IonTitle, IonCardSubtitle, IonCard } from '@ionic/angular/standalone';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { PaymentNodesComponent } from "../../payment-nodes/payment-nodes/payment-nodes.component";
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-finances',
  templateUrl: './finances.component.html',
  styleUrls: ['./finances.component.scss'],
  imports: [IonCard, IonContent, IonTitle, IonCardSubtitle, PaymentNodesComponent, CommonModule],
  standalone: true
})
export class FinancesComponent  implements OnInit {
  authToken: string = localStorage.getItem('authToken') || '';
  monthlyEstimatedRevenue: number = 0;

  constructor(private http: HttpClient) { }
  payments : PaymentGetDto[] = [];

  ngOnInit() {
    if (this.authToken !== ''){
      this.fetchTrainerPayments(this.authToken)?.subscribe(
      (data)=> {
        this.payments = data;
        console.log(this.payments)
      },
      (error) => {
        console.log("deu ruim");
      }
      );

    }
  }

  fetchTrainerPayments(authToken: string){
    const url = `http://localhost:8080/api/payment/list`;
        const headers = new HttpHeaders().set('Authorization', `Bearer ${authToken}`);
        if (authToken !== ''){
          return this.http.get<PaymentGetDto[]>(url, {headers});
        }
        else return null;
  }

  dueDateConverter(dueDate: string){
    return dueDate.substring(0, 11);
  }
}

interface PaymentGetDto{
  paymentId : number;
  price : number;
  dueDate : string;
  paymentType : string;
  customerName : string;
}
