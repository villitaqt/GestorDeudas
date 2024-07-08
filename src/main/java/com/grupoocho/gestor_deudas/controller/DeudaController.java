package com.grupoocho.gestor_deudas.controller;

import com.grupoocho.gestor_deudas.model.Deuda;
import com.grupoocho.gestor_deudas.service.DeudaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/deudas")
public class DeudaController {

    private final DeudaService deudaService;

    @Autowired
    public DeudaController(DeudaService deudaService) {
        this.deudaService = deudaService;
    }

    @GetMapping
    public List<Deuda> getDeudas(){
        return deudaService.getAllDeudas();
    }

    @GetMapping("/mes")
    public List<Deuda> getDeudasDelMesActual() {
        return deudaService.getDeudasDelMesActual();
    }

    @GetMapping("/mes/nopagadas")
    public List<Deuda> getDeudasDelMesActualYNoPagadas() {
        return deudaService.getDeudasDelMesActualYNoPagadas();
    }

    @PostMapping("/subir/{cronogramaId}")
    public void uploadFile(@RequestParam("file") MultipartFile file, @PathVariable Long cronogramaId) {
        try {
            deudaService.processFile(file, cronogramaId);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al procesar el archivo", e);
        }
    }

    @GetMapping("/por-mes")
    public List<Deuda> getDeudasPorMes(@RequestParam("mes") int mes, @RequestParam("anio") int anio) {
        return deudaService.obtenerDeudasPorMes(mes, anio);
    }

    @PutMapping("/{id}/pagar")
    public ResponseEntity<Deuda> marcarDeudaComoPagada(@PathVariable Long id) {
        Optional<Deuda> deudaOptional = deudaService.marcarDeudaComoPagada(id);

        return deudaOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> postDeuda(@RequestBody Deuda deuda) {
        Deuda nuevaDeuda = deudaService.guardarDeuda(deuda);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaDeuda);
    }
}
