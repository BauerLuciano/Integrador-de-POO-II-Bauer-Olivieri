package com.inmobiliaria.alquileres_temporarios.propietarios.service;

import com.inmobiliaria.alquileres_temporarios.gastos.model.GastoMantenimiento;
import com.inmobiliaria.alquileres_temporarios.gastos.repository.GastoRepository;
import com.inmobiliaria.alquileres_temporarios.propietarios.dto.LiquidacionResponse;
import com.inmobiliaria.alquileres_temporarios.propietarios.model.Propietario;
import com.inmobiliaria.alquileres_temporarios.propietarios.repository.PropietarioRepository;
import com.inmobiliaria.alquileres_temporarios.reservas.model.EstadoReserva;
import com.inmobiliaria.alquileres_temporarios.reservas.model.Reserva;
import com.inmobiliaria.alquileres_temporarios.reservas.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LiquidacionService {

    private final ReservaRepository reservaRepo;
    private final GastoRepository gastoRepo;
    private final PropietarioRepository propietarioRepo;

    public LiquidacionResponse calcularLiquidacion(Long propietarioId, int mes, int anio) {
        Propietario p = propietarioRepo.findById(propietarioId)
                .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

        List<Reserva> reservas = reservaRepo.buscarPorPropietarioYPeriodo(propietarioId, mes, anio);
        List<GastoMantenimiento> gastos = gastoRepo.buscarGastosPorPropietarioYPeriodo(propietarioId, mes, anio);

        // Sumamos Ingresos (Solo reservas activas o finalizadas, NO canceladas)
        BigDecimal ingresos = reservas.stream()
                .filter(r -> r.getEstado() != EstadoReserva.CANCELADA)
                .map(Reserva::getMontoTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Sumamos Comisiones
        BigDecimal comisiones = reservas.stream()
                .filter(r -> r.getEstado() != EstadoReserva.CANCELADA)
                .map(Reserva::getComisionInmobiliaria)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Sumamos Gastos
        BigDecimal totalGastos = gastos.stream()
                .map(GastoMantenimiento::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // La cuenta matemática final
        // Total = (Ingresos - Comisiones) - Gastos
        BigDecimal totalNeto = ingresos.subtract(comisiones).subtract(totalGastos);

        return new LiquidacionResponse(
            p.getNombre() + " " + p.getApellido(),
            mes, anio, ingresos, comisiones, totalGastos, totalNeto
        );
    }
}