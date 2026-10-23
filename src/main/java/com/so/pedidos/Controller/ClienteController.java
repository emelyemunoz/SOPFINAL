package com.so.pedidos.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.so.pedidos.Service.ClienteService;
import com.so.pedidos.model.Cliente;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // LISTAR USUARIOS
    @GetMapping
    public List<Cliente> listarClientes() {
        return clienteService.listarClientes();
    }

    // AGREGAR USUARIO
    @PostMapping
    public Cliente guardarCliente(@RequestBody Cliente cliente) {
        return clienteService.guardarCliente(cliente);
    }

    // LOGIN
    @PostMapping("/login")
    public Cliente iniciarSesion(@RequestBody Cliente cliente) {
        return clienteService.iniciarSesion(
                cliente.getCorreoElectronico(),
                cliente.getPassword()
        );
    }

    // BUSCAR USUARIO
    @GetMapping("/{id}")
    public Cliente buscarCliente(@PathVariable Integer id) {
        return clienteService.buscarCliente(id);
    }

    // EDITAR USUARIO
    @PutMapping("/{id}")
    public Cliente actualizarCliente(
            @PathVariable Integer id,
            @RequestBody Cliente cliente) {

        return clienteService.actualizarCliente(
                id,
                cliente
        );
    }

    // ELIMINAR USUARIO
    @DeleteMapping("/{id}")
    public void eliminarCliente(@PathVariable Integer id) {
        clienteService.eliminarCliente(id);
    }
}