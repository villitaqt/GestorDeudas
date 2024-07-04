package com.grupoocho.gestor_deudas.repository;

import com.grupoocho.gestor_deudas.model.Deuda;
import com.grupoocho.gestor_deudas.model.Recibo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReciboRepository
        extends JpaRepository<Recibo, Long> {
}
