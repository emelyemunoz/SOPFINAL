package com.so.pedidos.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.so.pedidos.Service.PedidoService;
import com.so.pedidos.model.Cliente;
import com.so.pedidos.model.Pedido;

@RestController
@RequestMapping("/pedido")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/listpedidos")
    public List<Pedido> getAllPedido() {
        return pedidoService.getAllPedidos();
    }

    @GetMapping("/{par_id}")
    public Pedido getPedidoById(@PathVariable("par_id") int pedidoId) {

        Optional<Pedido> pedidoOptional = pedidoService.findById(pedidoId);

        return pedidoOptional.orElse(null);
    }

    @PostMapping("/create_pedido")
    public Pedido createPedido(@RequestBody Pedido par_Pedido) {

        if (par_Pedido != null
                && pedidoService.findById(par_Pedido.getIdpedido()).isPresent()) {

            return null;

        } else {

            // Guarda el pedido y envía el correo de confirmación
            return pedidoService.crearPedido(par_Pedido);
        }
    }

    @PutMapping("/update/{par_id}")
    public Pedido updatePedido(
            @PathVariable("par_id") int pedidoId,
            @RequestBody Pedido updatedpedido) {

        Optional<Pedido> pedidoOptional = pedidoService.findById(pedidoId);

        if (pedidoOptional.isPresent()) {

            Pedido pedido = pedidoOptional.get();

            pedido.setFecha(updatedpedido.getFecha());
            pedido.setEstado(updatedpedido.getEstado());
            pedido.setTotal(updatedpedido.getTotal());
            pedido.setCliente(updatedpedido.getCliente());

            // Actualiza sin mandar otra confirmación de compra
            return pedidoService.save(pedido);

        } else {

            return null;
        }
    }

    @PatchMapping("/update_partial_pedido/{par_id}")
    public Pedido partialUpdatePedido(
            @PathVariable int par_id,
            @RequestBody Map<String, Object> updates) {

        Optional<Pedido> pedidoOptional = pedidoService.findById(par_id);

        if (pedidoOptional.isPresent()) {

            Pedido pedido = pedidoOptional.get();

            updates.forEach((key, value) -> {

                switch (key) {

                    case "fecha":
                        pedido.setFecha((String) value);
                        break;

                    case "estado":
                        pedido.setEstado((String) value);
                        break;

                    case "total":
                        pedido.setTotal(Double.parseDouble((String) value));
                        break;

                    case "cliente":
                        pedido.setCliente((Cliente) value);
                        break;
                }
            });

            // Actualiza sin mandar otra confirmación de compra
            return pedidoService.save(pedido);

        } else {

            return null;
        }
    }

    @DeleteMapping("/delete_pedido/{id}")
    public void deletePedido(@PathVariable("id") int pedidoId) {

        Optional<Pedido> pedidoOptional = pedidoService.findById(pedidoId);

        if (pedidoOptional.isPresent()) {
            pedidoService.delete(pedidoOptional.get());
        }
    }
}