package com.so.pedidos.Controller;

import com.so.pedidos.Service.CorreoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/correo")
public class CorreoController {

    private final CorreoService correoService;

    public CorreoController(CorreoService correoService) {
        this.correoService = correoService;
    }

    @GetMapping("/prueba")
    public String pruebaCorreo() {

        correoService.enviarCorreo(
                "emely@grupo1.os",
                "Prueba desde Spring Boot",
                "Este correo fue enviado desde Spring Boot utilizando nuestro servidor Postfix."
        );

        return "Correo enviado correctamente";
    }
}