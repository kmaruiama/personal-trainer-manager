import { Component, OnInit } from '@angular/core';
import { IonContent, IonButton, IonLabel, IonItem, IonInput, IonAlert, IonTitle } from '@ionic/angular/standalone';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { AlertController } from '@ionic/angular';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { catchError, map, throwError } from 'rxjs';

interface RegisterResponse {
  accessToken: string;
}

@Component({
  selector: 'app-login',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
  standalone: true,
  imports: [IonContent, IonInput, IonButton, IonLabel, IonItem, ReactiveFormsModule, HttpClientModule, IonTitle],
})
export class RegisterComponent {
  form: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private httpClient: HttpClient,
    private alertController: AlertController
  ) {
    this.form = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required],
      email: [' ', Validators.required],
      name: [' ', Validators.required],
      cpf: [' ', Validators.required],
      birth: [' ', Validators.required],
      street: ['', [Validators.required]],
      number: ['', [Validators.required]],
      neighborhood: ['', [Validators.required]],
      city: ['', [Validators.required]],
      address: [''],
    });
  }

  postRegister() {
    const formValues = this.form.value;

    if (!(formValues.password === formValues.confirmPassword)) {
      this.form.invalid;
    }

    if (this.form.valid) {
      this.concatenateAddress();
      const credentials = this.form.value;

      this.httpClient
        .post<RegisterResponse>('http://localhost:8080/api/auth/register', credentials)
        .pipe(
          map((response) => response),
          catchError((error) => {
            this.showErrorAlert('Erro ao se registrar!');
            return throwError(() => error);
          })
        )
        .subscribe({
          next: (response) => {
            this.showSuccessAlert('Registro realizado com sucesso!');
          }
        });
    } else {
      this.showErrorAlert('Preencha todos os campos para se registrar!');
    }
  }

  concatenateAddress() {
    const formValues = this.form.value;
    const concatenateAddress = `${formValues.street}, ${formValues.number}, ${formValues.neighborhood}, ${formValues.city}`;
    this.form.patchValue({
      address: concatenateAddress,
    });
  }

  async showErrorAlert(message: string) {
    const alert = await this.alertController.create({
      header: 'Erro',
      message: message,
      buttons: ['OK']
    });

    await alert.present();
  }

  async showSuccessAlert(message: string) {
    const alert = await this.alertController.create({
      header: 'Sucesso!',
      message: message,
      buttons: ['OK']
    });

    await alert.present();
  }
}
