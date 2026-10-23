package com.so.pedidos.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.so.pedidos.Repository.ClienteRepository;
import com.so.pedidos.Repository.ProductoRepository;
import com.so.pedidos.model.Cliente;
import com.so.pedidos.model.Producto;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final ClienteRepository clienteRepository;
    private final AuditoriaService auditoriaService;

    public ProductoService(
            ProductoRepository productoRepository,
            ClienteRepository clienteRepository,
            AuditoriaService auditoriaService) {

        this.productoRepository = productoRepository;
        this.clienteRepository = clienteRepository;
        this.auditoriaService = auditoriaService;
    }

    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> findById(int id) {
        return productoRepository.findById(id);
    }

    // CREAR PRODUCTO

    public Producto crearProducto(
            Producto producto,
            String nombreUsuario) {

        Cliente responsable =
                clienteRepository.findByNombreUsuario(nombreUsuario);

        Producto productoGuardado =
                productoRepository.save(producto);

        auditoriaService.registrar(
                responsable != null
                        ? responsable.getIdCliente()
                        : null,

                nombreUsuario,

                responsable != null
                        ? responsable.getRol()
                        : "ADMIN",

                "CREATE_PRODUCT",
                "PRODUCTO",

                productoGuardado.getIdproducto(),

                null,
                null,

                "Producto \"" +
                        productoGuardado.getNombreProducto() +
                        "\" creado",

                null,

                "Nombre: " +
                        productoGuardado.getNombreProducto()
                        + ", Descripcion: " +
                        productoGuardado.getDescripcion()
                        + ", Precio: " +
                        productoGuardado.getPrecio()
        );

        return productoGuardado;
    }

    // ACTUALIZAR PRODUCTO

    public Producto actualizarProducto(
            int id,
            Producto datosNuevos,
            String nombreUsuario) {

        Producto producto =
                productoRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Producto no encontrado"
                                )
                        );

        Cliente responsable =
                clienteRepository.findByNombreUsuario(nombreUsuario);

        String valorAnterior =
                "Nombre: " + producto.getNombreProducto()
                + ", Descripcion: " + producto.getDescripcion()
                + ", Precio: " + producto.getPrecio();

        producto.setNombreProducto(
                datosNuevos.getNombreProducto()
        );

        producto.setDescripcion(
                datosNuevos.getDescripcion()
        );

        producto.setPrecio(
                datosNuevos.getPrecio()
        );

        producto.setLote(
                datosNuevos.getLote()
        );

        Producto actualizado =
                productoRepository.save(producto);

        String valorNuevo =
                "Nombre: " + actualizado.getNombreProducto()
                + ", Descripcion: " + actualizado.getDescripcion()
                + ", Precio: " + actualizado.getPrecio();

        auditoriaService.registrar(
                responsable != null
                        ? responsable.getIdCliente()
                        : null,

                nombreUsuario,

                responsable != null
                        ? responsable.getRol()
                        : "ADMIN",

                "UPDATE_PRODUCT",
                "PRODUCTO",

                actualizado.getIdproducto(),

                null,
                null,

                "Producto \"" +
                        actualizado.getNombreProducto() +
                        "\" modificado",

                valorAnterior,
                valorNuevo
        );

        return actualizado;
    }

    // ELIMINAR PRODUCTO

    public void eliminarProducto(
            int id,
            String nombreUsuario) {

        Producto producto =
                productoRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Producto no encontrado"
                                )
                        );

        Cliente responsable =
                clienteRepository.findByNombreUsuario(nombreUsuario);

        String valorAnterior =
                "Nombre: " + producto.getNombreProducto()
                + ", Descripcion: " + producto.getDescripcion()
                + ", Precio: " + producto.getPrecio();

        auditoriaService.registrar(
                responsable != null
                        ? responsable.getIdCliente()
                        : null,

                nombreUsuario,

                responsable != null
                        ? responsable.getRol()
                        : "ADMIN",

                "DELETE_PRODUCT",
                "PRODUCTO",

                producto.getIdproducto(),

                null,
                null,

                "Producto \"" +
                        producto.getNombreProducto() +
                        "\" eliminado",

                valorAnterior,
                null
        );

        productoRepository.delete(producto);
    }
}