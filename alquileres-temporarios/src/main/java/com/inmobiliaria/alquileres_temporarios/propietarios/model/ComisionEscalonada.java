package com.inmobiliaria.alquileres_temporarios.propietarios.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("ESCALONADA")
public class ComisionEscalonada extends EsquemaComision {

    private BigDecimal montoUmbral;
    private BigDecimal porcentajeBase;
    private BigDecimal porcentajeExcedente;

    public ComisionEscalonada() {}

    @Override
    public BigDecimal calcularComision(BigDecimal montoBase) {
        if (montoBase == null) return BigDecimal.ZERO;
        
        if (montoBase.compareTo(montoUmbral) <= 0) {
            return montoBase.multiply(porcentajeBase);
        } else {
            BigDecimal comisionBase = montoUmbral.multiply(porcentajeBase);
            BigDecimal excedente = montoBase.subtract(montoUmbral);
            BigDecimal comisionExcedente = excedente.multiply(porcentajeExcedente);
            return comisionBase.add(comisionExcedente);
        }
    }

    public BigDecimal getMontoUmbral() { return montoUmbral; }
    public void setMontoUmbral(BigDecimal montoUmbral) { this.montoUmbral = montoUmbral; }
    public BigDecimal getPorcentajeBase() { return porcentajeBase; }
    public void setPorcentajeBase(BigDecimal porcentajeBase) { this.porcentajeBase = porcentajeBase; }
    public BigDecimal getPorcentajeExcedente() { return porcentajeExcedente; }
    public void setPorcentajeExcedente(BigDecimal porcentajeExcedente) { this.porcentajeExcedente = porcentajeExcedente; }
}