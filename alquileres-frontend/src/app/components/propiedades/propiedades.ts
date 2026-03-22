import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms'; 
import { PropiedadService } from '../../services/propiedad';
import { Propiedad } from '../../models/propiedad';
import { AlertService } from '../../services/alert.service';

@Component({
  selector: 'app-propiedades',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule], 
  templateUrl: './propiedades.html',
  styleUrl: './propiedades.css'
})
export class PropiedadesComponent implements OnInit {
  
  todasLasPropiedades: Propiedad[] = [];
  listaPropiedades: Propiedad[] = [];  
  loading: boolean = true;

  filtroTexto: string = '';
  filtroEstado: string = '';

  private propiedadService = inject(PropiedadService);
  private alertService = inject(AlertService);

  ngOnInit(): void {
    this.cargarPropiedades();
  }

  cargarPropiedades(): void {
    this.loading = true;
    this.propiedadService.getPropiedades().subscribe({
      next: (data) => {
        this.todasLasPropiedades = data;
        this.listaPropiedades = data; 
        this.loading = false;
      },
      error: (err) => {
        console.error('Error al traer propiedades:', err);
        this.loading = false;
      }
    });
  }

  filtrarPropiedades(): void {
    this.listaPropiedades = this.todasLasPropiedades.filter(p => {
      const termino = this.filtroTexto.toLowerCase();
      const coincideTexto = !termino || 
        p.direccion.toLowerCase().includes(termino) ||
        (p.propietario?.nombre?.toLowerCase() || '').includes(termino) ||
        (p.propietario?.apellido?.toLowerCase() || '').includes(termino);

      const coincideEstado = !this.filtroEstado || p.estado === this.filtroEstado;
      return coincideTexto && coincideEstado;
    });
  }

  eliminar(id: number | undefined) {
    if (!id) return;

    this.alertService.confirmar(
      '¿Dar de baja propiedad?', 
      'La propiedad dejará de estar disponible y pasará a estado Inactiva.', 
      'Sí, dar de baja'
    ).then((result) => {
      if (result.isConfirmed) {
        this.propiedadService.eliminarPropiedad(id).subscribe({
          next: () => {
            const index = this.todasLasPropiedades.findIndex(p => p.id === id);
            if (index !== -1) {
              this.todasLasPropiedades[index].estado = 'Inactiva';
              this.filtrarPropiedades();
            }
            this.alertService.exito('¡Dada de baja!', 'La propiedad fue marcada como Inactiva con éxito.');
          },
          error: (err) => {
            console.error('Error al dar de baja:', err);
            this.alertService.error('Error', 'No se pudo desactivar la propiedad. Verificá tu conexión.');
          }
        });
      }
    });
  }

  reactivar(propiedad: Propiedad) {
    if (!propiedad.id) return;

    const idPropiedad = propiedad.id; 

    this.alertService.confirmar(
      '¿Reactivar propiedad?', 
      'La propiedad volverá a estar "Disponible" para recibir reservas.', 
      'Sí, reactivar'
    ).then((result) => {
      if (result.isConfirmed) {
        const propReactivada = { ...propiedad, estado: 'Disponible' };
        this.propiedadService.actualizarPropiedad(idPropiedad, propReactivada).subscribe({
          next: () => {
            const index = this.todasLasPropiedades.findIndex(p => p.id === idPropiedad);
            if (index !== -1) {
              this.todasLasPropiedades[index].estado = 'Disponible';
              this.filtrarPropiedades();
            }
            this.alertService.exito('¡Reactivada!', 'La propiedad ya está disponible en el catálogo.');
          },
          error: (err) => {
            console.error('Error al reactivar:', err);
            this.alertService.error('Error', 'No se pudo reactivar la propiedad.');
          }
        });
      }
    });
  }
}
