import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output} from '@angular/core';
import { IonCard, IonCardSubtitle, IonButton } from '@ionic/angular/standalone';

@Component({
  selector: 'app-payment-nodes',
  templateUrl: './payment-nodes.component.html',
  styleUrls: ['./payment-nodes.component.scss'],
  imports: [IonButton, IonCard, IonCardSubtitle, CommonModule],
  standalone: true
})
export class PaymentNodesComponent{
  @Input() customerName!: string;
  @Input() dueDate!: string;
  @Input() price!: number;
  @Input() isPayed! : string;

  @Output() confirmPaymentClicked = new EventEmitter<void>();

  @Output() editPaymentClicked = new EventEmitter<void>();

  constructor() { }

  confirmPayment(){
    this.confirmPaymentClicked.emit();
  }
  editPayment(){
    this.editPaymentClicked.emit();
  }

}
