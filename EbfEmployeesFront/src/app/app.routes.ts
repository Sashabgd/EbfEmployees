import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { Routes } from '@angular/router';
export const appRoutes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent }
]
