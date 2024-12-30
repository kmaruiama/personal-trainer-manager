import { Component, OnInit } from '@angular/core';
import { IonContent, IonButton, IonLabel, IonItem, IonInput } from '@ionic/angular/standalone';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import {HttpClient, HttpClientModule} from '@angular/common/http'
import { catchError, map } from 'rxjs/operators';

interface LoginResponse {
  accessToken: string;

}

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  standalone: true,
  imports: [IonContent, IonInput, IonButton, IonLabel, IonItem, ReactiveFormsModule, HttpClientModule]
})

export class LoginComponent{
  form: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private httpClient: HttpClient,
    private router: Router
  ) {
    this.form = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

  postLogin() {
    if (this.form.valid) {
      const credentials = this.form.value;


      this.httpClient.post<LoginResponse>('http://localhost:8080/api/auth/login', credentials)
        .pipe(
          map((response) => {
            //no futuro trocar isso por estratégias mais seguras
            localStorage.setItem('authToken', response.accessToken);
            this.router.navigate(['menu']);
            return response;
          }),
          catchError((error) => {
            console.error('Login error:', error);
            alert('Login failed. Please check your credentials or try again later.');
            return error;
          })
        )
        .subscribe();
    } else {
      console.log('Form is not valid');
    }
  }
}
