import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { AlertController } from '@ionic/angular';
import { HttpClient } from '@angular/common/http';
import { catchError, map, throwError } from 'rxjs';
import { IonContent, IonButton, IonLabel, IonItem, IonInput, IonRadio, IonRadioGroup, IonTitle } from '@ionic/angular/standalone';

@Component({
  selector: 'app-customer-add',
  templateUrl: './customer-add.component.html',
  styleUrls: ['./customer-add.component.scss'],
  standalone: true,
  imports: [
    IonContent,
    IonInput,
    IonButton,
    IonLabel,
    IonItem,
    IonRadio,
    IonTitle,
    IonRadioGroup,
    ReactiveFormsModule,
  ],
})
export class CustomerAddComponent {
  form: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private httpClient: HttpClient,
    private alertController: AlertController
  ) {
    this.form = this.formBuilder.group({
      name: ['', [Validators.required]],
      birth: ['', [Validators.required]],
      cpf: ['', [Validators.required, Validators.pattern(/^\d{3}\.\d{3}\.\d{3}-\d{2}$/)]],
      street: ['', [Validators.required]],
      number: ['', [Validators.required]],
      neighborhood: ['', [Validators.required]],
      city: ['', [Validators.required]],
      extras: [''],
      price: ['', [Validators.required]],
      typeOfPayment: ['', [Validators.required]],
      address: [''],
    });
  }

  postCustomer() {
    if (this.form.valid) {
      console.log(this.form);
      this.concatenateAddress();
      const customerData = this.preparePayload();

      const authToken = localStorage.getItem('authToken');
      const headers = {
        'Authorization': `Bearer ${authToken}`,
      };

      this.httpClient
        .post('http://localhost:8080/api/customer/new', customerData, { headers })
        .pipe(
          map((response) => response),
          catchError((error) => {
            this.showErrorAlert('Erro ao enviar os dados do cliente!');
            return throwError(() => error);
          })
        )
        .subscribe({
          next: (response) => {
            this.showSuccessAlert('Cliente cadastrado com sucesso!');
          },
        });
    } else {
      this.showErrorAlert('Preencha todos os campos corretamente!');
    }
  }

  concatenateAddress() {
    const formValues = this.form.value;
    const fullAddress = `${formValues.street}, ${formValues.number}, ${formValues.neighborhood}, ${formValues.city}, ${formValues.extras}`;
    this.form.patchValue({
      address: fullAddress,
    });
  }

  preparePayload() {
    const formValues = this.form.value;
    return {
      name: formValues.name,
      cpf: formValues.cpf,
      address: formValues.address,
      birth: formValues.birth,
      price: formValues.price,
      typeOfPayment: formValues.typeOfPayment
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

  async showSuccessAlert(message: string) {
    const alert = await this.alertController.create({
      header: 'Sucesso!',
      message: message,
      buttons: ['OK'],
    });

    await alert.present();
  }
}
