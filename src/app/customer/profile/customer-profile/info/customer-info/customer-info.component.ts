import {
  IonContent,
  IonLabel,
  IonTitle,
  IonInput,
  IonItem,
} from '@ionic/angular/standalone';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-customer-info',
  templateUrl: './customer-info.component.html',
  styleUrls: ['./customer-info.component.scss'],
  standalone: true,
  imports: [
    IonContent,
    IonLabel,
    IonTitle,
    IonInput,
    ReactiveFormsModule,
    IonItem,
  ],
})

export class EditCustomerComponent implements OnInit {
  form: FormGroup;
  customerId: number | null = null;
  authToken: string = localStorage.getItem('authToken') || '';

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.form = this.fb.group({
      nome: ['', [Validators.required]],
      birth: ['', Validators.required],
      cpf: ['', [Validators.required, Validators.pattern(/^\d{3}\.\d{3}\.\d{3}-\d{2}$/)]],
      street: ['', [Validators.required]],
      number: ['', [Validators.required]],
      extras: ['', [Validators.required]],
      neighborhood: ['', [Validators.required]],
      city: ['', [Validators.required]],
    });
  }

  ngOnInit() {
    const navigation = this.router.getCurrentNavigation();
    this.customerId = navigation?.extras.state?.['id'] || null;
    this.fetchCustomerDetails();
  }

  fetchCustomerDetails() {
    const headers = { Authorization: `Bearer ${this.authToken}` };
    this.http
      .get<any>(`http://localhost:8080/api/customer/profile?id=${this.customerId}`, {
        headers,
      })
      .subscribe((data) => {
        const addressParts = data.address.split(',').map((part: string) => part.trim());
        this.form.patchValue({
          nome: data.name,
          birth: new Date(data.birth).toISOString().split('T')[0],
          cpf: data.cpf,
          street: addressParts[0] || '',
          number: addressParts[1] || '',
          neighborhood: addressParts[2] || '',
          city: addressParts[3] || '',
          extras: addressParts[4] || ''
        });
      });
  }
}
