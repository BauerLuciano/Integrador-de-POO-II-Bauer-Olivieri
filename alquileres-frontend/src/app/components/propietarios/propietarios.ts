import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common'; 
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms'; 
import { PropietarioService } from '../../services/propietario';
import { Propietario } from '../../models/propietario';

@Component({
  selector: 'app-propietarios',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule], 
  templateUrl: './propietarios.html',
  styleUrl: './propietarios.css'
})
export class PropietariosComponent implements OnInit {
  
  todosLosPropietarios: Propietario[] = []; 
  propietarios: Propietario[] = [];         

  // VARIABLES DE FILTRO
  filtroTexto: string = '';
  filtroEstado: string = '';

  private propietarioService = inject(PropietarioService);

  ngOnInit(): void {
    this.listar();
  }

  listar(): void {
    this.propietarioService.getPropietarios().subscribe({
      next: (data) => {
        this.todosLosPropietarios = data;
        this.propietarios = data; 
      },
      error: (err) => console.error('Error al conectar con el Backend:', err)
    });
  }

  // LÓGICA DE FILTRADO
  filtrarPropietarios(): void {
    this.propietarios = this.todosLosPropietarios.filter(p => {
      // 1. Filtro por texto (Nombre, Apellido o DNI)
      const termino = this.filtroTexto.toLowerCase();
      const coincideTexto = !termino || 
        (p.nombre?.toLowerCase() || '').includes(termino) ||
        (p.apellido?.toLowerCase() || '').includes(termino) ||
        (p.dni?.toLowerCase() || '').includes(termino);

      // 2. Filtro por Estado (Activo/Inactivo)
      let coincideEstado = true;
      if (this.filtroEstado === 'activo') coincideEstado = p.activo !== false;
      if (this.filtroEstado === 'inactivo') coincideEstado = p.activo === false;

      return coincideTexto && coincideEstado;
    });
  }

  eliminar(id: number) {
    if (confirm('¿Estás seguro de que querés dar de baja a este propietario?')) {
      this.propietarioService.eliminarPropietario(id).subscribe({
        next: () => {
          const index = this.todosLosPropietarios.findIndex(p => p.id === id);
          if (index !== -1) {
            this.todosLosPropietarios[index].activo = false;
            this.filtrarPropietarios();
          }
        },
        error: (err) => {
          console.error('Error al dar de baja:', err);
          alert('No se pudo dar de baja al propietario.');
        }
      });
    }
  }

  reactivar(id: number) {
    if (confirm('¿Estás seguro de que querés reactivar a este propietario?')) {
      this.propietarioService.reactivarPropietario(id).subscribe({
        next: () => {
          const index = this.todosLosPropietarios.findIndex(p => p.id === id);
          if (index !== -1) {
            this.todosLosPropietarios[index].activo = true;
            this.filtrarPropietarios();
          }
          alert('Propietario reactivado con éxito.');
        },
        error: (err) => {
          console.error('Error al reactivar:', err);
          alert('No se pudo reactivar al propietario.');
        }
      });
    }
  }
}