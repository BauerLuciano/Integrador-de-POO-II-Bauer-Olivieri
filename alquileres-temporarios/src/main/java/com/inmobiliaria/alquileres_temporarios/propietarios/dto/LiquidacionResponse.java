package com.inmobiliaria.alquileres_temporarios.propietarios.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LiquidacionResponse {
    private String nombrePropietario;
    private int mes;
    private int anio;
    private BigDecimal ingresosBrutos;      
    private BigDecimal comisiones;    
    private BigDecimal gastosMantenimiento;        
    private BigDecimal totalNeto;     
    
    private List<DetalleReservaDto> detalleReservas;
    private List<DetalleGastoDto> detalleGastos;

    @Data
    @AllArgsConstructor
    public static class DetalleReservaDto {
        private LocalDate fecha;
        private String propiedad;
        private String inquilino;
        private long noches;
        private BigDecimal monto;
    }

    @Data
    @AllArgsConstructor
    public static class DetalleGastoDto {
        private String motivo;
        private BigDecimal monto;
    }
}