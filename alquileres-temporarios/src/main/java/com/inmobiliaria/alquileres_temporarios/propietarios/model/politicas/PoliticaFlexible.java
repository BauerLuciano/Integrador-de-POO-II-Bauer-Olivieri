package com.inmobiliaria.alquileres_temporarios.propietarios.model.politicas;

import com.inmobiliaria.alquileres_temporarios.reservas.model.Reserva;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@DiscriminatorValue("FLEXIBLE")
@Data
@EqualsAndHashCode(callSuper = true)
public class PoliticaFlexible extends PoliticaCancelacion {

    // Los atributos que pide el diagrama
    private Integer diasAnticipacionSinCargo;
    private BigDecimal porcentajePenalidadTardia;

    @Override
    public BigDecimal calcularPenalidad(Reserva reserva, LocalDate fechaCancelacion) {
        long diasAnticipacion = ChronoUnit.DAYS.between(fechaCancelacion, reserva.getFechaInicio());
        
        int diasLimite = (diasAnticipacionSinCargo != null) ? diasAnticipacionSinCargo : 7;
        BigDecimal penalidad = (porcentajePenalidadTardia != null) ? porcentajePenalidadTardia : new BigDecimal("50");

        if (diasAnticipacion > diasLimite) {
            return BigDecimal.ZERO; 
        } else {
            return reserva.getMontoTotal().multiply(penalidad).divide(BigDecimal.valueOf(100)); 
        }
    }
}