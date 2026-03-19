import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ReporteLiquidacion } from '../models/reporte-liquidacion';

@Injectable({ providedIn: 'root' })
export class ReporteService {
  private apiUrl = 'http://localhost:8080/api/reservas/liquidacion';
  private apiUrlLiquidaciones = 'http://localhost:8080/api/liquidaciones';
  private http = inject(HttpClient);

  getLiquidacion(propietarioId: number, mes: number, anio: number): Observable<ReporteLiquidacion> {
    return this.http.get<ReporteLiquidacion>(`${this.apiUrl}?id=${propietarioId}&mes=${mes}&anio=${anio}`);
  }

  exportarPDF(propietarioId: number, mes: number, anio: number) {
    const url = `${this.apiUrl}/pdf?id=${propietarioId}&mes=${mes}&anio=${anio}`;
    window.open(url, '_blank');
  }

  confirmarLiquidacion(propietarioId: number, mes: number, anio: number): Observable<any> {
    const url = `${this.apiUrlLiquidaciones}/propietario/${propietarioId}/confirmar?mes=${mes}&anio=${anio}`;
    return this.http.post(url, {}); 
  }

  getHistorialLiquidaciones(propietarioId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrlLiquidaciones}/propietario/${propietarioId}/historial`);
  }
}