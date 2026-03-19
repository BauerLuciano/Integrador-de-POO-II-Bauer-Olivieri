package com.inmobiliaria.alquileres_temporarios.propietarios.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("FIJA")
public class ComisionFija extends EsquemaComision {

    private BigDecimal porcentaje;
    public ComisionFija() {}

    public ComisionFija(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    @Override
    public BigDecimal calcularComision(BigDecimal montoBase) {
        if (montoBase == null || porcentaje == null) return BigDecimal.ZERO;
        return montoBase.multiply(porcentaje)
            .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getPorcentaje() { return porcentaje; }
    public void setPorcentaje(BigDecimal porcentaje) { this.porcentaje = porcentaje; }
}