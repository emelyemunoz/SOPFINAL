package com.so.pedidos.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.so.pedidos.Repository.DetallePedidoRepository;
import com.so.pedidos.model.Detalle_pedido;
import com.so.pedidos.model.Pedido;

@Service
public class FacturaService {

    private final String carpetaFacturas = "/home/emely/facturas/";

    private final DetallePedidoRepository detallePedidoRepository;

    public FacturaService(
            DetallePedidoRepository detallePedidoRepository) {

        this.detallePedidoRepository = detallePedidoRepository;
    }

    public String generarFactura(Pedido pedido) {

        String nombreArchivo =
                "factura-pedido-" + pedido.getIdpedido() + ".txt";

        String rutaCompleta =
                carpetaFacturas + nombreArchivo;

        try {

            File carpeta = new File(carpetaFacturas);

            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            // Buscar los productos del pedido directamente en MySQL
            List<Detalle_pedido> detalles =
                    detallePedidoRepository
                            .findByPedido_Idpedido(
                                    pedido.getIdpedido()
                            );

            FileWriter archivo =
                    new FileWriter(rutaCompleta);

            archivo.write(
                    "====================================\n"
            );
            archivo.write(
                    "          FACTURA DE PEDIDO\n"
            );
            archivo.write(
                    "====================================\n\n"
            );

            archivo.write(
                    "Pedido #: "
                    + pedido.getIdpedido()
                    + "\n"
            );

            archivo.write(
                    "Fecha: "
                    + pedido.getFecha()
                    + "\n"
            );

            archivo.write(
                    "Estado: "
                    + pedido.getEstado()
                    + "\n"
            );

            if (pedido.getCliente() != null) {

                archivo.write(
                        "Cliente: "
                        + pedido.getCliente()
                                .getNombreCompleto()
                        + "\n"
                );

                archivo.write(
                        "Correo: "
                        + pedido.getCliente()
                                .getCorreoElectronico()
                        + "\n"
                );
            }

            archivo.write("\n");
            archivo.write(
                    "------------------------------------\n"
            );
            archivo.write("PRODUCTOS\n");
            archivo.write(
                    "------------------------------------\n"
            );

            if (detalles != null && !detalles.isEmpty()) {

                for (Detalle_pedido detalle : detalles) {

                    if (detalle.getProducto() != null) {

                        archivo.write(
                                "Producto: "
                                + detalle.getProducto()
                                        .getNombreProducto()
                                + "\n"
                        );
                    }

                    archivo.write(
                            "Cantidad: "
                            + detalle.getCantidad()
                            + "\n"
                    );

                    archivo.write(
                            "Precio unitario: Q"
                            + String.format(
                                    "%.2f",
                                    detalle.getPrecioUnitario()
                            )
                            + "\n"
                    );

                    archivo.write(
                            "Subtotal: Q"
                            + String.format(
                                    "%.2f",
                                    detalle.getSubTotal()
                            )
                            + "\n"
                    );

                    archivo.write(
                            "------------------------------------\n"
                    );
                }

            } else {

                archivo.write(
                        "No se encontraron detalles del pedido.\n"
                );
            }

            archivo.write("\n");

            archivo.write(
                    "TOTAL: Q"
                    + String.format(
                            "%.2f",
                            pedido.getTotal()
                    )
                    + "\n"
            );

            archivo.write(
                    "\nGracias por su compra.\n"
            );

            archivo.close();

            System.out.println(
                    "Factura generada: "
                    + rutaCompleta
            );

            return rutaCompleta;

        } catch (IOException e) {

            throw new RuntimeException(
                    "Error al generar la factura: "
                    + e.getMessage()
            );
        }
    }
}