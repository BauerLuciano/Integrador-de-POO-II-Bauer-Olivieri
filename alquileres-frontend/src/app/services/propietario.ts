import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Propietario } from '../models/propietario'; // Importamos tu modelo

@Injectable({
  providedIn: 'root'
})
export class PropietarioService {
  // ATENCIÓN: Esta URL tiene que ser la misma a la que le pegan en Postman
  private apiUrl = 'http://localhost:8080/api/propietarios';

  constructor(private http: HttpClient) { }


  getPropietarios(): Observable<Propietario[]> {
    return this.http.get<Propietario[]>(this.apiUrl);
  }

  crearPropietario(propietario: Propietario): Observable<Propietario> {
    return this.http.post<Propietario>(this.apiUrl, propietario);
  }

  getPropietarioById(id: number): Observable<Propietario> {
    return this.http.get<Propietario>(`${this.apiUrl}/${id}`);
  }

  actualizarPropietario(id: number, propietario: Propietario): Observable<Propietario> {
    return this.http.put<Propietario>(`${this.apiUrl}/${id}`, propietario);
  }

  eliminarPropietario(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  reactivarPropietario(id: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}/reactivar`, {});
  }
}