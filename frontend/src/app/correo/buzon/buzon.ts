import {
  Component,
  OnInit,
  ChangeDetectorRef
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

import {
  CorreoService,
  CorreoBuzon
} from '../correo.service';

@Component({
  selector: 'app-buzon',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './buzon.html',
  styleUrl: './buzon.css'
})
export class Buzon implements OnInit {

  correos: CorreoBuzon[] = [];
  correoSeleccionado: CorreoBuzon | null = null;

  cargando = true;
  error = '';

  constructor(
    private correoService: CorreoService,
    private router: Router,
    private cd: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cargarCorreos();
  }

  cargarCorreos(): void {

    this.cargando = true;
    this.error = '';

    this.correoService.obtenerCorreos().subscribe({

      next: (correos) => {
        this.correos = correos;
        this.cargando = false;

        this.cd.detectChanges();
      },

      error: (error) => {
        console.error(
          'Error al cargar correos:',
          error
        );

        this.error = 'No se pudo cargar el buzón.';
        this.cargando = false;

        this.cd.detectChanges();
      }

    });
  }

  abrirCorreo(id: number): void {

    this.error = '';

    this.correoService.obtenerCorreo(id).subscribe({

      next: (correo) => {
        this.correoSeleccionado = correo;

        this.cd.detectChanges();
      },

      error: (error) => {
        console.error(
          'Error al abrir correo:',
          error
        );

        this.error = 'No se pudo abrir el correo.';

        this.cd.detectChanges();
      }

    });
  }

  cerrarCorreo(): void {
    this.correoSeleccionado = null;
  }

  volverCatalogo(): void {
    this.router.navigate(['/catalogo']);
  }
}