package com.grupoocho.gestor_deudas.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Recibo extends Deuda {
    private String nombreDeudor;
    private String direccion;
    private String periodo;
    //LocalDate vencimiento
    private String informacion;
    private String detalleCobranza;


    public Recibo(long id, String numeroDocumento, String nombreDeudor, String nombreEmpresa, LocalDate fechaVencimiento,
                  BigDecimal monto, String imagen, boolean estaPagado, String direccion,
                  String periodo, String informacion, String detalleCobranza) {

        super(id, numeroDocumento, nombreDeudor, nombreEmpresa, fechaVencimiento, monto, imagen, estaPagado);
        this.direccion = direccion;
        this.periodo = periodo;
        this.informacion = informacion;
        this.detalleCobranza = detalleCobranza;
    }
}
