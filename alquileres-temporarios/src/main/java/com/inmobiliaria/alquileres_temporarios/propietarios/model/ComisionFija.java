package com.inmobiliaria.alquileres_temporarios.propietarios.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("FIJA")
public class ComisionFija extends EsquemaComision {

    private BigDecimal porcentaje; // Ej: 0.15 para 15%

    public ComisionFija() {}

    public ComisionFija(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    @Override
    public BigDecimal calcularComision(BigDecimal montoBase) {
        if (montoBase == null || porcentaje == null) return BigDecimal.ZERO;
        return montoBase.multiply(porcentaje);
    }

    public BigDecimal getPorcentaje() { return porcentaje; }
    public void setPorcentaje(BigDecimal porcentaje) { this.porcentaje = porcentaje; }
}