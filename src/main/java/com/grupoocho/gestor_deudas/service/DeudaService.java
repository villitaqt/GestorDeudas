package com.grupoocho.gestor_deudas.service;

import com.grupoocho.gestor_deudas.model.Deuda;
import com.grupoocho.gestor_deudas.repository.DeudaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeudaService {

    private final DeudaRepository deudaRepository;

    @Autowired
    public DeudaService(DeudaRepository deudaRepository) {
        this.deudaRepository = deudaRepository;
    }

    public List<Deuda> getAllDeudas() {
        return deudaRepository.findAll();
    }

    public List<Deuda> getDeudasDelMesActual() {
        LocalDate now = LocalDate.now();
        YearMonth currentYearMonth = YearMonth.from(now);

        return deudaRepository.findAll().stream()
                .filter(deuda -> YearMonth.from(deuda.getFechaVencimiento()).equals(currentYearMonth))
                .collect(Collectors.toList());
    }

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

    public Optional<Deuda> marcarDeudaComoPagada(Long id) {
        Optional<Deuda> deudaOptional = deudaRepository.findById(id);

        if (deudaOptional.isPresent()) {
            Deuda deuda = deudaOptional.get();
            if (!deuda.isEstaPagado()) {
                deuda.setEstaPagado(true);
                return Optional.of(deudaRepository.save(deuda));
            }
        }
        return Optional.empty();
    }

    public Deuda guardarDeuda(Deuda deuda) {
        return deudaRepository.save(deuda);
    }
}
