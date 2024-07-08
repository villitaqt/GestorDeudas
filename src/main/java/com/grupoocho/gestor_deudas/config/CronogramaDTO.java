package com.grupoocho.gestor_deudas.config;

import com.grupoocho.gestor_deudas.model.Letra;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CronogramaDTO {
    private BigDecimal monto;
    private BigDecimal interes;
    private LocalDate fechaDesembolso;
    private Integer plazo;
    private List<Letra> letras;
    // getters and setters
}
