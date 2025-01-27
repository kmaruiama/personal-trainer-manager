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
  IonMenuToggle,
  IonList,
  IonCheckbox,
  IonButtons,
} from '@ionic/angular/standalone';
import { PaymentNodesComponent } from 'src/app/payment-nodes/payment-nodes/payment-nodes.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-finances-list',
  templateUrl: './finances-list.component.html',
  styleUrls: ['./finances-list.component.scss'],
  standalone: true,
  imports: [
    IonCheckbox,
    IonList,
    IonButton,
    IonHeader,
    IonLabel,
    IonTitle,
    IonToolbar,
    IonContent,
    PaymentNodesComponent,
    CommonModule,
    IonMenu,
    IonItem,
    IonMenuToggle,
  ],
})
export class FinancesListComponent implements OnInit {
  authToken: string = localStorage.getItem('authToken') || '';
  payments: PaymentGetDto[] = [];
  paymentsData: PaymentGetDto[] = [];

  balanceRegularity = {
    payed: false,
    notPayed: false,
    toBePayed: false,
  };

  sortBy = {
    oldest: false,
    newest: false,
  };

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit() {
    if (this.authToken !== '') {
      this.fetchTrainerPayments(this.authToken)?.subscribe(
        (data) => {
          this.payments = data;
          this.paymentsData = data;
          this.checkOverdue(this.payments);
          this.checkOverdue(this.paymentsData);
        },
        (error) => {
          console.log('deu ruim');
        }
      );
    }
  }

  checkOverdue(payments: PaymentGetDto[]) {
    const currentDate = new Date();
    for (let i = 0; i < payments.length; i++) {
      const dueDate = new Date(payments[i].dueDate);
      if (dueDate < currentDate && !payments[i].payed) {
        payments[i].overdue = true;
      } else {
        payments[i].overdue = false;
      }
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

  checkBalanceRegularity(
    option: 'payed' | 'notPayed' | 'toBePayed',
    isChecked: boolean
  ){
    this.balanceRegularity[option] = isChecked;
    this.filterBalance();
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
    this.filterSort();
  }

  checkStatus (payment : PaymentGetDto) : string{
    let paymentStatus : string = '';
    if (payment.payed === true){
      paymentStatus = 'payed';
    }
    if (payment.overdue === true){
      paymentStatus = 'overdue';
    }
    //redundante mas ta aqui pra acelerar o entendimento
    if (payment.overdue === false && payment.payed === false)
    {
      paymentStatus = 'notPayed';
    }
    return paymentStatus;
  }

  filterSort() {
    if (this.sortBy.oldest) {
      this.payments.sort(
        (a, b) => new Date(a.dueDate).getTime() - new Date(b.dueDate).getTime()
      );
    } else if (this.sortBy.newest) {
      this.payments.sort(
        (a, b) => new Date(b.dueDate).getTime() - new Date(a.dueDate).getTime()
      );
    }
  }

  filterBalance() {
    if (
      //nenhum filtro foi clicado
      !this.balanceRegularity.payed &&
      !this.balanceRegularity.notPayed &&
      !this.balanceRegularity.toBePayed
    ) {
      this.payments = [...this.paymentsData];
    } else {
      //algum filtro (ou multiplos) foram selecionados
      this.payments = this.paymentsData.filter((payment) => {
        const notPayed = !payment.payed;
        const overdue = payment.overdue;
        //foi pago
        if (this.balanceRegularity.payed === true && payment.payed === true){
          return true
        }
        //está atrasado
        if(this.balanceRegularity.notPayed === true && payment.overdue === true){
          return true;
        }
        //nao foi pago mas nao ta atrasado
        if(this.balanceRegularity.toBePayed === true && payment.payed === false && payment.overdue === false){
          return true;
        }
        return false;
      });
    }
  }
}

interface PaymentGetDto {
  paymentId: number;
  price: number;
  dueDate: string;
  paymentType: string;
  customerName: string;
  payed: boolean;
  overdue: boolean;
}
