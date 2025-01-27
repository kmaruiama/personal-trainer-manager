import { CommonModule } from '@angular/common';
import { Component, Input} from '@angular/core';
import { IonCard, IonCardSubtitle} from '@ionic/angular/standalone';

@Component({
  selector: 'app-payment-nodes',
  templateUrl: './payment-nodes.component.html',
  styleUrls: ['./payment-nodes.component.scss'],
  imports: [IonCard, IonCardSubtitle, CommonModule],
  standalone: true
})
export class PaymentNodesComponent{
  @Input() customerName!: string;
  @Input() dueDate!: string;
  @Input() price!: number;
  @Input() isPayed! : string;
  constructor() { }
}
