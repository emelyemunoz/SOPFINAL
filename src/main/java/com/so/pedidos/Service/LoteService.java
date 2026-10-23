package com.so.pedidos.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.so.pedidos.Repository.ClienteRepository;
import com.so.pedidos.Repository.LoteRepository;
import com.so.pedidos.model.Cliente;
import com.so.pedidos.model.Lote;

@Service
public class LoteService {

    private final LoteRepository loteRepository;
    private final ClienteRepository clienteRepository;
    private final AuditoriaService auditoriaService;

    public LoteService(
            LoteRepository loteRepository,
            ClienteRepository clienteRepository,
            AuditoriaService auditoriaService) {

        this.loteRepository = loteRepository;
        this.clienteRepository = clienteRepository;
        this.auditoriaService = auditoriaService;
    }

    public List<Lote> getAlllotes() {
        return loteRepository.findAll();
    }

    public Optional<Lote> findById(int id) {
        return loteRepository.findById(id);
    }

    // CREAR LOTE
    public Lote crearLote(
            Lote lote,
            String nombreUsuario) {

        Cliente responsable =
                clienteRepository.findByNombreUsuario(nombreUsuario);

        Lote loteGuardado =
                loteRepository.save(lote);

        auditoriaService.registrar(
                responsable != null
                        ? responsable.getIdCliente()
                        : null,

                nombreUsuario,

                responsable != null
                        ? responsable.getRol()
                        : "ADMIN",

                "CREATE_LOTE",
                "LOTE",

                loteGuardado.getIdlote(),

                null,
                null,

                "Lote creado",

                null,

                "Estilo: " + loteGuardado.getEstilo()
                        + ", Stock: " + loteGuardado.getStock()
                        + ", Talla: " + loteGuardado.getTalla()
        );

        return loteGuardado;
    }

    // ACTUALIZAR LOTE
    public Lote actualizarLote(
            int id,
            Lote datosNuevos,
            String nombreUsuario) {

        Lote lote =
                loteRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Lote no encontrado"
                                )
                        );

        Cliente responsable =
                clienteRepository.findByNombreUsuario(nombreUsuario);

        String valorAnterior =
                "Estilo: " + lote.getEstilo()
                        + ", Stock: " + lote.getStock()
                        + ", Talla: " + lote.getTalla();

        lote.setEstilo(datosNuevos.getEstilo());
        lote.setStock(datosNuevos.getStock());
        lote.setTalla(datosNuevos.getTalla());

        Lote actualizado =
                loteRepository.save(lote);

        String valorNuevo =
                "Estilo: " + actualizado.getEstilo()
                        + ", Stock: " + actualizado.getStock()
                        + ", Talla: " + actualizado.getTalla();

        auditoriaService.registrar(
                responsable != null
                        ? responsable.getIdCliente()
                        : null,

                nombreUsuario,

                responsable != null
                        ? responsable.getRol()
                        : "ADMIN",

                "UPDATE_LOTE",
                "LOTE",

                actualizado.getIdlote(),

                null,
                null,

                "Lote modificado",

                valorAnterior,
                valorNuevo
        );

        return actualizado;
    }

    // ELIMINAR LOTE
    public void eliminarLote(
            int id,
            String nombreUsuario) {

        Lote lote =
                loteRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Lote no encontrado"
                                )
                        );

        Cliente responsable =
                clienteRepository.findByNombreUsuario(nombreUsuario);

        String valorAnterior =
                "Estilo: " + lote.getEstilo()
                        + ", Stock: " + lote.getStock()
                        + ", Talla: " + lote.getTalla();

        auditoriaService.registrar(
                responsable != null
                        ? responsable.getIdCliente()
                        : null,

                nombreUsuario,

                responsable != null
                        ? responsable.getRol()
                        : "ADMIN",

                "DELETE_LOTE",
                "LOTE",

                lote.getIdlote(),

                null,
                null,

                "Lote eliminado",

                valorAnterior,
                null
        );

        loteRepository.delete(lote);
    }
}
