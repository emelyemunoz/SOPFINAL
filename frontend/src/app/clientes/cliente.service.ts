import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ClienteService {

  private apiUrl = '/api/clientes';

  constructor(private http: HttpClient) {
  }

  registrar(cliente: any): Observable<any> {
    return this.http.post(this.apiUrl, cliente);
  }

  login(datosLogin: any): Observable<any> {
    return this.http.post(
      this.apiUrl + '/login',
      datosLogin
    );
  }
}