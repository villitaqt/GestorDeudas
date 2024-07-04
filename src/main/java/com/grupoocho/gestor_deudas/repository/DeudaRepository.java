package com.grupoocho.gestor_deudas.repository;

import com.grupoocho.gestor_deudas.model.Deuda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeudaRepository
        extends JpaRepository<Deuda, Long> {


}
