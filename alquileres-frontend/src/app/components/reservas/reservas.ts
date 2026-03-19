import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms'; 
import { ReservaService } from '../../services/reserva';
import { Reserva } from '../../models/reserva';

@Component({
  selector: 'app-reservas',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule], 
  templateUrl: './reservas.html',
  styleUrl: './reservas.css'
})
export class ReservasComponent implements OnInit {

  todasLasReservas: Reserva[] = []; 
  listaReservas: Reserva[] = [];    
  loading: boolean = true;

  filtroTexto: string = '';
  filtroEstado: string = '';

  private reservaService = inject(ReservaService);

  ngOnInit(): void {
    this.cargarReservas();
  }

  cargarReservas(): void {
    this.loading = true;
    this.reservaService.getUltimasReservas().subscribe({
      next: (data) => {
        this.todasLasReservas = data;
        this.listaReservas = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error al traer reservas:', err);
        this.loading = false;
      }
    });
  }

  // LÓGICA DE FILTRADO
  filtrarReservas(): void {
    this.listaReservas = this.todasLasReservas.filter(res => {
      const termino = this.filtroTexto.toLowerCase();
      const coincideTexto = !termino || 
        (res.propiedad?.direccion?.toLowerCase() || '').includes(termino) ||
        (res.inquilino?.toLowerCase() || '').includes(termino) ||
        (res.propiedad?.propietario?.apellido?.toLowerCase() || '').includes(termino);

      const coincideEstado = !this.filtroEstado || res.estado === this.filtroEstado;

      return coincideTexto && coincideEstado;
    });
  }

  cancelarReserva(reserva: any) {
    if (confirm(`¿Estás seguro de que querés cancelar la reserva de ${reserva.inquilino}?`)) {
      
      this.reservaService.cancelarReserva(reserva.id).subscribe({
        next: () => {
          const index = this.todasLasReservas.findIndex(r => r.id === reserva.id);
          if (index !== -1) {
            this.todasLasReservas[index].estado = 'CANCELADA';
            this.filtrarReservas();
          }
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