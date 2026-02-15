import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';

export const roleGuard: CanActivateFn = (route) => {

  const router = inject(Router);
  const expectedRole = route.data?.['role'];
  const userRole = localStorage.getItem('role');

  if (userRole === expectedRole) {
    return true;
  }

  router.navigate(['/']);
  return false;
};
