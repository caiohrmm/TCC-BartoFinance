import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { guestGuard } from './core/guards/guest.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/dashboard',
    pathMatch: 'full'
  },
  {
    path: 'login',
    canActivate: [guestGuard],
    loadComponent: () => import('./features/auth/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'register',
    canActivate: [guestGuard],
    loadComponent: () => import('./features/auth/register/register.component').then(m => m.RegisterComponent)
  },
  {
    path: 'dashboard',
    canActivate: [authGuard],
    loadComponent: () => import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent)
  },
  {
    path: 'investidores',
    canActivate: [authGuard],
    loadComponent: () => import('./features/investidores/investidores-list/investidores-list.component').then(m => m.InvestidoresListComponent)
  },
  {
    path: 'investidores/:id',
    canActivate: [authGuard],
    loadComponent: () => import('./features/investidores/investidor-detail/investidor-detail.component').then(m => m.InvestidorDetailComponent)
  },
  {
    path: 'carteiras',
    canActivate: [authGuard],
    loadComponent: () => import('./features/carteiras/carteiras-list/carteiras-list.component').then(m => m.CarteirasListComponent)
  },
  {
    path: 'carteiras/:id',
    canActivate: [authGuard],
    loadComponent: () => import('./features/carteiras/carteira-detail/carteira-detail.component').then(m => m.CarteiraDetailComponent)
  },
  {
    path: '**',
    redirectTo: '/dashboard'
  }
];


