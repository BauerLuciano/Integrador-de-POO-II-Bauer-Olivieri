import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms'; 
import { PropiedadService } from '../../services/propiedad';
import { Propiedad } from '../../models/propiedad';

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

  // VARIABLES PARA LOS FILTROS 
  filtroTexto: string = '';
  filtroEstado: string = '';

  private propiedadService = inject(PropiedadService);

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

  // LÓGICA DE FILTRADO 
  filtrarPropiedades(): void {
    this.listaPropiedades = this.todasLasPropiedades.filter(p => {
      
      // 1. Chequeamos el texto
      const termino = this.filtroTexto.toLowerCase();
      const coincideTexto = !termino || 
        p.direccion.toLowerCase().includes(termino) ||
        (p.propietario?.nombre?.toLowerCase() || '').includes(termino) ||
        (p.propietario?.apellido?.toLowerCase() || '').includes(termino);

      // 2. Chequeamos el estado
      const coincideEstado = !this.filtroEstado || p.estado === this.filtroEstado;
      return coincideTexto && coincideEstado;
    });
  }

  eliminar(id: number | undefined) {
    if (!id) return;

    if (confirm('¿Estás seguro de que querés eliminar esta propiedad?')) {
      this.propiedadService.eliminarPropiedad(id).subscribe({
        next: () => {
          this.todasLasPropiedades = this.todasLasPropiedades.filter(p => p.id !== id);
          this.filtrarPropiedades(); 
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