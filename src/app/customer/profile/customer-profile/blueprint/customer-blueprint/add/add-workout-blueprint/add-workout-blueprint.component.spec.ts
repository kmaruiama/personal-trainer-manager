import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';

import { AddWorkoutBlueprintComponent } from './add-workout-blueprint.component';

describe('AddWorkoutBlueprintComponent', () => {
  let component: AddWorkoutBlueprintComponent;
  let fixture: ComponentFixture<AddWorkoutBlueprintComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ AddWorkoutBlueprintComponent ],
      imports: [IonicModule.forRoot()]
    }).compileComponents();

    fixture = TestBed.createComponent(AddWorkoutBlueprintComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
