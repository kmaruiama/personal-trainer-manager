import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-customer-blueprint',
  templateUrl: './customer-blueprint.component.html',
  styleUrls: ['./customer-blueprint.component.scss'],
  standalone: true,
  imports: []
})

export class CustomerBlueprintComponent  implements OnInit {

  private customerId: number = 0;
  private customerName: string = "";



  constructor(private router: Router, private http: HttpClient) { }

  ngOnInit() {
    const navigation = this.router.getCurrentNavigation();
    this.customerId = navigation?.extras.state?.['id'] || null;
    this.customerName = navigation?.extras.state?.['name']||null;

    if (this.customerId) {
      const authToken = localStorage.getItem('authToken');
      if (authToken) {
        this.getCurrentProgramBlueprint(this.customerId, authToken).subscribe(

        )
      }
    }
  }

  getCurrentProgramBlueprint(customerId: number, authToken : string){
    const url = `http://localhost:8080/api/workout`;
    const headers = new HttpHeaders().set('Authorization', `Bearer ${authToken}`);
    return this.http.get<string[]>(url, { headers, params: { id: customerId.toString() } });
  }
}

type workout = {
  workoutName: string;
  exercise: [nome: string, sets: number, reps: number]
};
