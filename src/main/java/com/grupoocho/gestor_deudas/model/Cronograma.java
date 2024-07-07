package com.grupoocho.gestor_deudas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Cronograma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal monto;
    private BigDecimal interes;
    private LocalDate fechaDesembolso;
    private Integer plazo;

    @OneToMany(mappedBy = "cronograma", cascade = CascadeType.ALL)
    private List<Letra> letras;
}