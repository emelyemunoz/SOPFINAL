import {
  Component,
  OnInit,
  OnDestroy,
  ChangeDetectorRef,
  Inject,
  PLATFORM_ID
} from '@angular/core';

import { isPlatformBrowser } from '@angular/common';

import { Router } from '@angular/router';
import { ProductoService } from '../producto.service';

import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';

@Component({
  selector: 'app-catalogo',
  standalone: true,
  imports: [
    MatToolbarModule,
    MatButtonModule
  ],
  templateUrl: './catalogo.html',
  styleUrl: './catalogo.css'
})
export class Catalogo implements OnInit, OnDestroy {

  productos: any[] = [];
  carrito: any[] = [];

  constructor(
    private productoService: ProductoService,
    private cd: ChangeDetectorRef,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    this.cargarProductos();
    this.cargarCarrito();
  }

  get cantidadCarrito(): number {
    return this.carrito.length;
  }

  cargarProductos(): void {

    this.productoService.obtenerProductos().subscribe({

      next: (respuesta: any[]) => {
        this.productos = respuesta;
        this.cd.detectChanges();
      },

      error: (error: any) => {
        console.error('Error al cargar productos:', error);
      }

    });
  }

  agregarAlCarrito(producto: any): void {

    if (!isPlatformBrowser(this.platformId)) {
      return;
    }

    const existente = this.carrito.find(
      item => item.idproducto === producto.idproducto
    );

    if (existente) {
      existente.cantidad++;
    } else {
      this.carrito.push({
        ...producto,
        cantidad: 1
      });
    }

    localStorage.setItem(
      'carrito',
      JSON.stringify(this.carrito)
    );

    alert(producto.nombreProducto + ' agregado al carrito');
  }

  cargarCarrito(): void {

    if (isPlatformBrowser(this.platformId)) {

      const carritoGuardado = localStorage.getItem('carrito');

      if (carritoGuardado) {
        this.carrito = JSON.parse(carritoGuardado);
      }

    }
  }

  irCarrito(): void {
    this.router.navigate(['/carrito']);
  }

  irMisPedidos(): void {
    this.router.navigate(['/mis-pedidos']);
  }

  irBuzon(): void {
    this.router.navigate(['/buzon']);
  }

  cerrarSesion(): void {

    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem('carrito');
      localStorage.removeItem('usuario');
    }

    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
  }
}