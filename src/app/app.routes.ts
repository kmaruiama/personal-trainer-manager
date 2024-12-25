import { Routes } from '@angular/router';
import { ReportUpdateComponent } from './customer/profile/customer-profile/report/customer-report/update/report-update/report-update.component';

export const routes: Routes = [
  {
    path: 'home',
    loadComponent: () => import('./home/home.page').then((m) => m.HomePage),
  },
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full',
  },

  {
    path: 'login',
    loadComponent: () => import('./login/login.component').then((m) => m.LoginComponent),
  },

  {
    path: 'register',
    loadComponent: () => import('./register/register.component').then((m) => m.RegisterComponent),
  },

  {
    path: 'menu',
    loadComponent: () => import('./menu-principal/menu-principal.component').then((m) => m.MenuPrincipalComponent)
  },

  {
    path: 'schedule',
    loadComponent: () => import('./schedule/schedule.component').then((m) => m.ScheduleComponent)
  },

  {
    path: 'customer',
    loadComponent: () => import('./customer/customer.component').then((m) => m.CustomerComponent)
  },


  { path: 'customer/profile',
    loadComponent: () => import('./customer/profile/customer-profile/customer-profile.component').then((m) => m.CustomerProfileComponent)
  },

  {
    path: 'customer/edit',
    loadComponent: ()=> import('./customer/profile/customer-profile/edit/customer-edit/edit-customer.component').then((m)=>m.EditCustomerComponent)
  },

  {
    path: 'customer/add',
    loadComponent:()=> import('./customer/add/customer-add/customer-add.component').then((m)=>m.CustomerAddComponent)
  },

  {
    path: 'customer/info',
    loadComponent: ()=> import('./customer/profile/customer-profile/info/customer-info/customer-info.component').then((m)=>m.EditCustomerComponent)
  },

  {
    path: 'customer/report',
    loadComponent: ()=> import('./customer/profile/customer-profile/report/customer-report/customer-report.component').then((m)=>m.CustomerReportComponent)
  },

  {
    path: 'customer/report/update',
    loadComponent: ()=> import('./customer/profile/customer-profile/report/customer-report/update/report-update/report-update.component').then((m)=>ReportUpdateComponent)
  },

  {
    path:'customer/report/history',
    loadComponent: ()=> import('./customer/profile/customer-profile/report/customer-report/history/weight-input-history/weight-input-history.component').then((m)=>m.WeightInputHistoryComponent)
  },

  {
    path: 'customer/report/chart',
    loadComponent: ()=> import('./customer/profile/customer-profile/report/customer-report/chart/report-chart/report-chart.component').then((m)=>m.ReportChartComponent)
  }
];
