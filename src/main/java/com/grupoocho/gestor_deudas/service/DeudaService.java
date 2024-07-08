package com.grupoocho.gestor_deudas.service;

import com.grupoocho.gestor_deudas.model.Cronograma;
import com.grupoocho.gestor_deudas.model.Deuda;
import com.grupoocho.gestor_deudas.model.Letra;
import com.grupoocho.gestor_deudas.repository.CronogramaRepository;
import com.grupoocho.gestor_deudas.repository.DeudaRepository;
import com.grupoocho.gestor_deudas.repository.LetraRepository;
import javax.persistence.*;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.Comparator;

@Service
public class DeudaService {

    private final DeudaRepository deudaRepository;
    private final CronogramaRepository cronogramaRepository;
    private final LetraRepository letraRepository;

    private static final Logger logger = Logger.getLogger(DeudaService.class.getName());
    @Autowired
    public DeudaService(DeudaRepository deudaRepository, CronogramaRepository cronogramaRepository, LetraRepository letraRepository) {
        this.deudaRepository = deudaRepository;
        this.cronogramaRepository = cronogramaRepository;
        this.letraRepository = letraRepository;
    }

    public List<Deuda> getAllDeudas() {
        return deudaRepository.findAll();
    }

    @Transactional
    public void saveAll(List<Deuda> deudas) {
        deudaRepository.saveAll(deudas);
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

        // Obtener todas las deudas
        List<Deuda> deudas = deudaRepository.findAll();

        // Filtrar por deudas del mes actual y no pagadas de meses anteriores
        return deudas.stream()
                .filter(deuda -> {
                    YearMonth deudaYearMonth = YearMonth.from(deuda.getFechaVencimiento());
                    return (deudaYearMonth.equals(currentYearMonth) ||
                            (deudaYearMonth.isBefore(currentYearMonth) && !deuda.isEstaPagado()));
                })
                .sorted(Comparator.comparing(Deuda::getFechaVencimiento))
                .collect(Collectors.toList());
    }



    public List<Deuda> obtenerDeudasPorMes(int mes, int anio) {
        try {
            YearMonth yearMonth = YearMonth.of(anio, mes);
            LocalDate startDate = yearMonth.atDay(1);
            LocalDate endDate = yearMonth.atEndOfMonth();

            logger.info("Fetching deudas from " + startDate + " to " + endDate);

            // Obtener deudas del repositorio y ordenarlas por fecha de vencimiento
            return ordenarPorFecha(deudaRepository.findByFechaVencimientoBetween(startDate, endDate));
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

    public void processFile(MultipartFile file, Long cronogramaId) throws IOException {
        Cronograma cronograma = cronogramaRepository.findById(cronogramaId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Cronograma ID: " + cronogramaId));

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            // Saltar encabezados
            reader.readLine(); // Primera línea de encabezado
            reader.readLine(); // Segunda línea de encabezado

            List<Letra> letras = new ArrayList<>();
            String line;  // Declara la variable `line` aquí

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                Letra letra = new Letra();
                letra.setNumero(Integer.parseInt(fields[0].trim()));
                letra.setVencimiento(LocalDate.parse(fields[1].trim(), DateTimeFormatter.ofPattern("M/d/yyyy")));
                letra.setSaldo(new BigDecimal(fields[2].replace("S/", "").trim()));
                letra.setCapital(new BigDecimal(fields[3].replace("S/", "").trim()));
                letra.setInteres(new BigDecimal(fields[4].replace("S/", "").trim()));
                letra.setCuota(new BigDecimal(fields[5].replace("S/", "").trim()));
                letra.setCronograma(cronograma);
                letras.add(letra);
            }

            letraRepository.saveAll(letras);
        }
    }
}
