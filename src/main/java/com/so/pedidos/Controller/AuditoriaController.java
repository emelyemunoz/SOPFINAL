package com.so.pedidos.Controller;

import com.so.pedidos.model.auditoria;
import com.so.pedidos.Service.AuditoriaService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auditoria")
@CrossOrigin(origins = "*")
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    public AuditoriaController(AuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    @GetMapping
    public List<auditoria> listar() {
        return auditoriaService.listar();
    }
}