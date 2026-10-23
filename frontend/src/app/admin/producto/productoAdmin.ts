import {
  Component,
  OnInit,
  ChangeDetectorRef
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  Router,
  RouterLink,
  RouterLinkActive
} from '@angular/router';

import { Lote, Producto } from '../admin.model';
import {
  ProductoService,
  LoteService
} from '../admin.service';

@Component({
  selector: 'app-producto-admin',
  imports: [
    CommonModule,
    FormsModule,
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './productoAdmin.html',
  styleUrl: './productoAdmin.css',
})
export class ProductoAdmin implements OnInit {

  productos: Producto[] = [];
  lotes: Lote[] = [];

  productoForms: Partial<Producto> = {};

  editando = false;

  constructor(
    private productoService: ProductoService,
    private loteService: LoteService,
    private cdr: ChangeDetectorRef,
    private router: Router
  ) {}

  ngOnInit(): void {

    this.cargarProductos();
    this.cargarLotes();

  }

  cargarProductos(): void {

    this.productoService.listar().subscribe({

      next: (data) => {

        this.productos = data;

        this.cdr.detectChanges();

      },

      error: (error) => {

        console.error(
          'Error al cargar productos:',
          error
        );

      }

    });

  }

  cargarLotes(): void {

    this.loteService.listar().subscribe({

      next: (data) => {

        this.lotes = data;

        this.cdr.detectChanges();

      },

      error: (error) => {

        console.error(
          'Error al cargar lotes:',
          error
        );

      }

    });

  }

  guardar(): void {

    if (
      !this.productoForms.nombreProducto ||
      !this.productoForms.descripcion ||
      this.productoForms.precio == null ||
      !this.productoForms.lote
    ) {

      alert(
        'Completa todos los campos del producto'
      );

      return;

    }

    if (
      this.editando &&
      this.productoForms.idproducto
    ) {

      this.actualizarProducto();

    } else {

      this.crearProducto();

    }

  }

  crearProducto(): void {

    this.productoService
      .crear(this.productoForms)
      .subscribe({

        next: () => {

          alert('Producto creado correctamente');

          this.cargarProductos();

          this.cancelar();

        },

        error: (error) => {

          console.error(
            'Error al crear producto:',
            error
          );

          alert(
            'No se pudo crear el producto'
          );

        }

      });

  }

  actualizarProducto(): void {

    if (!this.productoForms.idproducto) {
      return;
    }

    this.productoService
      .actualizar(
        this.productoForms.idproducto,
        this.productoForms
      )
      .subscribe({

        next: () => {

          alert(
            'Producto actualizado correctamente'
          );

          this.cargarProductos();

          this.cancelar();

        },

        error: (error) => {

          console.error(
            'Error al actualizar producto:',
            error
          );

          alert(
            'No se pudo actualizar el producto'
          );

        }

      });

  }

  editar(producto: Producto): void {

    this.productoForms = {
      ...producto
    };

    this.editando = true;

    this.cdr.detectChanges();

  }

  eliminar(id: number): void {

    const confirmar = confirm(
      '¿Seguro que deseas eliminar este producto?'
    );

    if (!confirmar) {
      return;
    }

    this.productoService
      .eliminar(id)
      .subscribe({

        next: () => {

          alert(
            'Producto eliminado correctamente'
          );

          this.cargarProductos();

        },

        error: (error) => {

          console.error(
            'Error al eliminar producto:',
            error
          );

          alert(
            'No se pudo eliminar el producto'
          );

        }

      });

  }

  cancelar(): void {

    this.productoForms = {};

    this.editando = false;

    this.cdr.detectChanges();

  }

  cerrarSesion(): void {

    localStorage.removeItem('usuario');

    this.router.navigate(['/login']);

  }

}