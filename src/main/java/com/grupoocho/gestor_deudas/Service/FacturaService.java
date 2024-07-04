package com.grupoocho.gestor_deudas.Service;

import com.grupoocho.gestor_deudas.model.DetalleFactura;
import com.grupoocho.gestor_deudas.model.Factura;
import com.grupoocho.gestor_deudas.repository.DetalleFacturaRepository;
import org.springframework.stereotype.Service;

@Service
public class FacturaService {

    private final DetalleFacturaRepository detalleFacturaRepository;

    public FacturaService(DetalleFacturaRepository detalleFacturaRepository) {
        this.detalleFacturaRepository = detalleFacturaRepository;
    }

    public void guardarDetalleFactura(DetalleFactura detalleFactura){
        detalleFacturaRepository.save(detalleFactura);
    }

    
}
