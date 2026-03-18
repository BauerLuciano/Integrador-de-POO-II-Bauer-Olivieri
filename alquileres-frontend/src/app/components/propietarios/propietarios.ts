import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common'; // Necesario para pipes y directivas
import { PropietarioService } from '../../services/propietario';
import { Propietario } from '../../models/propietario';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-propietarios',
  standalone: true,
  imports: [CommonModule, RouterModule], // Lo agregamos aquí
  templateUrl: './propietarios.html',
  styleUrl: './propietarios.css'
})
export class PropietariosComponent implements OnInit {
  
  // Lista donde guardaremos lo que venga de Spring Boot
  propietarios: Propietario[] = [];

  // Inyectamos el servicio (sintaxis moderna con inject)
  private propietarioService = inject(PropietarioService);

  ngOnInit(): void {
    this.listar();
  }

  listar(): void {
    this.propietarioService.getPropietarios().subscribe({
      next: (data) => {
        this.propietarios = data;
        console.log('Datos recibidos:', data);
      },
      error: (err) => {
        console.error('Error al conectar con el Backend:', err);
      }
    });
  }

  eliminar(id: number) {
    if (confirm('¿Estás seguro de que querés dar de baja a este propietario?')) {
      this.propietarioService.eliminarPropietario(id).subscribe({
        next: () => {
          const index = this.propietarios.findIndex(p => p.id === id);
          if (index !== -1) {
            this.propietarios[index].activo = false;
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
          // Buscamos al dueño en la lista y lo ponemos ACTIVO visualmente
          const index = this.propietarios.findIndex(p => p.id === id);
          if (index !== -1) {
            this.propietarios[index].activo = true;
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