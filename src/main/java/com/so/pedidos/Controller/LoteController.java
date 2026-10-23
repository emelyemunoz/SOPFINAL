package com.so.pedidos.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.*;

import com.so.pedidos.Service.LoteService;
import com.so.pedidos.model.Lote;

@RestController
@RequestMapping("/lote")
public class LoteController {

    private final LoteService loteService;

    public LoteController(LoteService loteService) {
        this.loteService = loteService;
    }

    @GetMapping("/listlote")
    public List<Lote> getAlllote() {
        return loteService.getAlllotes();
    }

    @GetMapping("/{par_id}")
    public Lote getLoteById(
            @PathVariable("par_id") int loteId) {

        Optional<Lote> loteOptional =
                loteService.findById(loteId);

        return loteOptional.orElse(null);
    }

    @PostMapping("/create_lote")
    public Lote createLote(
            @RequestBody Lote lote,
            @RequestParam String usuario) {

        return loteService.crearLote(
                lote,
                usuario
        );
    }

    @PutMapping("/update/{par_id}")
    public Lote updateLote(
            @PathVariable("par_id") int loteId,
            @RequestBody Lote lote,
            @RequestParam String usuario) {

        return loteService.actualizarLote(
                loteId,
                lote,
                usuario
        );
    }

    @DeleteMapping("/delete_lote/{id}")
    public void deleteLote(
            @PathVariable("id") int loteId,
            @RequestParam String usuario) {

        loteService.eliminarLote(
                loteId,
                usuario
        );
    }
}