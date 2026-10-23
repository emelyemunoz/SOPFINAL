package com.so.pedidos.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.so.pedidos.Service.DetallePedidoService;
import com.so.pedidos.model.Detalle_pedido;
import com.so.pedidos.model.Pedido;
import com.so.pedidos.model.Producto;

@RestController
@RequestMapping("/detallepedido")
public class DetallePedidoController {

    private final DetallePedidoService detallepedidoService;

    public DetallePedidoController(DetallePedidoService detallepedidoService) {
        this.detallepedidoService = detallepedidoService;
    }

    @GetMapping("/listpedidos")
    public List<Detalle_pedido> getAllDetallePedido() {
        return detallepedidoService.getAlldetallePedidos();
    }

    @GetMapping("/{par_id}")
    public Detalle_pedido getDetallePedidoById(@PathVariable("par_id") int detallepedidoId) {
        Optional<Detalle_pedido> DetallePedidoOptional = detallepedidoService.findById(detallepedidoId);
        return DetallePedidoOptional.orElse(null);
    }

    @PostMapping("/create_detallepedido")
    public Detalle_pedido createDetallePedido(@RequestBody Detalle_pedido par_DetallePedido) {

        if (par_DetallePedido != null && detallepedidoService.findById(par_DetallePedido.getIddetalle()).isPresent()) {
            return null;
        } else {
            return detallepedidoService.save(par_DetallePedido);
        }
    }

    @PutMapping("/update/{par_id}")
    public Detalle_pedido updateDetallePedido(@PathVariable("par_id") int detallepedidoId,
            @RequestBody Detalle_pedido updateddetallepedido) {

        Optional<Detalle_pedido> detallepedidoOptional = detallepedidoService.findById(detallepedidoId);
        if (detallepedidoOptional.isPresent()) {
            Detalle_pedido detallepedido = detallepedidoOptional.get();
            detallepedido.setPrecioUnitario(updateddetallepedido.getPrecioUnitario());
            detallepedido.setCantidad(updateddetallepedido.getCantidad());
            detallepedido.setSubTotal(updateddetallepedido.getSubTotal());
            detallepedido.setPedido(updateddetallepedido.getPedido());
            detallepedido.setProducto(updateddetallepedido.getProducto());
            return detallepedidoService.save(detallepedido);
        } else {
            return null;
        }
    }

    @PatchMapping("/update_partial_detallepedido/{par_id}")
    public Detalle_pedido partialUpdateDetallePedido(@PathVariable int par_id,
            @RequestBody Map<String, Object> updates) {
        Optional<Detalle_pedido> DetallePedidoOptional = detallepedidoService.findById(par_id);

        if (DetallePedidoOptional.isPresent()) {
            Detalle_pedido detallepedido = DetallePedidoOptional.get();

            updates.forEach((key, value) -> {
                switch (key) {
                    case "precioUnitario":
                        detallepedido.setPrecioUnitario(Double.parseDouble((String) value));
                        break;
                    case "cantidad":
                        detallepedido.setCantidad(Integer.parseInt((String) value));
                        break;
                    case "subTotal":
                        detallepedido.setSubTotal(Double.parseDouble((String) value));
                        break;
                    case "pedido":
                        detallepedido.setPedido((Pedido) value);
                        break;
                    case "producto":
                        detallepedido.setProducto((Producto) value);
                        break;
                }
            });

            return detallepedidoService.save(detallepedido);
        } else {
            return null;
        }
    }

    @DeleteMapping("/delete_detallepedido/{id}")
    public void deleteDetallePedido(@PathVariable("id") int detallepedidoId) {
        Optional<Detalle_pedido> detallepedidoOptional = detallepedidoService.findById(detallepedidoId);
        if (detallepedidoOptional.isPresent()) {
            detallepedidoService.delete(detallepedidoOptional.get());
        }
    }

}
