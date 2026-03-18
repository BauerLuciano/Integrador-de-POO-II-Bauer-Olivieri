package com.inmobiliaria.alquileres_temporarios.reservas.dto;
import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class ReporteLiquidacionDTO {
    private BigDecimal ingresosEstadias;
    private BigDecimal ingresosPenalidades;
    private BigDecimal totalIngresosBrutos;
    private BigDecimal comisionAgencia;
    private List<GastoDetalleDTO> detalleGastos;
    private BigDecimal gastosMantenimiento;
    private BigDecimal totalALiquidar;
    private List<ReservaDetalleDTO> detalleReservas;
}