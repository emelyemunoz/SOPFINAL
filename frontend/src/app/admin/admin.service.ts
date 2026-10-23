import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Producto, Lote, auditoria } from './admin.model';


@Injectable({
  providedIn: 'root'
})
export class ProductoService {

  private url = '/api/producto';

  constructor(private http: HttpClient) {}

  listar(): Observable<Producto[]> {
    return this.http.get<Producto[]>(
      `${this.url}/listproductos`
    );
  }

  crear(producto: Partial<Producto>): Observable<Producto> {
    const usuario = this.obtenerUsuario();

    return this.http.post<Producto>(
      `${this.url}/create_producto`,
      producto,
      {
        params: { usuario: usuario }
      }
    );
  }

  actualizar(
    id: number,
    producto: Partial<Producto>
  ): Observable<Producto> {

    const usuario = this.obtenerUsuario();

    return this.http.put<Producto>(
      `${this.url}/update/${id}`,
      producto,
      {
        params: { usuario: usuario }
      }
    );
  }

  eliminar(id: number): Observable<void> {
    const usuario = this.obtenerUsuario();

    return this.http.delete<void>(
      `${this.url}/delete_producto/${id}`,
      {
        params: { usuario: usuario }
      }
    );
  }

  private obtenerUsuario(): string {
    const usuarioGuardado = localStorage.getItem('usuario');

    if (!usuarioGuardado) {
      return 'DESCONOCIDO';
    }

    try {
      const usuario = JSON.parse(usuarioGuardado);
      return usuario.nombreUsuario || 'DESCONOCIDO';
    } catch {
      return usuarioGuardado;
    }
  }
}


@Injectable({
  providedIn: 'root'
})
export class LoteService {

  private url = '/api/lote';

  constructor(private http: HttpClient) {}

  listar(): Observable<Lote[]> {
    return this.http.get<Lote[]>(
      `${this.url}/listlote`
    );
  }

  crear(lote: Partial<Lote>): Observable<Lote> {
    const usuario = this.obtenerUsuario();

    return this.http.post<Lote>(
      `${this.url}/create_lote`,
      lote,
      {
        params: { usuario: usuario }
      }
    );
  }

  actualizar(
    id: number,
    lote: Partial<Lote>
  ): Observable<Lote> {

    const usuario = this.obtenerUsuario();

    return this.http.put<Lote>(
      `${this.url}/update/${id}`,
      lote,
      {
        params: { usuario: usuario }
      }
    );
  }

  eliminar(id: number): Observable<void> {
    const usuario = this.obtenerUsuario();

    return this.http.delete<void>(
      `${this.url}/delete_lote/${id}`,
      {
        params: { usuario: usuario }
      }
    );
  }

  private obtenerUsuario(): string {
    const usuarioGuardado = localStorage.getItem('usuario');

    if (!usuarioGuardado) {
      return 'DESCONOCIDO';
    }

    try {
      const usuario = JSON.parse(usuarioGuardado);
      return usuario.nombreUsuario || 'DESCONOCIDO';
    } catch {
      return usuarioGuardado;
    }
  }
}


@Injectable({
  providedIn: 'root'
})
export class AuditoriaService {

  private url = '/api/auditoria';

  constructor(private http: HttpClient) {}

  listar(): Observable<auditoria[]> {
    return this.http.get<auditoria[]>(
      this.url
    );
  }
}