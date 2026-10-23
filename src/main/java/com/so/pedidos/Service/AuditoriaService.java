package com.so.pedidos.Service;

import com.so.pedidos.model.auditoria;
import com.so.pedidos.Repository.AuditoriaRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;

    public AuditoriaService(AuditoriaRepository auditoriaRepository) {
        this.auditoriaRepository = auditoriaRepository;
    }

    public List<auditoria> listar() {
        return auditoriaRepository.findAll();
    }

    public auditoria registrar(
            Integer idUsuario,
            String usuario,
            String rol,
            String accion,
            String entidad,
            Integer idEntidad,
            Integer idUsuarioAfectado,
            String usuarioAfectado,
            String descripcion,
            String valorAnterior,
            String valorNuevo) {

        auditoria registro = new auditoria();

        registro.setIdUsuario(idUsuario);
        registro.setUsuario(usuario);
        registro.setRol(rol);
        registro.setAccion(accion);
        registro.setEntidad(entidad);
        registro.setIdEntidad(idEntidad);
        registro.setIdUsuarioAfectado(idUsuarioAfectado);
        registro.setUsuarioAfectado(usuarioAfectado);
        registro.setDescripcion(descripcion);
        registro.setValorAnterior(valorAnterior);
        registro.setValorNuevo(valorNuevo);

        return auditoriaRepository.save(registro);
    }
}