package com.grupoocho.gestor_deudas.model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private List<Letra> letras = new ArrayList<>();

    // Constructor personalizado
    public Cronograma(BigDecimal monto, BigDecimal interes, LocalDate fechaDesembolso, Integer plazo, List<Letra> letras) {
        this.monto = monto;
        this.interes = interes;
        this.fechaDesembolso = fechaDesembolso;
        this.plazo = plazo;
        this.setLetras(letras);
    }

    // Método para setear las letras y asignar el cronograma correspondiente
    public void setLetras(List<Letra> letras) {
        this.letras = letras;
        for (Letra letra : letras) {
            letra.setCronograma(this);
        }
    }
}