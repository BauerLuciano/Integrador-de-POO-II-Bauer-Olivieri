import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PropiedadService } from '../../services/propiedad';
import { ReservaService } from '../../services/reserva'; 
import { Propiedad } from '../../models/propiedad';
import { Reserva } from '../../models/reserva'; 
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule,RouterModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class DashboardComponent implements OnInit {
  propiedades: Propiedad[] = [];
  reservas: Reserva[] = []; // <--- Lista dinámica
  totalPropiedades = 0;
  propiedadesDisponibles = 0;
  totalReservasActivas = 0;

  private propiedadService = inject(PropiedadService);
  private reservaService = inject(ReservaService); // <--- Inyectamos

  ngOnInit(): void {
    this.cargarDatos();
  }

  cargarDatos(): void {
    // Cargar Propiedades
    this.propiedadService.getPropiedades().subscribe(data => {
      this.propiedades = data;
      this.totalPropiedades = data.length;
      this.propiedadesDisponibles = data.filter(p => p.estado === 'Disponible').length;
    });

    // Cargar Reservas
    this.reservaService.getUltimasReservas().subscribe(data => {
      this.reservas = data;
      this.totalReservasActivas = data.length; 
    });
  }
}