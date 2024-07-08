package com.grupoocho.gestor_deudas.model;

import javax.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"numeroDocumento"})})
public class Deuda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String numeroDocumento;
    private String nombreDeudor;
    private String nombreEmpresa;
    private LocalDate fechaVencimiento;
    private BigDecimal monto;
    private String imagen;
    private boolean estaPagado;

    public Deuda(String numeroDocumento, String nombreDeudor, String nombreEmpresa, LocalDate fechaVencimiento, BigDecimal monto, boolean estaPagado) {
        this.numeroDocumento = numeroDocumento;
        this.nombreDeudor = nombreDeudor;
        this.nombreEmpresa = nombreEmpresa;
        this.fechaVencimiento = fechaVencimiento;
        this.monto = monto;
        this.estaPagado = estaPagado;
    }

    // Constructor, getters y setters omitidos por brevedad
}


