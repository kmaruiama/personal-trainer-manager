import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';

import { EditWorkoutBlueprintComponent } from './edit-workout-blueprint.component';

describe('EditWorkoutBlueprintComponent', () => {
  let component: EditWorkoutBlueprintComponent;
  let fixture: ComponentFixture<EditWorkoutBlueprintComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ EditWorkoutBlueprintComponent ],
      imports: [IonicModule.forRoot()]
    }).compileComponents();

    fixture = TestBed.createComponent(EditWorkoutBlueprintComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
