import { Component, Input, OnInit } from '@angular/core';
import { IonCard, IonCardHeader, IonCardSubtitle, IonImg } from '@ionic/angular/standalone';

@Component({
  selector: 'app-payment-nodes',
  templateUrl: './payment-nodes.component.html',
  styleUrls: ['./payment-nodes.component.scss'],
  imports: [IonCard, IonCardSubtitle],
  standalone: true
})
export class PaymentNodesComponent{
  @Input() customerName!: string;
  @Input() dueDate!: string;
  @Input() price!: number;
  constructor() { }
}
