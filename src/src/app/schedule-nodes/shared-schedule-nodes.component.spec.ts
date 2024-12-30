import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';

import { SharedScheduleNodesComponent } from './shared-schedule-nodes.component';

describe('SharedScheduleNodesComponent', () => {
  let component: SharedScheduleNodesComponent;
  let fixture: ComponentFixture<SharedScheduleNodesComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ SharedScheduleNodesComponent ],
      imports: [IonicModule.forRoot()]
    }).compileComponents();

    fixture = TestBed.createComponent(SharedScheduleNodesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
