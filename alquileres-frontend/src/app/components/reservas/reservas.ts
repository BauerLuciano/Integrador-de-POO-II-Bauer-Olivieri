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

  reservaACancelar: any = null;
  motivoSeleccionado: string = '';
  detalleCancelacion: string = '';

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

  abrirModalCancelacion(reserva: any) {
    this.reservaACancelar = reserva;
    this.motivoSeleccionado = '';
    this.detalleCancelacion = '';
    
    const modal = new (window as any).bootstrap.Modal(document.getElementById('modalCancelacion'));
    modal.show();
  }

  confirmarCancelacion() {
    if (!this.reservaACancelar || !this.motivoSeleccionado) return;

    this.reservaService.cancelarReserva(this.reservaACancelar.id, this.motivoSeleccionado, this.detalleCancelacion).subscribe({
      next: () => {
        const modalElement = document.getElementById('modalCancelacion');
        const modalInstance = (window as any).bootstrap.Modal.getInstance(modalElement);
        if (modalInstance) modalInstance.hide();

        const index = this.todasLasReservas.findIndex(r => r.id === this.reservaACancelar.id);
        if (index !== -1) {
          this.todasLasReservas[index].estado = 'CANCELADA';
          this.filtrarReservas();
        }
        alert(`¡Reserva cancelada exitosamente!\nMotivo: ${this.motivoSeleccionado}`);
      },
      error: (err) => {
        console.error('Error al cancelar:', err);
        alert("No se pudo cancelar: " + (err.error?.message || err.message));
      }
    });
  }
}