package com.so.pedidos.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.so.pedidos.Repository.DetallePedidoRepository;
import com.so.pedidos.Repository.PedidoRepository;
import com.so.pedidos.Repository.ProductoRepository;
import com.so.pedidos.model.Detalle_pedido;
import com.so.pedidos.model.Pedido;
import com.so.pedidos.model.Producto;

@Service
public class DetallePedidoService {

    private final DetallePedidoRepository detallepedidoRepository;
    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final FacturaService facturaService;

    public DetallePedidoService(
            DetallePedidoRepository detallepedidoRepository,
            PedidoRepository pedidoRepository,
            ProductoRepository productoRepository,
            FacturaService facturaService) {

        this.detallepedidoRepository = detallepedidoRepository;
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
        this.facturaService = facturaService;
    }

    public List<Detalle_pedido> getAlldetallePedidos() {
        return detallepedidoRepository.findAll();
    }

    public Optional<Detalle_pedido> findById(int par_id) {
        return detallepedidoRepository.findById(par_id);
    }

    public Detalle_pedido save(Detalle_pedido par_detallepedido) {

        
        int idPedido =
                par_detallepedido.getPedido().getIdpedido();

        Pedido pedidoCompleto =
                pedidoRepository.findById(idPedido)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "No se encontró el pedido: " + idPedido
                                )
                        );

      
        int idProducto =
                par_detallepedido.getProducto().getIdproducto();

        Producto productoCompleto =
                productoRepository.findById(idProducto)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "No se encontró el producto: " + idProducto
                                )
                        );

       
        par_detallepedido.setPedido(pedidoCompleto);
        par_detallepedido.setProducto(productoCompleto);

        
        Detalle_pedido detalleGuardado =
                detallepedidoRepository.save(par_detallepedido);

        
        try {

            facturaService.generarFactura(pedidoCompleto);

            System.out.println(
                    "Factura actualizada correctamente para el pedido #"
                    + idPedido
            );

        } catch (Exception e) {

            System.err.println(
                    "No se pudo actualizar la factura: "
                    + e.getMessage()
            );
        }

        return detalleGuardado;
    }

    public void delete(Detalle_pedido par_detallepedido) {
        detallepedidoRepository.delete(par_detallepedido);
    }
}