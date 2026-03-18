package com.inmobiliaria.alquileres_temporarios.propietarios.model.politicas;

import com.inmobiliaria.alquileres_temporarios.reservas.model.Reserva;
import java.math.BigDecimal;
import java.time.LocalDate;

public interface PoliticaCancelacion {
    BigDecimal calcularPenalidad(Reserva reserva, LocalDate fechaCancelacion);
}