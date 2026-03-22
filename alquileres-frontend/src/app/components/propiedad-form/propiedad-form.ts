import { Component, OnInit, inject } from '@angular/core';
import { CommonModule, Location } from '@angular/common'; 
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { PropiedadService } from '../../services/propiedad';
import { PropietarioService } from '../../services/propietario';
import { Propietario } from '../../models/propietario';
import { GastoService } from '../../services/gasto';
import { AlertService } from '../../services/alert.service';

@Component({
  selector: 'app-propiedad-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './propiedad-form.html'
})
export class PropiedadFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private propService = inject(PropiedadService);
  private propieService = inject(PropietarioService);
  private gastoService = inject(GastoService); 
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private location = inject(Location); 
  private alertService = inject(AlertService);

  propietarios: Propietario[] = [];
  propiedadId: number | null = null;
  estadoOriginal: string = '';

  gastos: any[] = [];
  gastoForm = this.fb.group({
    concepto: ['', [Validators.required, Validators.maxLength(100)]],
    monto: [null, [Validators.required, Validators.min(1)]],
    fecha: [new Date().toISOString().substring(0, 10), Validators.required] 
  });

  form = this.fb.group({
    direccion: ['', [Validators.required, Validators.maxLength(150)]],
    precioPorNoche: [0, [Validators.required, Validators.min(1)]],
    porcentajeDeposito: [0, [Validators.required, Validators.min(0), Validators.max(100)]],
    estado: ['Disponible'],
    propietario: [null, Validators.required],
    
    politicaCancelacion: this.fb.group({
      tipo: ['FLEXIBLE', Validators.required],
      porcentajeRetencion: [100], 
      diasAnticipacionSinCargo: [7],
      porcentajePenalidadTardia: [50]
    })
  });

  ngOnInit() {
    this.propieService.getPropietarios().subscribe(data => {
      this.propietarios = data;
      this.route.paramMap.subscribe(params => {
        const id = params.get('id');
        if (id) {
          this.propiedadId = Number(id);
          this.cargarDatosPropiedad(this.propiedadId);
          this.cargarGastos(this.propiedadId);
        }
      });
    });
  }

  cargarDatosPropiedad(id: number) {
    this.propService.getPropiedadById(id).subscribe({
      next: (prop) => {
        this.estadoOriginal = prop.estado;
        this.form.patchValue({
          direccion: prop.direccion,
          precioPorNoche: prop.precioPorNoche,
          porcentajeDeposito: prop.porcentajeDeposito,
          estado: prop.estado,
          propietario: prop.propietario as any,
          politicaCancelacion: prop.politicaCancelacion 
        });
      },
      error: (err) => console.error('Error cargando propiedad', err)
    });
  }

  compararPropietarios(p1: Propietario, p2: Propietario): boolean {
    return p1 && p2 ? p1.id === p2.id : p1 === p2;
  }

  cancelar() {
    this.location.back(); 
  }

  guardar() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    
    if (this.propiedadId) {
      this.propService.actualizarPropiedad(this.propiedadId, this.form.value as any).subscribe({
        next: () => {
          this.alertService.exito('¡Actualizada!', 'La propiedad fue modificada con éxito.')
            .then(() => this.location.back()); 
        },
        error: (err) => this.alertService.error('Error', 'No se pudo actualizar la propiedad.')
      });
    } else {
      this.propService.crearPropiedad(this.form.value as any).subscribe({
        next: () => {
          this.alertService.exito('¡Creada!', 'La propiedad fue registrada con éxito.')
            .then(() => this.location.back()); 
        },
        error: (err) => this.alertService.error('Error', 'No se pudo crear la propiedad.')
      });
    }
  }

  cargarGastos(propiedadId: number) {
    this.gastoService.getGastosPorPropiedad(propiedadId).subscribe({
      next: (data) => this.gastos = data,
      error: (err) => console.error('Error al cargar gastos', err)
    });
  }

  guardarGasto() {
    if (this.gastoForm.invalid || !this.propiedadId) return;

    const nuevoGasto = {
      ...this.gastoForm.value,
      propiedad: { id: this.propiedadId } 
    };

    this.gastoService.crearGasto(nuevoGasto).subscribe({
      next: (gastoGuardado) => {
        this.gastos.push(gastoGuardado); 
        this.gastoForm.reset({ fecha: new Date().toISOString().substring(0, 10) }); 
        this.alertService.exito('Gasto Registrado', 'Se sumó a la cuenta de la propiedad.');
      },
      error: (err) => this.alertService.error('Error', 'No se pudo guardar el gasto.')
    });
  }

  eliminarGasto(id: number) {
    this.alertService.confirmar('¿Borrar gasto?', '¿Estás seguro de que querés eliminar este registro?', 'Sí, borrar')
      .then((result) => {
        if (result.isConfirmed) {
          this.gastoService.eliminarGasto(id).subscribe({
            next: () => {
              this.gastos = this.gastos.filter(g => g.id !== id);
              this.alertService.exito('¡Eliminado!', 'El gasto fue borrado del historial.');
            },
            error: (err) => this.alertService.error('Error', 'No se pudo borrar el gasto.')
          });
        }
      });
  }
}