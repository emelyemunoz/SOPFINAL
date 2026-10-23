import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';

import { auditoria } from '../admin.model';
import { AuditoriaService } from '../admin.service';

@Component({
  selector: 'app-auditoria',
  imports: [
    CommonModule,
    FormsModule,
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './auditoria.html',
  styleUrl: './auditoria.css'
})
export class AuditoriaAdmin implements OnInit {

  auditorias: auditoria[] = [];

  auditoriasFiltradas: auditoria[] = [];

  busqueda: string = '';

  constructor(
    private auditoriaService: AuditoriaService,
    private cdr: ChangeDetectorRef,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarAuditoria();
  }

  cargarAuditoria(): void {

    this.auditoriaService.listar().subscribe({

      next: (data) => {

        this.auditorias = data;

        this.auditoriasFiltradas = data;

        this.cdr.detectChanges();
      },

      error: (error) => {
        console.error('Error al cargar auditoría:', error);
      }

    });

  }

  buscarUsuario(): void {

    const texto = this.busqueda
      .trim()
      .toLowerCase();

    if (texto === '') {

      this.auditoriasFiltradas = this.auditorias;

      return;
    }

    this.auditoriasFiltradas =
      this.auditorias.filter(registro => {

        const usuario =
          registro.usuario?.toLowerCase() || '';

        const usuarioAfectado =
          registro.usuarioAfectado?.toLowerCase() || '';

        return usuario.includes(texto) ||
               usuarioAfectado.includes(texto);

      });

  }

  limpiarBusqueda(): void {

    this.busqueda = '';

    this.auditoriasFiltradas =
      this.auditorias;

  }

  cerrarSesion(): void {

    localStorage.removeItem('usuario');

    this.router.navigate(['/login']);

  }

}