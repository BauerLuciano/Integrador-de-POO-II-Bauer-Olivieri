package com.inmobiliaria.alquileres_temporarios.reservas.dto;
import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReservaDetalleDTO {
    private LocalDate fecha;
    private String propiedad;
    private String inquilino;
    private long noches;
    private BigDecimal monto;
}