import { Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard';
import { PropiedadesComponent } from './components/propiedades/propiedades';
import { PropietariosComponent } from './components/propietarios/propietarios';
import { PropiedadFormComponent } from './components/propiedad-form/propiedad-form';
import { PropietarioFormComponent } from './components/propietario-form/propietario-form';
import { ReservasComponent } from './components/reservas/reservas';
import { ReportesComponent } from './components/reportes/reportes';
import { ReservaFormComponent } from './components/reserva-form/reserva-form';
import { PropiedadDetalleComponent } from './components/propiedades/propiedad-detalle/propiedad-detalle';



export const routes: Routes = [
  { path: 'dashboard', component: DashboardComponent },
  { path: 'propiedades', component: PropiedadesComponent },
  { path: 'propiedades/nueva', component: PropiedadFormComponent },
  { path: 'propietarios', component: PropietariosComponent },
  { path: 'propietarios/nuevo', component: PropietarioFormComponent },
  { path: 'reservas', component: ReservasComponent },
  { path: 'reservas/nueva', component: ReservaFormComponent },
  { path: 'reportes', component: ReportesComponent },
  { path: 'propiedades/editar/:id', component: PropiedadFormComponent },
  { path: 'propiedades/detalle/:id', component: PropiedadDetalleComponent },
  { path: 'propietarios/editar/:id', component: PropietarioFormComponent },
  
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: '**', redirectTo: 'dashboard' } 
];