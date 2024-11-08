import { Routes } from '@angular/router';
import { UserListComponent } from './components/user-list/user-list.component';
import { ProfileComponent } from './components/profile/profile.component';
import { RegisterComponent } from './components/register/register.component';

export const routes: Routes = [
  { path: '', redirectTo: 'users', pathMatch: 'full' }, // Redirige la ruta raíz a 'users'
  { path: 'users', component: UserListComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'register', component: RegisterComponent },
  { path: '**', redirectTo: 'users' } // Redirige cualquier ruta no encontrada a 'users'
];
