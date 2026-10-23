package com.so.pedidos.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.so.pedidos.Repository.ClienteRepository;
import com.so.pedidos.Repository.PedidoRepository;
import com.so.pedidos.model.Cliente;
import com.so.pedidos.model.Pedido;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final CorreoService correoService;
    private final FacturaService facturaService;

    public PedidoService(
            PedidoRepository pedidoRepository,
            ClienteRepository clienteRepository,
            CorreoService correoService,
            FacturaService facturaService) {

        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.correoService = correoService;
        this.facturaService = facturaService;
    }

    public List<Pedido> getAllPedidos() {
        return (List<Pedido>) pedidoRepository.findAll();
    }

    public Optional<Pedido> findById(int par_id) {
        return pedidoRepository.findById(par_id);
    }

    public Pedido crearPedido(Pedido par_pedido) {

        // Obtener el cliente completo desde MySQL
        if (par_pedido.getCliente() == null) {
            throw new RuntimeException("El pedido no contiene un cliente");
        }

        int idCliente = par_pedido.getCliente().getIdCliente();

        Cliente clienteCompleto = clienteRepository.findById(idCliente)
                .orElseThrow(() ->
                        new RuntimeException(
                                "No se encontró el cliente con ID: " + idCliente
                        )
                );

        // Asignar al pedido el cliente completo obtenido de la BD
        par_pedido.setCliente(clienteCompleto);

        // Guardar el pedido
        Pedido pedidoGuardado = pedidoRepository.save(par_pedido);

        // Generar factura automáticamente
        try {
            facturaService.generarFactura(pedidoGuardado);

            System.out.println(
                    "Factura generada para el pedido #"
                    + pedidoGuardado.getIdpedido()
            );

        } catch (Exception e) {

            System.err.println(
                    "El pedido fue guardado, pero no se pudo generar la factura: "
                    + e.getMessage()
            );
        }

        // Enviar correo de confirmación
        String correo = clienteCompleto.getCorreoElectronico();

        if (correo != null && !correo.isBlank()) {

            String asunto =
                    "Confirmacion de pedido #" + pedidoGuardado.getIdpedido();

            String mensaje =
                    "Hola " + clienteCompleto.getNombreCompleto() + ",\n\n"
                    + "Tu pedido fue registrado correctamente.\n\n"
                    + "Numero de pedido: "
                    + pedidoGuardado.getIdpedido() + "\n"
                    + "Fecha: "
                    + pedidoGuardado.getFecha() + "\n"
                    + "Estado: "
                    + pedidoGuardado.getEstado() + "\n"
                    + "Total: Q"
                    + String.format("%.2f", pedidoGuardado.getTotal())
                    + "\n\n"
                    + "Gracias por realizar tu pedido.";

            try {

                correoService.enviarCorreo(
                        correo,
                        asunto,
                        mensaje
                );

                System.out.println(
                        "Correo de confirmacion enviado a: " + correo
                );

            } catch (Exception e) {

                System.err.println(
                        "El pedido fue guardado, pero no se pudo enviar el correo: "
                        + e.getMessage()
                );
            }

        } else {

            System.err.println(
                    "El cliente no tiene correo electronico registrado."
            );
        }

        return pedidoGuardado;
    }

    public Pedido save(Pedido par_pedido) {
        return pedidoRepository.save(par_pedido);
    }

    public void delete(Pedido par_pedido) {
        pedidoRepository.delete(par_pedido);
    }
}