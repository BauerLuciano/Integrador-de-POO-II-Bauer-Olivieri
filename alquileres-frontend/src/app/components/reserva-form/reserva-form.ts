import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { ReservaService } from '../../services/reserva';
import { PropiedadService } from '../../services/propiedad';
import { Propiedad } from '../../models/propiedad';

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

  listaPropiedades: Propiedad[] = [];

  reservaForm = this.fb.group({
    propiedad: [null as Propiedad | null, Validators.required],
    inquilino: ['', [Validators.required, Validators.minLength(3)]],
    fechaInicio: ['', Validators.required],
    fechaFin: ['', Validators.required],
    montoTotal: [0, [Validators.required, Validators.min(1)]],    
    depositoRetenido: [0, Validators.required]                    
  });

  ngOnInit(): void {
    // 1. Cargamos las propiedades para el select
    this.propiedadService.getPropiedades().subscribe(data => {
      this.listaPropiedades = data;
    });

    // 2. Escuchamos cambios en el formulario para calcular en tiempo real
    this.reservaForm.valueChanges.subscribe(() => {
      this.calcularMontos();
    });
  }

  calcularMontos(): void {
    const val = this.reservaForm.value;
    const prop = val.propiedad as Propiedad;
    
    // Solo calculamos si tenemos Propiedad, Fecha Inicio y Fecha Fin
    if (prop && val.fechaInicio && val.fechaFin) {
      const inicio = new Date(val.fechaInicio);
      const fin = new Date(val.fechaFin);

      // Calculamos la diferencia de días
      const diffTime = fin.getTime() - inicio.getTime();
      const noches = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

      if (noches > 0) {
        // Fórmula: costoTotal = noches * precioPorNoche
        const costoCalculado = noches * prop.precioPorNoche;
        
        // Fórmula: montoDeposito = costoTotal * (porcentaje / 100)
        const depositoCalculado = costoCalculado * (prop.porcentajeDeposito / 100);

        // Actualizamos el formulario sin disparar un bucle infinito (emitEvent: false)
        this.reservaForm.patchValue({
          montoTotal: costoCalculado,
          depositoRetenido: depositoCalculado
        }, { emitEvent: false });
      } else {
        // Si la fecha es inválida (fin antes que inicio), reseteamos a 0
        this.reservaForm.patchValue({ montoTotal: 0, depositoRetenido: 0 }, { emitEvent: false });
      }
    }
  }

  guardar(): void {
    if (this.reservaForm.invalid) {
      this.reservaForm.markAllAsTouched();
      return;
    }

    // Al enviar, nos aseguramos de mandar el objeto de la reserva
    this.reservaService.crearReserva(this.reservaForm.value as any).subscribe({
      next: () => {
        alert('¡Reserva cargada con éxito! 📅');
        this.router.navigate(['/reservas']);
      },
      error: (err) => {
        console.error('Error al guardar reserva:', err);
        alert('Error al guardar. Revisá si las fechas no se solapan.');
      }
    });
  }
}