package com.grupoocho.gestor_deudas.controller;

import com.grupoocho.gestor_deudas.config.CronogramaDTO;
import com.grupoocho.gestor_deudas.model.Cronograma;
import com.grupoocho.gestor_deudas.model.Deuda;
import com.grupoocho.gestor_deudas.repository.CronogramaRepository;
import com.grupoocho.gestor_deudas.service.CronogramaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cronograma")
public class CronogramaController {

    private final CronogramaService cronogramaService;

    @Autowired
    public CronogramaController(CronogramaService cronogramaService) {
        this.cronogramaService = cronogramaService;
    }

    @GetMapping
    public List<Cronograma> getAllCronograma(){
        return cronogramaService.getAllCronogramas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cronograma> getCronogramaById(@PathVariable Long id) {
        Cronograma cronograma = cronogramaService.getCronogramaById(id);
        if (cronograma != null) {
            return ResponseEntity.ok(cronograma);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<?> postCronograma(@RequestBody Cronograma cronograma) {
        Cronograma nuevoCronograma =  cronogramaService.guardarCronograma(cronograma);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCronograma);
    }

    @PostMapping("/crear")
    public ResponseEntity<Cronograma> crearCronograma(@RequestBody CronogramaDTO cronogramaDto) {
        // Convertir el DTO a la entidad Cronograma
        Cronograma cronograma = new Cronograma(
                cronogramaDto.getMonto(),
                cronogramaDto.getInteres(),
                cronogramaDto.getFechaDesembolso(),
                cronogramaDto.getPlazo(),
                cronogramaDto.getLetras()
        );

        // Guardar el cronograma en la base de datos
        Cronograma savedCronograma = cronogramaService.guardarCronograma(cronograma);
        return ResponseEntity.ok(savedCronograma);
    }
}