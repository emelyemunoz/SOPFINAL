import {
  Component,
  OnInit,
  ChangeDetectorRef
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-pedido-admin',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './pedidoAdmin.html',
  styleUrl: './pedidoAdmin.css'
})
export class PedidoAdmin implements OnInit {

  pedidos: any[] = [];

  constructor(
    private http: HttpClient,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cargarPedidos();
  }

  cargarPedidos(): void {

    this.http.get<any[]>(
      '/api/pedido/listpedidos'
    ).subscribe({

      next: (respuesta) => {
        this.pedidos = respuesta;
        this.cdr.detectChanges();
      },

      error: (error) => {
        console.error('Error al cargar pedidos:', error);
      }

    });
  }

  actualizarEstado(pedido: any): void {

    this.http.put(
      `/api/pedido/update/${pedido.idpedido}`,
      pedido
    ).subscribe({

      next: () => {
        alert('Estado actualizado');
        this.cargarPedidos();
      },

      error: (error) => {
        console.error('Error al actualizar pedido:', error);
        alert('No se pudo actualizar el estado');
      }

    });
  }

  cerrarSesion(): void {
    this.router.navigate(['/login']);
  }
}