import { Component, OnInit, inject } from '@angular/core';
import { CommonModule, Location } from '@angular/common'; // <-- Agregamos Location para volver atrás
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { PropiedadService } from '../../services/propiedad';
import { PropietarioService } from '../../services/propietario';
import { Propietario } from '../../models/propietario';

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
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private location = inject(Location); 

  propietarios: Propietario[] = [];
  propiedadId: number | null = null;

  form = this.fb.group({
    direccion: ['', Validators.required],
    precioPorNoche: [0, [Validators.required, Validators.min(1)]],
    porcentajeDeposito: [0, [Validators.required, Validators.max(100)]],
    estado: ['Disponible'],
    propietario: [null, Validators.required],
    
    // --- ESTE ES EL NUEVO SUB-GRUPO DE LA POLÍTICA ---
    politicaCancelacion: this.fb.group({
      tipo: ['FLEXIBLE', Validators.required],
      // Valores por defecto para que no vayan vacíos
      porcentajeRetencion: [100], 
      diasAnticipacionSinCargo: [7],
      porcentajePenalidadTardia: [50]
    })
  });

  ngOnInit() {
    this.propieService.getPropietarios().subscribe(data => {
      this.propietarios = data;

      // Miramos la URL para saber si estamos editando
      this.route.paramMap.subscribe(params => {
        const id = params.get('id');
        if (id) {
          this.propiedadId = Number(id);
          this.cargarDatosPropiedad(this.propiedadId);
        }
      });
    });
  }

  cargarDatosPropiedad(id: number) {
    this.propService.getPropiedadById(id).subscribe({
      next: (prop) => {
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
    if (this.form.invalid) return;
    
    if (this.propiedadId) {
      this.propService.actualizarPropiedad(this.propiedadId, this.form.value as any).subscribe({
        next: () => {
          alert('¡Propiedad actualizada!');
          this.location.back(); 
        }
      });
    } else {
      this.propService.crearPropiedad(this.form.value as any).subscribe({
        next: () => {
          alert('¡Propiedad creada!');
          this.location.back(); 
        }
      });
    }
  }
}