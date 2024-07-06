package com.grupoocho.gestor_deudas.repository;

import com.grupoocho.gestor_deudas.model.Deuda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface DeudaRepository
        extends JpaRepository<Deuda, Long> {

    List<Deuda> findByFechaVencimientoBetween(LocalDate startDate, LocalDate endDate);
}
