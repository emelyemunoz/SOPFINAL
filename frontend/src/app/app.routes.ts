import { Routes } from '@angular/router';

import { Registro } from './clientes/registro/registro';
import { Login } from './clientes/login/login';
import { Catalogo } from './producto/catalogo/catalogo';
import { Carro } from './producto/carro/carro';
import { ProductoAdmin } from './admin/producto/productoAdmin';
import { LoteAdmin } from './admin/lote/loteAdmin';
import { MisPedidos } from './producto/pedidos/mis-pedidos';
import { PedidoAdmin } from './admin/pedido/pedidoAdmin';
import { AuditoriaAdmin } from './admin/auditoria/auditoria';
import { Buzon } from './correo/buzon/buzon';

export const routes: Routes = [
  { path: 'registro', component: Registro },
  { path: 'login', component: Login },
  { path: 'admin/auditoria', component: AuditoriaAdmin },
  { path: 'catalogo', component: Catalogo },
  { path: 'carrito', component: Carro },
  { path: 'admin/pedido', component: PedidoAdmin },
  { path: 'admin/producto', component: ProductoAdmin },
  { path: 'admin/lote', component: LoteAdmin },
  { path: 'mis-pedidos', component: MisPedidos },

  
  { path: 'buzon', component: Buzon },

  { path: '', redirectTo: 'login', pathMatch: 'full' },
];