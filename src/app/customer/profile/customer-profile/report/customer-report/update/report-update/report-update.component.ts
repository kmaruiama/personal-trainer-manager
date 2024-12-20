import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { IonTitle, IonContent, IonInput, IonCard, IonRadioGroup, IonItem, IonRadio, IonLabel, IonButton } from "@ionic/angular/standalone";
import { Router } from '@angular/router';

@Component({
  selector: 'app-report-update',
  templateUrl: './report-update.component.html',
  styleUrls: ['./report-update.component.scss'],
  standalone: true,
  imports: [
    ReactiveFormsModule,
    HttpClientModule,
    IonLabel,
    IonRadio,
    IonItem,
    IonRadioGroup,
    IonCard,
    IonInput,
    IonTitle,
    IonContent,
    CommonModule
  ]
})
export class ReportUpdateComponent implements OnInit
{
  formGroup: FormGroup;
  customerId: number = 0;

  constructor(private fb: FormBuilder, private http: HttpClient, private router:Router) {
    this.formGroup = this.fb.group({
      peso: ['', [Validators.required, Validators.min(0)]],
      percentualGordura: ['', [Validators.required, Validators.min(0), Validators.max(100)]],
      dataOpcao: ['atual', Validators.required],
      height:['',[Validators.required, Validators.min(1.20), Validators.max(2.20)]],
      data: [''],
    });

    this.formGroup.get('dataOpcao')?.valueChanges.subscribe((value) => {
      if (value === 'date') {
        this.formGroup.get('data')?.setValidators([Validators.required]);
      } else {
        this.formGroup.get('data')?.clearValidators();
      }
      this.formGroup.get('data')?.updateValueAndValidity();
    });
  }

  onSubmit() {
    if (this.formGroup.valid) {
      const payload = {
        customerId: this.customerId,
        date: this.formGroup.value.dataOpcao === 'atual' ? null : this.formGroup.value.data,
        bodyFatPercentage: this.formGroup.value.percentualGordura,
        weight: this.formGroup.value.peso,
        height: this.formGroup.value.height
      };

      const authToken = localStorage.getItem('authToken');

      if (authToken) {
        const headers = {
          'Authorization': `Bearer ${authToken}`,
        };

        this.http.post('http://localhost:8080/api/weight/new', payload, { headers })
          .subscribe({
            next: (response) => {
              console.log('Response:', response);
              alert('Dados enviados com sucesso!');
            },
            error: (error) => {
              console.error('Error:', error);
              alert('Erro ao enviar os dados. Tente novamente.');
            }
          });
      } else {
        alert('Token de autorização não encontrado!');
      }
    }
  }


  ngOnInit(){
    const navigation = this.router.getCurrentNavigation();
    this.customerId = navigation?.extras.state?.['id'] || null;
  }
}
