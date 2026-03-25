package com.inmobiliaria.alquileres_temporarios.propietarios.service;

import com.inmobiliaria.alquileres_temporarios.gastos.model.GastoMantenimiento;
import com.inmobiliaria.alquileres_temporarios.gastos.repository.GastoRepository;
import com.inmobiliaria.alquileres_temporarios.propietarios.dto.LiquidacionResponse;
import com.inmobiliaria.alquileres_temporarios.propietarios.model.Liquidacion;
import com.inmobiliaria.alquileres_temporarios.propietarios.model.Propietario;
import com.inmobiliaria.alquileres_temporarios.propietarios.repository.LiquidacionRepository;
import com.inmobiliaria.alquileres_temporarios.propietarios.repository.PropietarioRepository;
import com.inmobiliaria.alquileres_temporarios.reservas.model.EstadoReserva;
import com.inmobiliaria.alquileres_temporarios.reservas.model.Reserva;
import com.inmobiliaria.alquileres_temporarios.reservas.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LiquidacionService {

    private final ReservaRepository reservaRepo;
    private final GastoRepository gastoRepo;
    private final PropietarioRepository propietarioRepo;
    private final LiquidacionRepository liquidacionRepo;

    @Transactional
    public LiquidacionResponse calcularLiquidacion(Long propietarioId, int mes, int anio) {
        Propietario p = propietarioRepo.findById(propietarioId)
                .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));
        List<Reserva> reservas = reservaRepo.buscarPorPropietarioYPeriodoPendientes(propietarioId, mes, anio)
                .stream()
                .filter(r -> r.getEstado() != EstadoReserva.PENDIENTE)
                .filter(r -> r.getEstado() == EstadoReserva.CANCELADA || r.getSaldoPendiente().compareTo(BigDecimal.ZERO) == 0)
                .toList();
                
        List<GastoMantenimiento> gastos = gastoRepo.buscarGastosPorPropietarioYPeriodoPendientes(propietarioId, mes, anio);

        BigDecimal ingresosEstadias = reservas.stream()
                .filter(r -> r.getEstado() != EstadoReserva.CANCELADA)
                .map(r -> r.getMontoTotal().subtract(r.getDepositoRetenido()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal ingresosPenalidades = reservas.stream()
                .filter(r -> r.getMontoPenalidad() != null)
                .map(Reserva::getMontoPenalidad)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal ingresos = ingresosEstadias.add(ingresosPenalidades);

        BigDecimal comisiones = reservas.stream()
                .filter(r -> r.getEstado() != EstadoReserva.CANCELADA)
                .map(Reserva::getComisionInmobiliaria)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalGastos = gastos.stream()
                .map(GastoMantenimiento::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalNeto = ingresos.subtract(comisiones).subtract(totalGastos);

        return new LiquidacionResponse(
            p.getNombre() + " " + p.getApellido(),
            mes, anio, ingresos, comisiones, totalGastos, totalNeto
        );
    }

    @Transactional
    public Liquidacion confirmarLiquidacion(Long propietarioId, int mes, int anio) {
        Propietario p = propietarioRepo.findById(propietarioId)
                .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));
        List<Reserva> reservas = reservaRepo.buscarPorPropietarioYPeriodoPendientes(propietarioId, mes, anio)
                .stream()
                .filter(r -> r.getEstado() != EstadoReserva.PENDIENTE)
                .filter(r -> r.getEstado() == EstadoReserva.CANCELADA || r.getSaldoPendiente().compareTo(BigDecimal.ZERO) == 0)
                .toList();
                
        List<GastoMantenimiento> gastos = gastoRepo.buscarGastosPorPropietarioYPeriodoPendientes(propietarioId, mes, anio);

        LiquidacionResponse calculo = calcularLiquidacion(propietarioId, mes, anio);

        Liquidacion liq = new Liquidacion();
        liq.setPropietario(p);
        liq.setMes(mes);
        liq.setAnio(anio);
        
        liq.setTotalBruto(calculo.getIngresos());
        liq.setTotalComisiones(calculo.getComisiones());
        liq.setTotalGastos(calculo.getGastos());
        liq.setNetoAPagar(calculo.getTotalNeto());

        reservas.forEach(r -> r.setLiquidada(true));
        gastos.forEach(g -> g.setLiquidado(true));

        reservaRepo.saveAll(reservas);
        gastoRepo.saveAll(gastos);

        return liquidacionRepo.save(liq);
    }

    public List<Liquidacion> obtenerHistorial(Long propietarioId) {
        return liquidacionRepo.findByPropietarioIdOrderByFechaEmisionDesc(propietarioId);
    }
}