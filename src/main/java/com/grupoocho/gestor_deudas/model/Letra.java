package com.grupoocho.gestor_deudas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cronograma_id", foreignKey = @ForeignKey(name = "fk_letra_cronograma"))
    private Cronograma cronograma;

}