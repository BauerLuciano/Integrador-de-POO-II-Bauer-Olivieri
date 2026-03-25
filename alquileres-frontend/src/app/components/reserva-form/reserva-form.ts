import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { ReservaService } from '../../services/reserva';
import { PropiedadService } from '../../services/propiedad';
import { Propiedad } from '../../models/propiedad';
import { AlertService } from '../../services/alert.service';

@Component({
  selector: 'app-reserva-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './reserva-form.html',
  styleUrl: './reserva-form.css'
})
export class ReservaFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private reservaService = inject(ReservaService);
  private propiedadService = inject(PropiedadService);
  private router = inject(Router);
  private alertService = inject(AlertService);

  fechaHoy: string = this.obtenerFechaLocal();
  
  listaPropiedades: Propiedad[] = [];
  
  obtenerFechaLocal(): string {
    const fecha = new Date();
    const anio = fecha.getFullYear();
    const mes = String(fecha.getMonth() + 1).padStart(2, '0');
    const dia = String(fecha.getDate()).padStart(2, '0');
    return `${anio}-${mes}-${dia}`;
  }
 

  reservaForm = this.fb.group({
    propiedad: [null as Propiedad | null, Validators.required],
    inquilino: ['', [
      Validators.required, 
      Validators.minLength(3), 
      Validators.maxLength(50), 
      Validators.pattern('^[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+$')
    ]],
    fechaInicio: ['', Validators.required],
    fechaFin: ['', Validators.required],
    montoTotal: [0, [Validators.required, Validators.min(1)]],    
    depositoRetenido: [0, Validators.required]                    
  });

  ngOnInit(): void {
    this.propiedadService.getPropiedades().subscribe(data => {
      this.listaPropiedades = data;
    });

    this.reservaForm.valueChanges.subscribe(() => {
      this.calcularMontos();
    });
  }
  
  permitirSoloLetras(event: any, controlName: string) {
    const input = event.target as HTMLInputElement;
    let valorLimpio = input.value.replace(/[^a-zA-ZñÑáéíóúÁÉÍÓÚ\s]/g, ''); 
    this.reservaForm.get(controlName)?.setValue(valorLimpio, { emitEvent: false });
    input.value = valorLimpio;
  }

  calcularMontos(): void {
    const val = this.reservaForm.value;
    const prop = val.propiedad as Propiedad;
    
    if (prop && val.fechaInicio && val.fechaFin) {
      const inicio = new Date(val.fechaInicio);
      const fin = new Date(val.fechaFin);

      const diffTime = fin.getTime() - inicio.getTime();
      const noches = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

      if (noches > 0) {
        const costoEstadia = noches * prop.precioPorNoche;
        const depositoCalculado = costoEstadia * (prop.porcentajeDeposito / 100);
        const totalAPagar = costoEstadia + depositoCalculado;
        this.reservaForm.patchValue({
          montoTotal: totalAPagar,
          depositoRetenido: depositoCalculado
        }, { emitEvent: false });
      } else {
        this.reservaForm.patchValue({ montoTotal: 0, depositoRetenido: 0 }, { emitEvent: false });
      }
    }
  }

  guardar(): void {
    if (this.reservaForm.invalid) {
      this.reservaForm.markAllAsTouched();
      return;
    }

    this.reservaService.crearReserva(this.reservaForm.value as any).subscribe({
      next: () => {
        this.alertService.exito('¡Reserva Confirmada!', 'El calendario ha sido bloqueado con éxito.')
          .then(() => this.router.navigate(['/reservas']));
      },
      error: (err) => {
        console.error('Error al guardar reserva:', err);
        const mensajeError = err.error || 'Hubo un error al procesar la reserva. Revisá los datos.';
        this.alertService.error('Error al reservar', mensajeError);
      }
    });
  }
}