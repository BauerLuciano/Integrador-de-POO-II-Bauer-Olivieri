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

  cancelarReserva(reserva: any) {
    if (confirm(`¿Estás seguro de que querés cancelar la reserva de ${reserva.inquilino}?`)) {
      
      this.reservaService.cancelarReserva(reserva.id).subscribe({
        next: () => {
          reserva.estado = 'CANCELADA';
          alert('¡Reserva dada de baja exitosamente!');
        },
        error: (err) => {
          console.error('Error al cancelar:', err);
          alert('Hubo un error. No se pudo cancelar la reserva.');
        }
      });
    }
  }
}