import { Component, OnInit, inject } from '@angular/core';
import { CommonModule, CurrencyPipe, DatePipe } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { ReservaService } from '../../../services/reserva';

@Component({
  selector: 'app-reserva-detalle',
  standalone: true,
  imports: [CommonModule, RouterModule, CurrencyPipe, DatePipe],
  templateUrl: './reserva-detalle.html'
})
export class ReservaDetalleComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private reservaService = inject(ReservaService);

  reserva: any = null;
  loading: boolean = true;

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.cargarDetalle(Number(id));
      }
    });
  }

  cargarDetalle(id: number) {
    this.reservaService.getReservaById(id).subscribe({
      next: (data) => {
        this.reserva = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error al cargar la reserva:', err);
        this.loading = false;
        alert('No se pudo cargar el detalle.');
      }
    });
  }

  imprimirTicket() {
    window.print();
  }
}