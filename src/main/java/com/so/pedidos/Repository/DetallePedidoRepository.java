package com.so.pedidos.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.so.pedidos.model.Detalle_pedido;

public interface DetallePedidoRepository
        extends JpaRepository<Detalle_pedido, Integer> {

    List<Detalle_pedido> findByPedido_Idpedido(int idpedido);
}