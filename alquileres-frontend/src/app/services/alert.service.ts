import { Injectable } from '@angular/core';
import Swal, { SweetAlertIcon } from 'sweetalert2';

@Injectable({
  providedIn: 'root'
})
export class AlertService {

  exito(titulo: string, texto: string = '') {
    return Swal.fire({
      icon: 'success',
      title: titulo,
      text: texto,
      confirmButtonColor: '#198754', 
      timer: 2500,
      timerProgressBar: true
    });
  }

  error(titulo: string, texto: string = 'Algo salió mal...') {
    return Swal.fire({
      icon: 'error',
      title: titulo,
      text: texto,
      confirmButtonColor: '#dc3545', 
    });
  }

 
  notificacion(titulo: string, texto: string, icono: SweetAlertIcon = 'info') {
    return Swal.fire({
      title: titulo,
      text: texto,
      icon: icono,
      confirmButtonColor: '#0d6efd', 
    });
  }

  confirmar(titulo: string, texto: string, confirmText: string = 'Sí, confirmar') {
    return Swal.fire({
      title: titulo,
      text: texto,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#0d6efd',
      cancelButtonColor: '#6c757d',
      confirmButtonText: confirmText,
      cancelButtonText: 'No, volver',
      reverseButtons: true
    });
  }
}