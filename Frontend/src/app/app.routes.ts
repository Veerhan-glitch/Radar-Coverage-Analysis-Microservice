import { Routes } from '@angular/router';
import { Auth } from '../pages/auth/auth';
import { Register } from '../pages/register/register';
import { Dashboard } from '../pages/dashboard/dashboard';
import { Index } from '../pages/index/index';

export const routes: Routes = [
  { path: '', component: Index },
  { path: 'auth', component: Auth },
  { path: 'register', component: Register },
  { path: 'dashboard', component: Dashboard }
];
