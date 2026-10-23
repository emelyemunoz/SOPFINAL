package com.so.pedidos.Controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.so.pedidos.Repository.PedidoRepository;
import com.so.pedidos.model.Pedido;

@RestController
@RequestMapping("/factura")
public class FacturaController {

    private final PedidoRepository pedidoRepository;

    public FacturaController(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @GetMapping("/descargar/{idPedido}/{idCliente}")
    public ResponseEntity<?> descargarFactura(
            @PathVariable int idPedido,
            @PathVariable int idCliente) {

        try {

            
            Pedido pedido = pedidoRepository.findById(idPedido)
                    .orElse(null);

            if (pedido == null) {
                return ResponseEntity
                        .notFound()
                        .build();
            }

            
            if (pedido.getCliente() == null ||
                pedido.getCliente().getIdCliente() != idCliente) {

                return ResponseEntity
                        .status(403)
                        .body("No tiene permiso para descargar esta factura.");
            }

            
            Path rutaFactura = Paths.get(
                    "/home/emely/facturas/factura-pedido-"
                    + idPedido
                    + ".txt"
            );

            
            if (!Files.exists(rutaFactura)) {
                return ResponseEntity
                        .notFound()
                        .build();
            }

            Resource archivo =
                    new UrlResource(rutaFactura.toUri());

            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"factura-pedido-"
                                    + idPedido
                                    + ".txt\""
                    )
                    .body(archivo);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .internalServerError()
                    .body("Error al descargar la factura.");
        }
    }
}