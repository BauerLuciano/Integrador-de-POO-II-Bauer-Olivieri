import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, RouterModule, ActivatedRoute } from '@angular/router';
import { PropietarioService } from '../../services/propietario';
import { Propietario } from '../../models/propietario';
import { AlertService } from '../../services/alert.service';

@Component({
  selector: 'app-propietario-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './propietario-form.html',
  styleUrl: './propietario-form.css'
})
export class PropietarioFormComponent implements OnInit { 
  private fb = inject(FormBuilder);
  private propietarioService = inject(PropietarioService);
  private router = inject(Router);
  private route = inject(ActivatedRoute); 
  private alertService = inject(AlertService);

  propietarioId: number | null = null;

  propietarioForm = this.fb.group({
    dni: ['', [Validators.required, Validators.pattern("^[0-9]*$"), Validators.minLength(7), Validators.maxLength(10)]],
    nombre: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50), Validators.pattern('^[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+$')]],
    apellido: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50), Validators.pattern('^[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+$')]],
    email: ['', [Validators.required, Validators.email, Validators.maxLength(100)]], 
    telefono: ['', [Validators.required, Validators.pattern("^[0-9+ ]*$"), Validators.maxLength(20)]],
    
    esquemaComision: this.fb.group({
      tipo_esquema: ['FIJA', Validators.required],
      porcentaje: [10], 
      montoUmbral: [100000],
      porcentajeBase: [10],
      porcentajeExcedente: [5]
    })
  });

  cargarDatos(id: number) {
    this.propietarioService.getPropietarioById(id).subscribe({
      next: (propietario) => {
        this.propietarioForm.patchValue({
          dni: propietario.dni,
          nombre: propietario.nombre,
          apellido: propietario.apellido,
          email: propietario.email || '',
          telefono: propietario.telefono || '',
          esquemaComision: propietario.esquemaComision || { tipo_esquema: 'FIJA', porcentaje: 10 }
        });
      },
      error: (err) => console.error('Error al cargar propietario:', err)
    });
  }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.propietarioId = Number(id); 
        this.cargarDatos(this.propietarioId);
      }
    });
  }

  permitirSoloNumeros(event: any, controlName: string) {
    const input = event.target as HTMLInputElement;
    let valorLimpio = input.value.replace(/[^0-9]/g, ''); 
    this.propietarioForm.get(controlName)?.setValue(valorLimpio, { emitEvent: false });
    input.value = valorLimpio;
  }

  permitirSoloLetras(event: any, controlName: string) {
    const input = event.target as HTMLInputElement;
    let valorLimpio = input.value.replace(/[^a-zA-ZñÑáéíóúÁÉÍÓÚ\s]/g, ''); 
    this.propietarioForm.get(controlName)?.setValue(valorLimpio, { emitEvent: false });
    input.value = valorLimpio;
  }

  permitirSoloTelefono(event: any, controlName: string) {
    const input = event.target as HTMLInputElement;
    let valorLimpio = input.value.replace(/[^0-9+ ]/g, ''); 
    this.propietarioForm.get(controlName)?.setValue(valorLimpio, { emitEvent: false });
    input.value = valorLimpio;
  }

  guardar(): void {
    if (this.propietarioForm.invalid) {
      this.propietarioForm.markAllAsTouched();
      return;
    }

    const datosFormulario = this.propietarioForm.value as Propietario;

    if (this.propietarioId) {
      this.propietarioService.actualizarPropietario(this.propietarioId, datosFormulario).subscribe({
        next: () => {
          this.alertService.exito('¡Actualizado!', 'Dueño actualizado con éxito.')
            .then(() => this.router.navigate(['/propietarios']));
        },
        error: (err) => {
          console.error('Error al actualizar:', err);
          this.alertService.error('Error', 'No se pudo actualizar los datos.');
        }
      });
    } else {
      this.propietarioService.crearPropietario(datosFormulario).subscribe({
        next: () => {
          this.alertService.exito('¡Registrado!', 'Dueño registrado con éxito.')
            .then(() => this.router.navigate(['/propietarios']));
        },
        error: (err) => {
          console.error('Error en el backend:', err);
          this.alertService.error('Error', 'No se pudo guardar.');
        }
      });
    }
  }
}