package com.inmobiliaria.alquileres_temporarios.propietarios.model.politicas;

import com.inmobiliaria.alquileres_temporarios.reservas.model.Reserva;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PoliticaEstricta implements PoliticaCancelacion {
    @Override
    public BigDecimal calcularPenalidad(Reserva reserva, LocalDate fechaCancelacion) {
        // Retiene el 100% del monto total de la reserva
        return reserva.getMontoTotal(); 
    }
}