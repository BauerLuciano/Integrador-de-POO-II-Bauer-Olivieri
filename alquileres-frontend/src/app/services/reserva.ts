import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Reserva } from '../models/reserva';

@Injectable({ providedIn: 'root' })
export class ReservaService {
  private apiUrl = 'http://localhost:8080/api/reservas';
  private http = inject(HttpClient);

  getUltimasReservas(): Observable<Reserva[]> {
    return this.http.get<Reserva[]>(this.apiUrl);
  }

  crearReserva(reserva: Reserva): Observable<Reserva> {
    return this.http.post<Reserva>(this.apiUrl, reserva);
  }

  getHistorialPropiedad(propiedadId: number): Observable<Reserva[]> {
    return this.http.get<Reserva[]>(`${this.apiUrl}/historial/propiedad/${propiedadId}`);
  }

  getIngresosPropietario(propietarioId: number, inicio: string, fin: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/ingresos/propietario/${propietarioId}?inicio=${inicio}&fin=${fin}`);
  }

  cancelarReserva(id: number, motivo: string, detalle: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}/cancelar?motivo=${encodeURIComponent(motivo)}&detalle=${encodeURIComponent(detalle)}`, {}); 
  }

  getReservaById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }

  registrarPago(reservaId: number, pago: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/${reservaId}/pagos`, pago);
  }

  getSaldoPendiente(id: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/${id}/saldo`);
  }
}