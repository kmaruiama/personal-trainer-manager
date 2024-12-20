import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-mass-chart',
  templateUrl: './mass-chart.component.html',
  styleUrls: ['./mass-chart.component.scss'],
  standalone:true
})
export class MassChartComponent  implements OnInit {
  customerId: number = 0;
  customerData: {date: string, weight: number, bodyFatPercentage: number}[] = [];
  constructor(private router: Router,
              private http: HttpClient
  ) { }

  ngOnInit() {
    const navigation = this.router.getCurrentNavigation();
    this.customerId = navigation?.extras.state?.['id'] || null;

    if (this.customerId != 0){
      this.populatePlot(this.customerId);
    }
    else{
      this.router.navigate(['customer/profile/report']);
    }
  }

  populatePlot(customerId: number) {
    const authToken = localStorage.getItem('authToken');

    if (authToken) {
      const headers = { Authorization: `Bearer ${authToken}` };

      this.http.get<{ date: string, weight: number, bodyFatPercentage: number }[]>(`http://localhost:8080/api/weight/list?customerId=${customerId}`, { headers })
      .subscribe({
        next: (data) => {
          this.customerData = data;
          console.log(this.customerData);
        }
      });
    }
  }
}
