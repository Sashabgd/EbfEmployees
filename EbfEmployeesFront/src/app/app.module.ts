import { appRoutes } from './app.routes';
import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { HttpInterceptorService } from './services/http-interceptor.service';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatDialogModule } from '@angular/material/dialog'
import { MatNativeDateModule } from '@angular/material/core';
import { MatSidenavModule } from '@angular/material/sidenav';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatButtonModule } from '@angular/material/button';
import { CompaniesComponent } from './components/companies/companies.component';
import { ReportsComponent } from './components/reports/reports.component';
import { EmployeesComponent } from './components/employees/employees.component';
import { LoginDialogComponent } from './components/login-dialog/login-dialog.component';
import { MatCardModule } from '@angular/material/card'
import { FormsModule, ReactiveFormsModule } from '@angular/forms'
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input'
import { MatSnackBarModule } from '@angular/material/snack-bar'
import { MatTableModule } from '@angular/material/table'
import { MatIconModule } from '@angular/material/icon';
import { CompanyDetailsComponent } from './components/company-details/company-details.component';
import { CreateCompanyDialogComponent } from './components/dialogs/create-company-dialog/create-company-dialog.component';
import { CreateEmployeeDialogComponent } from './components/dialogs/create-employee-dialog/create-employee-dialog.component';
import { EmployeeDetailsComponent } from './components/employee-details/employee-details.component'
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    CompaniesComponent,
    ReportsComponent,
    EmployeesComponent,
    LoginDialogComponent,
    CompanyDetailsComponent,
    CreateCompanyDialogComponent,
    CreateEmployeeDialogComponent,
    EmployeeDetailsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    RouterModule.forRoot(appRoutes),
    HttpClientModule,
    MatToolbarModule,
    MatNativeDateModule,
    MatSidenavModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatDialogModule,
    MatCardModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSnackBarModule,
    MatTableModule,
    MatIconModule,
    MatPaginatorModule,
    MatSortModule
  ],
  providers: [{ provide: HTTP_INTERCEPTORS, useClass: HttpInterceptorService, multi: true }],
  bootstrap: [AppComponent],
  entryComponents: [
    LoginDialogComponent,
    CreateCompanyDialogComponent,
    CreateEmployeeDialogComponent
  ]
})
export class AppModule { }
