package com.so.pedidos.Service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.so.pedidos.Repository.ClienteRepository;
import com.so.pedidos.model.Cliente;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final AuditoriaService auditoriaService;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public ClienteService(
            ClienteRepository clienteRepository,
            AuditoriaService auditoriaService) {

        this.clienteRepository = clienteRepository;
        this.auditoriaService = auditoriaService;
    }

    // LISTAR USUARIOS

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    // CREAR USUARIO

    public Cliente guardarCliente(Cliente cliente) {

        if (cliente.getRol() == null || cliente.getRol().isBlank()) {
            cliente.setRol("CLIENTE");
        }

        String passwordHash =
                passwordEncoder.encode(cliente.getPassword());

        cliente.setPassword(passwordHash);

        Cliente clienteGuardado =
                clienteRepository.save(cliente);

        auditoriaService.registrar(
                clienteGuardado.getIdCliente(),
                clienteGuardado.getNombreUsuario(),
                clienteGuardado.getRol(),
                "CREATE_USER",
                "CLIENTE",
                clienteGuardado.getIdCliente(),
                clienteGuardado.getIdCliente(),
                clienteGuardado.getNombreUsuario(),
                "Usuario creado en el sistema",
                null,
                null
        );

        return clienteGuardado;
    }

    // BUSCAR USUARIO

    public Cliente buscarCliente(Integer id) {

        return clienteRepository
                .findById(id)
                .orElse(null);
    }

    // EDITAR USUARIO

    public Cliente actualizarCliente(
            Integer id,
            Cliente datosNuevos) {

        Cliente clienteExistente =
                clienteRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Usuario no encontrado"
                                )
                        );

        String valorAnterior =
                "NombreUsuario: " + clienteExistente.getNombreUsuario()
                + ", NombreCompleto: " + clienteExistente.getNombreCompleto()
                + ", Telefono: " + clienteExistente.getTelefono()
                + ", Correo: " + clienteExistente.getCorreoElectronico()
                + ", NIT: " + clienteExistente.getNit()
                + ", Rol: " + clienteExistente.getRol();

        clienteExistente.setNombreUsuario(
                datosNuevos.getNombreUsuario()
        );

        clienteExistente.setNombreCompleto(
                datosNuevos.getNombreCompleto()
        );

        clienteExistente.setTelefono(
                datosNuevos.getTelefono()
        );

        clienteExistente.setCorreoElectronico(
                datosNuevos.getCorreoElectronico()
        );

        clienteExistente.setNit(
                datosNuevos.getNit()
        );

        clienteExistente.setRol(
                datosNuevos.getRol()
        );

        Cliente actualizado =
                clienteRepository.save(clienteExistente);

        String valorNuevo =
                "NombreUsuario: " + actualizado.getNombreUsuario()
                + ", NombreCompleto: " + actualizado.getNombreCompleto()
                + ", Telefono: " + actualizado.getTelefono()
                + ", Correo: " + actualizado.getCorreoElectronico()
                + ", NIT: " + actualizado.getNit()
                + ", Rol: " + actualizado.getRol();

        auditoriaService.registrar(
                actualizado.getIdCliente(),
                actualizado.getNombreUsuario(),
                actualizado.getRol(),
                "UPDATE_USER",
                "CLIENTE",
                actualizado.getIdCliente(),
                actualizado.getIdCliente(),
                actualizado.getNombreUsuario(),
                "Datos del usuario modificados",
                valorAnterior,
                valorNuevo
        );

        return actualizado;
    }

    // ELIMINAR USUARIO

    public void eliminarCliente(Integer id) {

        Cliente cliente =
                clienteRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Usuario no encontrado"
                                )
                        );

        String valorAnterior =
                "NombreUsuario: " + cliente.getNombreUsuario()
                + ", NombreCompleto: " + cliente.getNombreCompleto()
                + ", Telefono: " + cliente.getTelefono()
                + ", Correo: " + cliente.getCorreoElectronico()
                + ", NIT: " + cliente.getNit()
                + ", Rol: " + cliente.getRol();

        auditoriaService.registrar(
                cliente.getIdCliente(),
                cliente.getNombreUsuario(),
                cliente.getRol(),
                "DELETE_USER",
                "CLIENTE",
                cliente.getIdCliente(),
                cliente.getIdCliente(),
                cliente.getNombreUsuario(),
                "Usuario eliminado del sistema",
                valorAnterior,
                null
        );

        clienteRepository.deleteById(id);
    }

    // LOGIN

    public Cliente iniciarSesion(
            String correo,
            String password) {

        Cliente cliente =
                clienteRepository
                        .findByCorreoElectronico(correo);

        if (cliente == null ||
                !passwordEncoder.matches(
                        password,
                        cliente.getPassword())) {

            throw new RuntimeException(
                    "Correo o contraseña incorrectos"
            );
        }

        auditoriaService.registrar(
                cliente.getIdCliente(),
                cliente.getNombreUsuario(),
                cliente.getRol(),
                "LOGIN",
                "CLIENTE",
                cliente.getIdCliente(),
                cliente.getIdCliente(),
                cliente.getNombreUsuario(),
                "Inicio de sesión exitoso",
                null,
                null
        );

        return cliente;
    }
}