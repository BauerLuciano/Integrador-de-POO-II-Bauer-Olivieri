package com.inmobiliaria.alquileres_temporarios.propietarios.model.politicas;

import com.inmobiliaria.alquileres_temporarios.reservas.model.Reserva;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class PoliticaFlexible implements PoliticaCancelacion {
    @Override
    public BigDecimal calcularPenalidad(Reserva reserva, LocalDate fechaCancelacion) {
        long diasAnticipacion = ChronoUnit.DAYS.between(fechaCancelacion, reserva.getFechaInicio());
        
        if (diasAnticipacion > 7) {
            return BigDecimal.ZERO; // Cancelación gratuita
        } else {
            // Retiene el 50% si es sobre la fecha
            return reserva.getMontoTotal().divide(BigDecimal.valueOf(2)); 
        }
    }
}