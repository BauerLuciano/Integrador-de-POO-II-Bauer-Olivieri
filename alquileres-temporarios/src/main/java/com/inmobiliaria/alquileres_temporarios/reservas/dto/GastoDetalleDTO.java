package com.inmobiliaria.alquileres_temporarios.reservas.dto;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GastoDetalleDTO {
    private String motivo;
    private BigDecimal monto;
}