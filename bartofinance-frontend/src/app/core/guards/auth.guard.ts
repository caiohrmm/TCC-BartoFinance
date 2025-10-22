import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const isAuth = authService.isAuthenticated();
  const hasToken = authService.getToken();
  
  console.log('AuthGuard check:', { isAuth, hasToken });

  if (isAuth && hasToken) {
    return true;
  }

  // Redireciona para login se não autenticado
  console.log('AuthGuard: redirecting to login');
  router.navigate(['/login']);
  return false;
};


