package com.grupoocho.gestor_deudas.repository;
import com.grupoocho.gestor_deudas.model.Cronograma;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CronogramaRepository extends JpaRepository<Cronograma, Long> {
}
