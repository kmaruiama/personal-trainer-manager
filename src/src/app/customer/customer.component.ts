import { IonImg, IonContent, IonTitle, IonList, IonItem, IonLabel, IonSearchbar } from '@ionic/angular/standalone';
import { Component, OnInit } from '@angular/core';
import { NgFor } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FilterPipe } from './custompipe';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-customer',
  templateUrl: './customer.component.html',
  styleUrls: ['./customer.component.scss'],
  standalone: true,
  imports: [IonImg, IonContent, IonTitle, IonList, IonItem, IonLabel, NgFor, IonSearchbar, FormsModule, FilterPipe]
})
export class CustomerComponent implements OnInit {

  constructor(private router: Router, private http: HttpClient) {}

  searchTerm: string = "";
  customers: { id: number, name: string }[] = [];

  goToAddNewCustomer() {
    this.router.navigate(['/customer/add']);
  }

  goToCustomerProfile(customerId: number, customerName: string) {
    this.router.navigate(['/customer/profile'], {
      state: { id: customerId, name: customerName}
    });
  }

  ngOnInit() {
    this.fetchCustomers();
  }

  fetchCustomers() {
    const authToken = localStorage.getItem('authToken');

    if (!authToken) {
      this.router.navigate(['login']);
      return;
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${authToken}`
    });

    this.http.get<{ id: number, name: string }[]>('http://localhost:8080/api/customer/list', { headers })
      .subscribe(
        (data) => {
          this.customers = data;
        },
        (error) => {
          console.error('Error fetching customer data', error);
        }
      );
  }
}
