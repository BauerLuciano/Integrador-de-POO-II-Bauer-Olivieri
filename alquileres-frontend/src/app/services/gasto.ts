import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class GastoService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/gastos';

  // Traer gastos de una propiedad específica
  getGastosPorPropiedad(propiedadId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/propiedad/${propiedadId}`);
  }

  // Guardar un gasto nuevo
  crearGasto(gasto: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, gasto);
  }

  // Eliminar un gasto si nos equivocamos
  eliminarGasto(id: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/${id}`);
  }
}