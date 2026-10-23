
package com.so.pedidos.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.so.pedidos.Service.BuzonService;
import com.so.pedidos.model.CorreoBuzon;

@RestController
@RequestMapping("/correo")
public class BuzonController {

    private final BuzonService buzonService;

    public BuzonController(BuzonService buzonService) {
        this.buzonService = buzonService;
    }

    @GetMapping("/buzon/cliente/{idCliente}")
    public List<CorreoBuzon> obtenerBuzon(
            @PathVariable Integer idCliente) throws Exception {

        return buzonService.obtenerCorreos(idCliente);
    }

    @GetMapping("/buzon/cliente/{idCliente}/{idCorreo}")
    public CorreoBuzon obtenerCorreo(
            @PathVariable Integer idCliente,
            @PathVariable int idCorreo) throws Exception {

        return buzonService.obtenerCorreoPorId(
                idCliente,
                idCorreo
        );
    }
}