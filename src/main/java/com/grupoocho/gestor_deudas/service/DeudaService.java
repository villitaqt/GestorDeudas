package com.grupoocho.gestor_deudas.service;

import com.grupoocho.gestor_deudas.model.Deuda;
import com.grupoocho.gestor_deudas.repository.DeudaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.Comparator;

@Service
public class DeudaService {

    private final DeudaRepository deudaRepository;
    private static final Logger logger = Logger.getLogger(DeudaService.class.getName());
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

    // Función de ordenación por fecha de vencimiento
    private List<Deuda> ordenarPorFecha(List<Deuda> deudas) {
        return deudas.stream()
                .sorted(Comparator.comparing(Deuda::getFechaVencimiento))
                .collect(Collectors.toList());
    }

    public List<Deuda> getDeudasDelMesActualYNoPagadas() {
        LocalDate now = LocalDate.now();
        YearMonth currentYearMonth = YearMonth.from(now);

        // Obtener todas las deudas y ordenarlas por fecha de vencimiento
        List<Deuda> deudasOrdenadas = ordenarPorFecha(deudaRepository.findAll());

        // Filtrar por deudas del mes actual y no pagadas
        return deudasOrdenadas.stream()
                .filter(deuda -> {
                    YearMonth deudaYearMonth = YearMonth.from(deuda.getFechaVencimiento());
                    return (deudaYearMonth.equals(currentYearMonth) ||
                            (deudaYearMonth.isBefore(currentYearMonth) && !deuda.isEstaPagado()));
                })
                .collect(Collectors.toList());
    }

    public List<Deuda> obtenerDeudasPorMes(int mes, int anio) {
        try {
            YearMonth yearMonth = YearMonth.of(anio, mes);
            LocalDate startDate = yearMonth.atDay(1);
            LocalDate endDate = yearMonth.atEndOfMonth();

            logger.info("Fetching deudas from " + startDate + " to " + endDate);

            // Obtener deudas del repositorio y ordenarlas por fecha de vencimiento
            List<Deuda> deudasOrdenadas = ordenarPorFecha(deudaRepository.findByFechaVencimientoBetween(startDate, endDate));

            return deudasOrdenadas;
        } catch (Exception e) {
            logger.severe("Error fetching deudas: " + e.getMessage());
            throw e;
        }
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
