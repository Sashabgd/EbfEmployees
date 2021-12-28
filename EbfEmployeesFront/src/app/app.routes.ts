import { EmployeesComponent } from './components/employees/employees.component';
import { ReportsComponent } from './components/reports/reports.component';
import { CompaniesComponent } from './components/companies/companies.component';
import { Component } from '@angular/core';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { Routes } from '@angular/router';
export const appRoutes: Routes = [
  {
    path: '', component: HomeComponent, children: [
      { path: 'companies', component: CompaniesComponent },
      { path: 'reports', component: ReportsComponent },
      { path: 'employees', component: EmployeesComponent }
    ]
  },
  { path: 'login', component: LoginComponent }
]
