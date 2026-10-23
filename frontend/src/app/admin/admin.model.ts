export interface Lote {
  idlote: number;
  estilo: string;
  stock: number;
  talla: number;
}

export interface Producto {
  idproducto: number;
  nombreProducto: string;
  descripcion: string;
  precio: number;
  lote: Lote;
}

export interface auditoria {

  idAuditoria: number;

  idUsuario: number | null;

  usuario: string;

  rol: string;

  accion: string;

  entidad: string;

  idEntidad: number | null;

  idUsuarioAfectado: number | null;

  usuarioAfectado: string | null;

  descripcion: string;

  valorAnterior: string | null;

  valorNuevo: string | null;

  fechaHora: string;

}