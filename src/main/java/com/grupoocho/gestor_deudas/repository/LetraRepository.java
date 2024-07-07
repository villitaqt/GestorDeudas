package com.grupoocho.gestor_deudas.repository;

import com.grupoocho.gestor_deudas.model.Letra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LetraRepository extends JpaRepository<Letra, Long> {
}