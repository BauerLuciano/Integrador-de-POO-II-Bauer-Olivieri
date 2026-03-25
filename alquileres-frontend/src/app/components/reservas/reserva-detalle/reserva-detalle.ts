import { Component, OnInit, inject } from '@angular/core';
import { CommonModule, CurrencyPipe, DatePipe } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ReservaService } from '../../../services/reserva';
import { AlertService } from '../../../services/alert.service';

@Component({
  selector: 'app-reserva-detalle',
  standalone: true,
  imports: [CommonModule, RouterModule, CurrencyPipe, DatePipe, ReactiveFormsModule],
  templateUrl: './reserva-detalle.html'
})
export class ReservaDetalleComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private reservaService = inject(ReservaService);
  private fb = inject(FormBuilder);
  private alertService = inject(AlertService); 

  reserva: any = null;
  saldoPendiente: number = 0;
  loading: boolean = true;
  mostrarModalPago: boolean = false;
  
  pagoForm = this.fb.group({
    tipo: ['SALDO', Validators.required],
    monto: [0, [Validators.required, Validators.min(1)]],
    fechaPago: [{ value: new Date().toISOString().substring(0, 10), disabled: true }, Validators.required]
  });

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
        this.reservaService.getSaldoPendiente(id).subscribe({
          next: (saldo) => {
            this.saldoPendiente = saldo;
            this.loading = false;
          }
        });
      },
      error: (err) => {
        console.error('Error al cargar la reserva:', err);
        this.loading = false;
        this.alertService.error('Error', 'No se pudo cargar el detalle.');
      }
    });
  }

  abrirModalPago() {
    this.mostrarModalPago = true;
    const tipoSugerido = this.saldoPendiente === this.reserva.montoTotal ? 'SENIA' : 'SALDO';

    this.pagoForm.reset({
      tipo: tipoSugerido,
      monto: this.saldoPendiente,
      fechaPago: new Date().toISOString().substring(0, 10)
    });
  }

  cerrarModalPago() {
    this.mostrarModalPago = false;
  }

  guardarPago() {
    if (this.pagoForm.invalid || !this.reserva) return;
    const datosPago = this.pagoForm.getRawValue();
    const montoARegistrar = datosPago.monto ?? 0;

    if (montoARegistrar > this.saldoPendiente) {
      this.alertService.notificacion('Monto excedido', `El monto ingresado ($${montoARegistrar}) supera la deuda actual.`, 'warning');
      return;
    }

    this.reservaService.registrarPago(this.reserva.id, {
      ...datosPago,
      monto: montoARegistrar 
    }).subscribe({
      next: () => {
        this.alertService.exito('¡Pago registrado!', 'El dinero ingresó a la caja correctamente.');
        this.cerrarModalPago();
        this.loading = true;
        this.cargarDetalle(this.reserva.id);
      },
      error: (err) => {
        this.alertService.error('Error al pagar', err.error || 'No se pudo registrar el pago.');
      }
    });
  }

  get montoExcedeDeuda(): boolean {
    const monto = this.pagoForm.get('monto')?.value ?? 0;
    return monto > this.saldoPendiente;
  }

  imprimirTicket() {
    window.print();
  }

  finalizarReserva() {
    this.alertService.confirmar(
      '¿Registrar Check-Out?', 
      'La reserva pasará a "Finalizada" y la propiedad quedará "Disponible" para nuevos alquileres.', 
      'Sí, finalizar'
    ).then((result) => {
      if (result.isConfirmed) {
        this.reservaService.finalizarReserva(this.reserva.id).subscribe({
          next: () => {
            this.reserva.estado = 'FINALIZADA';
            this.alertService.exito('¡Check-Out Exitoso!', 'La reserva se cerró y la propiedad fue liberada.');
          },
          error: (err) => {
            console.error('Error al finalizar:', err);
            this.alertService.error('Error', 'No se pudo finalizar la reserva.');
          }
        });
      }
    });
  }
}