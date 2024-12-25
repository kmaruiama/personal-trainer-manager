import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { Chart } from 'chart.js';

@Component({
  selector: 'app-report-chart',
  templateUrl: './report-chart.component.html',
  styleUrls: ['./report-chart.component.scss'],
  standalone: true
})
export class ReportChartComponent implements OnInit {
  @ViewChild('lineCanvas') lineCanvas!: ElementRef;

  lineChart: Chart | undefined;
  customerId: number = 0;

  constructor(private router: Router) {}

  ngOnInit() {
    const navigation = this.router.getCurrentNavigation();
    this.customerId = navigation?.extras.state?.['id'] || 0;
    // Aguarda a inicialização do ViewChild
    setTimeout(() => this.createLineChart(), 0);
  }

  private createLineChart() {
    this.lineChart = new Chart(this.lineCanvas.nativeElement, {
      type: 'line',
      data: {
        labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
        datasets: [
          {
            label: 'My First Dataset',
            data: [65, 59, 80, 81, 56, 55, 40],
            fill: false,
            borderColor: 'rgba(75,192,192,1)',
            backgroundColor: 'rgba(75,192,192,0.4)',
            tension: 0.1,
          },
        ],
      },
    });
  }
}
