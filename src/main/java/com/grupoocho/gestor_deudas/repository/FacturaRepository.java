package com.grupoocho.gestor_deudas.repository;


import com.grupoocho.gestor_deudas.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
}
