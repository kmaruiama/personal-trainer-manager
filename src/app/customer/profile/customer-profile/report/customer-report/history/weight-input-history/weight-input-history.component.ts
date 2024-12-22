import { IonLabel, IonContent, IonItem, IonInput, IonButton, IonTitle } from '@ionic/angular/standalone';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { AlertController } from '@ionic/angular';

@Component({
  selector: 'app-weight-input-history',
  templateUrl: './weight-input-history.component.html',
  styleUrls: ['./weight-input-history.component.scss'],
  standalone: true,
  imports: [IonTitle, IonContent, IonLabel, IonItem, IonInput, IonButton, CommonModule]
})
export class WeightInputHistoryComponent implements OnInit {
  customerId: number = 0;
  customerData: { date: string, weight: number, bodyFatPercentage: number, id: number, height: number }[] = [];

  constructor(
    private router: Router,
    private http: HttpClient,
    private alertController: AlertController
  ) {
  }

  ngOnInit() {
    const navigation = this.router.getCurrentNavigation();
    this.customerId = navigation?.extras.state?.['id'] || 0;

    if (this.customerId !== 0) {
      this.populateComponent(this.customerId);
    } else {
      this.router.navigate(['customer/profile/report']);
    }
  }

  populateComponent(customerId: number) {
    const authToken = localStorage.getItem('authToken');

    if (authToken) {
      const headers = { Authorization: `Bearer ${authToken}` };

      this.http.get<{ date: string, weight: number, bodyFatPercentage: number, id: number, height: number }[]>(`http://localhost:8080/api/weight/list?customerId=${customerId}`, { headers })
        .subscribe({
          next: (data) => {
            this.customerData = data;
          },
          error: (error) => {
            this.showErrorAlert("Erro ao retornar as informações");
          }
        });
    }
  }

  /*nao deu pra pegar pelo formulario por causa da diretiva ngfor
   tentei varias maneiras mas essa é a mais simples que funciona*/
  onSubmit(id: number, bodyFatPercentageAny: any, weightAny: any, event: Event): void {
    event.stopPropagation;
    const record = this.customerData.find(data => data.id === id);

    const bodyFatPercentage = Number(bodyFatPercentageAny);
    const weight = Number(weightAny);

    if (isNaN(bodyFatPercentage) || isNaN(weight)) {
      this.showErrorAlert('Valores inválidos');
      return;
    }

    if (record) {
      const payload = {
        weightId: record.id,
        date: record.date,
        weight: weight,
        bodyFatPercentage: bodyFatPercentage,
        height: record.height,
      };

      const authToken = localStorage.getItem('authToken');
      const headers = { Authorization: `Bearer ${authToken}` };

      this.http.patch(`http://localhost:8080/api/weight/edit`, payload, { headers }).subscribe({
        next: () => {
          this.showSuccessAlert('Edição enviada com sucesso!');
          this.populateComponent(this.customerId);
        },
        error: () => {
          this.showErrorAlert('Erro ao atualizar histórico de composição corporal');
        },
      });
    } else {
      this.showErrorAlert('Registro não encontrado para atualização');
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
