package com.so.pedidos.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.so.pedidos.Repository.ClienteRepository;
import com.so.pedidos.model.Cliente;
import com.so.pedidos.model.CorreoBuzon;

import jakarta.mail.Address;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Store;

@Service
public class BuzonService {

    @Value("${correo.imap.host}")
    private String host;

    @Value("${correo.imap.port}")
    private String port;

    @Value("${correo.imap.usuario}")
    private String usuario;

    @Value("${correo.imap.password}")
    private String password;

    private final ClienteRepository clienteRepository;

    public BuzonService(
            ClienteRepository clienteRepository) {

        this.clienteRepository = clienteRepository;
    }

    private Session crearSession() {

        Properties propiedades = new Properties();

        propiedades.put(
                "mail.store.protocol",
                "imaps"
        );

        propiedades.put(
                "mail.imaps.host",
                host
        );

        propiedades.put(
                "mail.imaps.port",
                port
        );

        propiedades.put(
                "mail.imaps.ssl.enable",
                "true"
        );

        return Session.getInstance(propiedades);
    }

    /*
     * Verifica si el mensaje fue enviado
     * al correo del cliente.
     */
    private boolean perteneceAlCliente(
            Message mensaje,
            String correoCliente) throws Exception {

        Address[] destinatarios =
                mensaje.getAllRecipients();

        if (destinatarios == null) {
            return false;
        }

        for (Address direccion : destinatarios) {

            if (direccion != null) {

                String texto =
                        direccion.toString()
                                .toLowerCase();

                if (texto.contains(
                        correoCliente.toLowerCase())) {

                    return true;
                }
            }
        }

        return false;
    }

    public List<CorreoBuzon> obtenerCorreos(
            Integer idCliente) throws Exception {

        List<CorreoBuzon> correos =
                new ArrayList<>();

        Cliente cliente =
                clienteRepository
                        .findById(idCliente)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Cliente no encontrado"
                                )
                        );

        String correoCliente =
                cliente.getCorreoElectronico();

        Store store = null;
        Folder inbox = null;

        try {

            Session session =
                    crearSession();

            store =
                    session.getStore("imaps");

            /*
             * Seguimos entrando al buzón IMAP
             * configurado actualmente.
             */
            store.connect(
                    host,
                    usuario,
                    password
            );

            inbox =
                    store.getFolder("INBOX");

            inbox.open(
                    Folder.READ_ONLY
            );

            Message[] mensajes =
                    inbox.getMessages();

            SimpleDateFormat formatoFecha =
                    new SimpleDateFormat(
                            "dd/MM/yyyy HH:mm"
                    );

            for (
                    int i = mensajes.length - 1;
                    i >= 0;
                    i--
            ) {

                Message mensaje =
                        mensajes[i];

                /*
                 * IMPORTANTE:
                 * solamente muestra el mensaje
                 * si fue enviado al cliente
                 * actualmente conectado.
                 */
                if (!perteneceAlCliente(
                        mensaje,
                        correoCliente)) {

                    continue;
                }

                String remitente =
                        "Desconocido";

                if (
                        mensaje.getFrom() != null
                        &&
                        mensaje.getFrom().length > 0
                ) {

                    remitente =
                            mensaje
                                    .getFrom()[0]
                                    .toString();
                }

                String asunto =
                        mensaje.getSubject();

                if (
                        asunto == null
                        ||
                        asunto.isBlank()
                ) {

                    asunto =
                            "(Sin asunto)";
                }

                String fecha = "";

                if (
                        mensaje.getSentDate()
                        != null
                ) {

                    fecha =
                            formatoFecha.format(
                                    mensaje.getSentDate()
                            );
                }

                CorreoBuzon correo =
                        new CorreoBuzon(

                                mensaje
                                        .getMessageNumber(),

                                remitente,

                                asunto,

                                fecha,

                                ""
                        );

                correos.add(correo);
            }

        } finally {

            if (
                    inbox != null
                    &&
                    inbox.isOpen()
            ) {

                inbox.close(false);
            }

            if (
                    store != null
                    &&
                    store.isConnected()
            ) {

                store.close();
            }
        }

        return correos;
    }

    public CorreoBuzon obtenerCorreoPorId(
            Integer idCliente,
            int idCorreo) throws Exception {

        Cliente cliente =
                clienteRepository
                        .findById(idCliente)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Cliente no encontrado"
                                )
                        );

        String correoCliente =
                cliente.getCorreoElectronico();

        Store store = null;
        Folder inbox = null;

        try {

            Session session =
                    crearSession();

            store =
                    session.getStore("imaps");

            store.connect(
                    host,
                    usuario,
                    password
            );

            inbox =
                    store.getFolder("INBOX");

            inbox.open(
                    Folder.READ_ONLY
            );

            if (
                    idCorreo < 1
                    ||
                    idCorreo >
                    inbox.getMessageCount()
            ) {

                return null;
            }

            Message mensaje =
                    inbox.getMessage(idCorreo);

            /*
             * Seguridad:
             * evita que otro cliente abra
             * un correo cambiando el ID
             * manualmente en la URL.
             */
            if (!perteneceAlCliente(
                    mensaje,
                    correoCliente)) {

                return null;
            }

            String remitente =
                    "Desconocido";

            if (
                    mensaje.getFrom() != null
                    &&
                    mensaje.getFrom().length > 0
            ) {

                remitente =
                        mensaje
                                .getFrom()[0]
                                .toString();
            }

            String asunto =
                    mensaje.getSubject();

            if (
                    asunto == null
                    ||
                    asunto.isBlank()
            ) {

                asunto =
                        "(Sin asunto)";
            }

            String fecha = "";

            if (
                    mensaje.getSentDate()
                    != null
            ) {

                SimpleDateFormat formatoFecha =
                        new SimpleDateFormat(
                                "dd/MM/yyyy HH:mm"
                        );

                fecha =
                        formatoFecha.format(
                                mensaje.getSentDate()
                        );
            }

            String contenido = "";

            if (
                    mensaje.isMimeType(
                            "text/plain"
                    )
            ) {

                Object contenidoMensaje =
                        mensaje.getContent();

                if (
                        contenidoMensaje
                        != null
                ) {

                    contenido =
                            contenidoMensaje
                                    .toString();
                }

            } else {

                contenido =
                        "Este correo contiene un formato que todavía no se puede mostrar.";
            }

            return new CorreoBuzon(

                    mensaje
                            .getMessageNumber(),

                    remitente,

                    asunto,

                    fecha,

                    contenido
            );

        } finally {

            if (
                    inbox != null
                    &&
                    inbox.isOpen()
            ) {

                inbox.close(false);
            }

            if (
                    store != null
                    &&
                    store.isConnected()
            ) {

                store.close();
            }
        }
    }
}