import { Component, EventEmitter, Input, Output} from '@angular/core';
import { IonCard, IonCardHeader, IonCardSubtitle, IonImg } from '@ionic/angular/standalone';

@Component({
  selector: 'app-shared-schedule-nodes',
  standalone: true,
  templateUrl: './shared-schedule-nodes.component.html',
  styleUrls: ['./shared-schedule-nodes.component.scss'],
  imports:[IonCard, IonCardHeader, IonCardSubtitle, IonImg]
})
export class SharedScheduleNodesComponent{
  @Input() customerName!: string;
  @Input() workoutName!: string;
  @Input() scheduleHour!: string;
  @Input() weekDay!: string;

  @Output() deleteClicked = new EventEmitter<void>();

  deleteSchedule() {
    this.deleteClicked.emit();
  }
}
