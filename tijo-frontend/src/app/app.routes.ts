import { Routes } from '@angular/router';
import { CarsPage } from './pages/cars.page';
import { OwnersPage } from './pages/owners.page';

export const routes: Routes = [
  { path: '', redirectTo: 'cars', pathMatch: 'full' },
  { path: 'cars', component: CarsPage },
  { path: 'owners', component: OwnersPage }
];
