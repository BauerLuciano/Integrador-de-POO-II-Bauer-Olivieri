import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReservaService } from '../../services/reserva';
import { Reserva } from '../../models/reserva';

@Component({
  selector: 'app-reservas',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './reservas.html',
  styleUrl: './reservas.css'
})
export class ReservasComponent implements OnInit {
  listaReservas: Reserva[] = [];
  loading: boolean = true;

  private reservaService = inject(ReservaService);

  ngOnInit(): void {
    this.cargarReservas();
  }

  cargarReservas(): void {
    this.loading = true;
    this.reservaService.getUltimasReservas().subscribe({
      next: (data) => {
        this.listaReservas = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error al traer reservas:', err);
        this.loading = false;
      }
    });
  }
}