package com.so.pedidos.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.*;

import com.so.pedidos.Service.ProductoService;
import com.so.pedidos.model.Producto;

@RestController
@RequestMapping("/producto")
public class ProductoContoller {

    private final ProductoService productoService;

    public ProductoContoller(
            ProductoService productoService) {

        this.productoService = productoService;
    }

    // LISTAR PRODUCTOS

    @GetMapping("/listproductos")
    public List<Producto> getAllProducto() {

        return productoService.getAllProductos();
    }

    // BUSCAR PRODUCTO

    @GetMapping("/{par_id}")
    public Producto getProductoById(
            @PathVariable("par_id") int productoId) {

        Optional<Producto> productoOptional =
                productoService.findById(productoId);

        return productoOptional.orElse(null);
    }

    // CREAR PRODUCTO

    @PostMapping("/create_producto")
    public Producto createProducto(
            @RequestBody Producto producto,
            @RequestParam String usuario) {

        if (producto != null &&
                productoService
                        .findById(producto.getIdproducto())
                        .isPresent()) {

            return null;
        }

        return productoService.crearProducto(
                producto,
                usuario
        );
    }

    // ACTUALIZAR PRODUCTO

    @PutMapping("/update/{par_id}")
    public Producto updateProducto(
            @PathVariable("par_id") int productoId,
            @RequestBody Producto producto,
            @RequestParam String usuario) {

        return productoService.actualizarProducto(
                productoId,
                producto,
                usuario
        );
    }

    // ELIMINAR PRODUCTO

    @DeleteMapping("/delete_producto/{id}")
    public void deleteProducto(
            @PathVariable("id") int productoId,
            @RequestParam String usuario) {

        productoService.eliminarProducto(
                productoId,
                usuario
        );
    }
}