import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { ManagerDashboardComponent } from './manager/manager-dashboard/manager-dashboard.component';
import { ClerkDashboardComponent } from './clerk/clerk-dashboard/clerk-dashboard.component';
import { roleGuard } from './core/guards/role.guard';

export const routes: Routes = [
  { path: '', component: LoginComponent },

  {
    path: 'manager',
    component: ManagerDashboardComponent,
    canActivate: [roleGuard],
    data: { role: 'MANAGER' }
  },

  {
    path: 'clerk',
    component: ClerkDashboardComponent,
    canActivate: [roleGuard],
    data: { role: 'CLERK' }
  },

  { path: '**', redirectTo: '' }
];
