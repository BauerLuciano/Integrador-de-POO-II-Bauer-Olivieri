package com.inmobiliaria.alquileres_temporarios.propietarios.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class Liquidacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaEmision = LocalDate.now();
    private int mes;
    private int anio;

    @ManyToOne
    @JoinColumn(name = "propietario_id")
    private Propietario propietario;

    private BigDecimal totalBruto;
    private BigDecimal totalComisiones;
    private BigDecimal totalGastos;
    private BigDecimal netoAPagar;
}