import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpHeaders } from '@angular/common/http';
import {
  IonContent,
  IonToolbar,
  IonTitle,
  IonRadioGroup,
  IonLabel,
  IonHeader,
  IonMenu,
  IonItem,
  IonRadio,
  IonButton,
  IonMenuToggle, IonList, IonCheckbox } from '@ionic/angular/standalone';
import { PaymentNodesComponent } from 'src/app/payment-nodes/payment-nodes/payment-nodes.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-finances-list',
  templateUrl: './finances-list.component.html',
  styleUrls: ['./finances-list.component.scss'],
  standalone: true,
  imports: [IonCheckbox, IonList, IonButton,
    IonHeader,
    IonLabel,
    IonRadioGroup,
    IonTitle,
    IonToolbar,
    IonContent,
    PaymentNodesComponent,
    CommonModule,
    IonRadioGroup,
    IonMenu,
    IonItem,
    IonRadio,
    IonMenuToggle
  ],
})
export class FinancesListComponent implements OnInit {
  authToken: string = localStorage.getItem('authToken') || '';
  payments: PaymentGetDto[] = [];

  balanceRegularity = {
    payed: false,
    notPayed: false,
    toBePayed: false,
  };

  sortBy = {
    oldest: false,
    newest: false
  }

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit() {
    if (this.authToken !== '') {
      this.fetchTrainerPayments(this.authToken)?.subscribe(
        (data) => {
          this.payments = data;
        },
        (error) => {
          console.log('deu ruim');
        }
      );
    }
  }

  fetchTrainerPayments(authToken: string) {
    const url = `http://localhost:8080/api/payment/list`;
    const headers = new HttpHeaders().set(
      'Authorization',
      `Bearer ${authToken}`
    );
    if (authToken !== '') {
      return this.http.get<PaymentGetDto[]>(url, { headers });
    } else return null;
  }

  dueDateConverter(dueDate: string): string {
    const date = new Date(dueDate);
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
  }

  checkBalanceRegularity(option: 'payed' | 'notPayed' | 'toBePayed', isChecked: boolean) {
    this.balanceRegularity[option] = isChecked;
    console.log('Balance Regularity:', this.balanceRegularity);
  }


  checkSortBy(option: 'oldest' | 'newest', isChecked: boolean) {
    if (option === 'oldest') {
      this.sortBy.oldest = isChecked;
    }
    if (option === 'newest') {
      this.sortBy.newest = isChecked;
    }

    if (option === 'oldest' && isChecked) {
      this.sortBy.newest = false;
    }
    if (option === 'newest' && isChecked) {
      this.sortBy.oldest = false;
    }

    console.log(this.sortBy);
  }


}

interface PaymentGetDto {
  paymentId: number;
  price: number;
  dueDate: string;
  paymentType: string;
  customerName: string;
  payed: boolean;
}
