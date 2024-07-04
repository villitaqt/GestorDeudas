package com.grupoocho.gestor_deudas.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


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

    // Constructor, getters y setters omitidos por brevedad
}


