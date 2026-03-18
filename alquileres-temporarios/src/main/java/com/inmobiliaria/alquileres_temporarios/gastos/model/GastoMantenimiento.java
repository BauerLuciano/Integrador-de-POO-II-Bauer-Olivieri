package com.inmobiliaria.alquileres_temporarios.gastos.model;

import com.inmobiliaria.alquileres_temporarios.propiedades.model.Propiedad;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class GastoMantenimiento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal monto;
    private LocalDate fecha;
    private String concepto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propiedad_id")
    private Propiedad propiedad;
}