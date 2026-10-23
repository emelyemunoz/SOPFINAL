import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CorreoBuzon {
  id: number;
  remitente: string;
  asunto: string;
  fecha: string;
  contenido: string;
}

@Injectable({
  providedIn: 'root'
})
export class CorreoService {

  private apiUrl = '/api/correo';

  constructor(private http: HttpClient) {}

  private obtenerIdCliente(): number {

    const usuarioGuardado = localStorage.getItem('usuario');

    if (!usuarioGuardado) {
      throw new Error('No hay usuario con sesión iniciada');
    }

    const usuario = JSON.parse(usuarioGuardado);

    return usuario.idCliente;
  }

  obtenerCorreos(): Observable<CorreoBuzon[]> {

    const idCliente = this.obtenerIdCliente();

    return this.http.get<CorreoBuzon[]>(
      `${this.apiUrl}/buzon/cliente/${idCliente}`
    );
  }

  obtenerCorreo(id: number): Observable<CorreoBuzon> {

    const idCliente = this.obtenerIdCliente();

    return this.http.get<CorreoBuzon>(
      `${this.apiUrl}/buzon/cliente/${idCliente}/${id}`
    );
  }
}