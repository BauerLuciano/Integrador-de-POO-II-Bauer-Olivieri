package com.inmobiliaria.alquileres_temporarios.gastos.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inmobiliaria.alquileres_temporarios.propiedades.model.Propiedad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class GastoMantenimiento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal monto;
    private LocalDate fecha;
    private String concepto;

    @Column(name = "liquidado")
    private boolean liquidado = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propiedad_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Propiedad propiedad;
}