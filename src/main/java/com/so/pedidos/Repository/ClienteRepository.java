package com.so.pedidos.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.so.pedidos.model.Cliente;

public interface ClienteRepository
        extends JpaRepository<Cliente, Integer> {

    Cliente findByCorreoElectronico(String correoElectronico);

    Cliente findByNombreUsuario(String nombreUsuario);
}