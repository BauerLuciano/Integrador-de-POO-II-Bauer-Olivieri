package com.inmobiliaria.alquileres_temporarios.propietarios.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IngresosPropietarioDTO {
    private Long propietarioId;
    private int cantidadReservas;
    private BigDecimal ingresosTotales;
}