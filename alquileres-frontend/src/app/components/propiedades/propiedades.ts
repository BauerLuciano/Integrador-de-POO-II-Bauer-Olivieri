import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { PropiedadService } from '../../services/propiedad';
import { Propiedad } from '../../models/propiedad';

@Component({
  selector: 'app-propiedades',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './propiedades.html',
  styleUrl: './propiedades.css'
})
export class PropiedadesComponent implements OnInit {
  
  listaPropiedades: Propiedad[] = [];
  loading: boolean = true;

  private propiedadService = inject(PropiedadService);

  ngOnInit(): void {
    this.cargarPropiedades();
  }

  cargarPropiedades(): void {
    this.loading = true;
    this.propiedadService.getPropiedades().subscribe({
      next: (data) => {
        this.listaPropiedades = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error al traer propiedades:', err);
        this.loading = false;
      }
    });
  }

  // Método para el botón de borrar (opcional por ahora)
  eliminar(id: number | undefined) {
    if (!id) return;

    if (confirm('¿Estás seguro de que querés eliminar esta propiedad?')) {
      this.propiedadService.eliminarPropiedad(id).subscribe({
        next: () => {
          // Filtramos la lista para sacar la propiedad borrada sin tener que recargar la página
          this.listaPropiedades = this.listaPropiedades.filter(p => p.id !== id);
          alert('Propiedad eliminada con éxito');
        },
        error: (err) => {
          console.error('Error al eliminar:', err);
          alert('No se pudo eliminar la propiedad. Quizás tenga reservas asociadas.');
        }
      });
    }
  }
}