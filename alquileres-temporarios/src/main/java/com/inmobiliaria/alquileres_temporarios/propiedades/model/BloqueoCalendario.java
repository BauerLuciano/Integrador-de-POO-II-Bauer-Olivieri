package com.inmobiliaria.alquileres_temporarios.propiedades.model;

import java.time.LocalDate;

public interface BloqueoCalendario {
    LocalDate getFechaInicio();
    LocalDate getFechaFin();

    // Lógica para la HU-06 (Validación de solapamiento)
    default boolean seSolapaCon(LocalDate inicio, LocalDate fin) {
        return !inicio.isAfter(this.getFechaFin()) && !fin.isBefore(this.getFechaInicio());
    }
}