import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, RouterModule, ActivatedRoute } from '@angular/router';
import { PropietarioService } from '../../services/propietario';
import { Propietario } from '../../models/propietario';

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

  propietarioId: number | null = null;

 propietarioForm = this.fb.group({
    dni: ['', [Validators.required, Validators.pattern("^[0-9]*$"), Validators.minLength(7)]],
    nombre: ['', [Validators.required, Validators.minLength(2)]],
    apellido: ['', [Validators.required, Validators.minLength(2)]],
    email: ['', [Validators.required, Validators.email]], 
    telefono: ['', [Validators.required, Validators.pattern("^[0-9+ ]*$")]],
    
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
          // Si el dueño ya tenía comisión, la cargamos. Si no, le dejamos la Fija por defecto
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

  guardar(): void {
    if (this.propietarioForm.invalid) {
      this.propietarioForm.markAllAsTouched();
      return;
    }

    const datosFormulario = this.propietarioForm.value as Propietario;

    if (this.propietarioId) {
      
      // MODO EDICIÓN
      this.propietarioService.actualizarPropietario(this.propietarioId, datosFormulario).subscribe({
        next: () => {
          alert('¡Dueño actualizado con éxito! 🏠');
          this.router.navigate(['/propietarios']);
        },
        error: (err) => {
          console.error('Error al actualizar:', err);
          alert('No se pudo actualizar los datos.');
        }
      });

    } else {
      
      // MODO CREACIÓN
      this.propietarioService.crearPropietario(datosFormulario).subscribe({
        next: () => {
          alert('¡Dueño registrado con éxito! 🏠');
          this.router.navigate(['/propietarios']);
        },
        error: (err) => {
          console.error('Error en el backend:', err);
          alert('No se pudo guardar.');
        }
      });
      
    }
  }
}