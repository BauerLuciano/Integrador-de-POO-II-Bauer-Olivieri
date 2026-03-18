package com.inmobiliaria.alquileres_temporarios.propietarios.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LiquidacionResponse {
    private String nombrePropietario;
    private int mes;
    private int anio;
    private BigDecimal ingresos;      // Suma de reservas (montoTotal)
    private BigDecimal comisiones;    // Suma de comisiones cobradas por la inmobiliaria
    private BigDecimal gastos;        // Suma de gastos de mantenimiento
    private BigDecimal totalNeto;     // El resultado final para el propietario
}