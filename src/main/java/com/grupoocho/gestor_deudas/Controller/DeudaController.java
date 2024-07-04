package com.grupoocho.gestor_deudas.Controller;

import com.grupoocho.gestor_deudas.model.Deuda;
import com.grupoocho.gestor_deudas.repository.DeudaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/deuda")
public class DeudaController {

    private final DeudaRepository deudaRepository;

    @Autowired
    public DeudaController(DeudaRepository deudaRepository) {
        this.deudaRepository = deudaRepository;
    }

    @GetMapping
    public List<Deuda> getDeudas(){
        return deudaRepository.findAll();
    }

    @GetMapping("/mes")
    public List<Deuda> getDeudasDelMesActual() {
        LocalDate now = LocalDate.now();
        YearMonth currentYearMonth = YearMonth.from(now);

        return deudaRepository.findAll().stream()
                .filter(deuda -> YearMonth.from(deuda.getFechaVencimiento()).equals(currentYearMonth))
                .collect(Collectors.toList());
    }

    @GetMapping("/mes/nopagadas")
    public List<Deuda> getDeudasDelMesActualYNoPagadas() {
        LocalDate now = LocalDate.now();
        YearMonth currentYearMonth = YearMonth.from(now);

        return deudaRepository.findAll().stream()
                .filter(deuda -> {
                    YearMonth deudaYearMonth = YearMonth.from(deuda.getFechaVencimiento());
                    return (deudaYearMonth.equals(currentYearMonth) ||
                            (deudaYearMonth.isBefore(currentYearMonth) && !deuda.isEstaPagado()));
                })
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}/pagar")
    public ResponseEntity<Deuda> marcarDeudaComoPagada(@PathVariable Long id) {
        Optional<Deuda> deudaOptional = deudaRepository.findById(id);

        if (deudaOptional.isPresent()) {
            Deuda deuda = deudaOptional.get();
            if (!deuda.isEstaPagado()) {
                deuda.setEstaPagado(true);
                Deuda deudaPagada = deudaRepository.save(deuda);
                return ResponseEntity.ok(deudaPagada);
            } else {
                return ResponseEntity.badRequest().body(null);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public void postDeuda(@RequestBody Deuda deuda){
        deudaRepository.save(deuda);
    }
}
