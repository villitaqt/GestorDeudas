package com.grupoocho.gestor_deudas.Controller;

import com.grupoocho.gestor_deudas.model.DetalleFactura;
import com.grupoocho.gestor_deudas.model.Factura;
import com.grupoocho.gestor_deudas.model.Recibo;
import com.grupoocho.gestor_deudas.repository.DetalleFacturaRepository;
import com.grupoocho.gestor_deudas.repository.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    private final FacturaRepository facturaRepository; // Repositorio JPA de Factura
    private final DetalleFacturaRepository detalleFacturaRepository;

    @Autowired
    public FacturaController(FacturaRepository facturaRepository, DetalleFacturaRepository detalleFacturaRepository) {
        this.facturaRepository = facturaRepository;
        this.detalleFacturaRepository = detalleFacturaRepository;
    }

    // Endpoint para crear una nueva Factura con Detalles de Factura
    @PostMapping
    public void crearFactura(@RequestBody Factura factura) {
        facturaRepository.save(factura);
    }

    @GetMapping
    public List<Factura> getFactura(){
        return facturaRepository.findAll();
    }

}