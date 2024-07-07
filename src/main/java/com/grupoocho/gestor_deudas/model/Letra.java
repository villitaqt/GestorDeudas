package com.grupoocho.gestor_deudas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Letra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer numero;
    private LocalDate vencimiento;
    private BigDecimal saldo;
    private BigDecimal capital;
    private BigDecimal interes;
    private BigDecimal cuota;

    @ManyToOne
    @JoinColumn(name = "cronograma_id")
    private Cronograma cronograma;
}