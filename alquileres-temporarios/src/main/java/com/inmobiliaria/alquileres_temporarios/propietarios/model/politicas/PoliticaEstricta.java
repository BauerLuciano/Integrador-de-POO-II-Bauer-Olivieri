package com.inmobiliaria.alquileres_temporarios.propietarios.model.politicas;

import com.inmobiliaria.alquileres_temporarios.reservas.model.Reserva;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("ESTRICTA")
@Data
@EqualsAndHashCode(callSuper = true)
public class PoliticaEstricta extends PoliticaCancelacion {

    private BigDecimal porcentajeRetencion;

    @Override
    public BigDecimal calcularPenalidad(Reserva reserva, LocalDate fechaCancelacion) {
        if (porcentajeRetencion == null) {
            return reserva.getMontoTotal(); 
        }
        
        return reserva.getMontoTotal().multiply(porcentajeRetencion).divide(BigDecimal.valueOf(100));
    }
}