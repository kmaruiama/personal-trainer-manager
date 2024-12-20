import { Component, Input} from '@angular/core';
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
}