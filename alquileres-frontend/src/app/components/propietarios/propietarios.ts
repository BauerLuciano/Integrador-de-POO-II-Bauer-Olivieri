import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common'; 
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms'; 
import { PropietarioService } from '../../services/propietario';
import { Propietario } from '../../models/propietario';
import { AlertService } from '../../services/alert.service';

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

  filtroTexto: string = '';
  filtroEstado: string = '';
  
  // Variable para controlar la paginación
  paginaActual: number = 1;

  private propietarioService = inject(PropietarioService);
  private alertService = inject(AlertService);

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

  filtrarPropietarios(): void {
    // Reseteamos a la página 1 al filtrar
    this.paginaActual = 1;

    this.propietarios = this.todosLosPropietarios.filter(p => {
      const termino = this.filtroTexto.toLowerCase();
      const coincideTexto = !termino || 
        (p.nombre?.toLowerCase() || '').includes(termino) ||
        (p.apellido?.toLowerCase() || '').includes(termino) ||
        (p.dni?.toLowerCase() || '').includes(termino);

      let coincideEstado = true;
      if (this.filtroEstado === 'activo') coincideEstado = p.activo !== false;
      if (this.filtroEstado === 'inactivo') coincideEstado = p.activo === false;

      return coincideTexto && coincideEstado;
    });
  }

  eliminar(id: number) {
    this.alertService.confirmar('¿Dar de baja?', '¿Estás seguro de que querés dar de baja a este propietario?', 'Sí, dar de baja').then((result) => {
      if (result.isConfirmed) {
        this.propietarioService.eliminarPropietario(id).subscribe({
          next: () => {
            const index = this.todosLosPropietarios.findIndex(p => p.id === id);
            if (index !== -1) {
              this.todosLosPropietarios[index].activo = false;
              this.filtrarPropietarios(); // Esto también resetea la página a 1
            }
            this.alertService.exito('¡Dado de baja!', 'El propietario fue desactivado correctamente.');
          },
          error: (err) => {
            console.error('Error al dar de baja:', err);
            this.alertService.error('Error', 'No se pudo dar de baja al propietario.');
          }
        });
      }
    });
  }

  reactivar(id: number) {
    this.alertService.confirmar('¿Reactivar?', '¿Estás seguro de que querés reactivar a este propietario?', 'Sí, reactivar').then((result) => {
      if (result.isConfirmed) {
        this.propietarioService.reactivarPropietario(id).subscribe({
          next: () => {
            const index = this.todosLosPropietarios.findIndex(p => p.id === id);
            if (index !== -1) {
              this.todosLosPropietarios[index].activo = true;
              this.filtrarPropietarios(); // Esto también resetea la página a 1
            }
            this.alertService.exito('¡Reactivado!', 'Propietario reactivado con éxito.');
          },
          error: (err) => {
            console.error('Error al reactivar:', err);
            this.alertService.error('Error', 'No se pudo reactivar al propietario.');
          }
        });
      }
    });
  }
}