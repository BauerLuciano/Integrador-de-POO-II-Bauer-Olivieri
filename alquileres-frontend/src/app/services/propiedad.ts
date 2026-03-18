import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Propiedad } from '../models/propiedad';

@Injectable({
  providedIn: 'root'
})
export class PropiedadService {
  private apiUrl = 'http://localhost:8080/api/propiedades';
  private http = inject(HttpClient);

  getPropiedades(): Observable<Propiedad[]> {
    return this.http.get<Propiedad[]>(this.apiUrl);
  }

  crearPropiedad(propiedad: Propiedad): Observable<Propiedad> {
    return this.http.post<Propiedad>(this.apiUrl, propiedad);
  }

  eliminarPropiedad(id: number): Observable<void> {
    return this.http.delete<void>(`http://localhost:8080/api/propiedades/${id}`);
  }

  getPropiedadById(id: number): Observable<Propiedad> {
    return this.http.get<Propiedad>(`http://localhost:8080/api/propiedades/${id}`);
  }

  actualizarPropiedad(id: number, propiedad: Propiedad): Observable<Propiedad> {
    return this.http.put<Propiedad>(`http://localhost:8080/api/propiedades/${id}`, propiedad);
  }
}