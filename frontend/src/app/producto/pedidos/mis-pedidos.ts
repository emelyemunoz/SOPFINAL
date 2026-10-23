import {
  Component,
  OnInit,
  Inject,
  PLATFORM_ID,
  ChangeDetectorRef
} from '@angular/core';

import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-mis-pedidos',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './mis-pedidos.html',
  styleUrl: './mis-pedidos.css'
})
export class MisPedidos implements OnInit {

  pedidos: any[] = [];

  constructor(
    private http: HttpClient,
    private router: Router,
    private cdr: ChangeDetectorRef,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {

    if (!isPlatformBrowser(this.platformId)) {
      return;
    }

    const datosUsuario = localStorage.getItem('usuario');

    if (!datosUsuario) {
      this.router.navigate(['/login']);
      return;
    }

    const usuario = JSON.parse(datosUsuario);

    this.cargarPedidos(usuario.idCliente);
  }

  cargarPedidos(idCliente: number): void {

    this.http.get<any[]>(
      '/api/pedido/listpedidos'
    ).subscribe({

      next: (respuesta) => {

        console.log('Pedidos recibidos:', respuesta);
        console.log('Cliente actual:', idCliente);

        this.pedidos = respuesta.filter(
          pedido => pedido.cliente?.idCliente === idCliente
        );

        console.log('Pedidos filtrados:', this.pedidos);

        this.cdr.detectChanges();
      },

      error: (error) => {
        console.error('Error al cargar pedidos:', error);
      }

    });
  }

  descargarFactura(pedido: any): void {

    const datosUsuario = localStorage.getItem('usuario');

    if (!datosUsuario) {
      this.router.navigate(['/login']);
      return;
    }

    const usuario = JSON.parse(datosUsuario);

    this.http.get(
      `/api/factura/descargar/${pedido.idpedido}/${usuario.idCliente}`,
      {
        responseType: 'blob'
      }
    ).subscribe({

      next: (archivo) => {

        const url = window.URL.createObjectURL(archivo);

        const enlace = document.createElement('a');

        enlace.href = url;
        enlace.download =
          `factura-pedido-${pedido.idpedido}.txt`;

        enlace.click();

        window.URL.revokeObjectURL(url);
      },

      error: (error) => {

        console.error(
          'Error al descargar factura:',
          error
        );

        alert(
          'No se pudo descargar la factura.'
        );
      }

    });
  }

  volverCatalogo(): void {
    this.router.navigate(['/catalogo']);
  }
}