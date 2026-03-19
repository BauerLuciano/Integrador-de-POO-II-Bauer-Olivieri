package com.inmobiliaria.alquileres_temporarios.reservas.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.inmobiliaria.alquileres_temporarios.gastos.repository.GastoRepository;
import com.inmobiliaria.alquileres_temporarios.propiedades.model.Propiedad;
import com.inmobiliaria.alquileres_temporarios.propiedades.repository.PropiedadRepository;
import com.inmobiliaria.alquileres_temporarios.propiedades.service.PropiedadService;
import com.inmobiliaria.alquileres_temporarios.propietarios.dto.IngresosPropietarioDTO;
import com.inmobiliaria.alquileres_temporarios.propietarios.model.politicas.PoliticaCancelacion;
import com.inmobiliaria.alquileres_temporarios.reservas.dto.GastoDetalleDTO;
import com.inmobiliaria.alquileres_temporarios.reservas.dto.ReporteLiquidacionDTO;
import com.inmobiliaria.alquileres_temporarios.reservas.dto.ReservaDetalleDTO;
import com.inmobiliaria.alquileres_temporarios.reservas.model.EstadoReserva;
import com.inmobiliaria.alquileres_temporarios.reservas.model.Pago;
import com.inmobiliaria.alquileres_temporarios.reservas.model.Reserva;
import com.inmobiliaria.alquileres_temporarios.reservas.repository.ReservaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepo;
    private final PropiedadRepository propiedadRepo;
    private final PropiedadService propiedadService; 
    private final GastoRepository gastoRepo; // Inyectamos gastos

    @Transactional
    public Reserva crearReserva(Reserva reserva) {
        if (reserva.getFechaInicio().isAfter(reserva.getFechaFin()) || reserva.getFechaInicio().isEqual(reserva.getFechaFin())) {
            throw new IllegalArgumentException("Las fechas de la reserva son inválidas.");
        }
        Propiedad propiedad = propiedadRepo.findById(reserva.getPropiedad().getId())
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada"));
        reserva.setPropiedad(propiedad);
        reserva.calcularCostos();
        propiedad.setEstado("Alquilada");
        propiedadRepo.save(propiedad);
        return reservaRepo.save(reserva);
    }

    @Transactional
    public Reserva registrarPago(Long reservaId, Pago nuevoPago) {
        Reserva reserva = reservaRepo.findById(reservaId).orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        reserva.registrarPago(nuevoPago);
        return reservaRepo.save(reserva);
    }
    
    public BigDecimal consultarSaldo(Long reservaId) {
        Reserva reserva = reservaRepo.findById(reservaId).orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        return reserva.getSaldoPendiente();
    }

    @Transactional
    public Reserva cancelarReserva(Long reservaId) {
        Reserva reserva = reservaRepo.findById(reservaId).orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        PoliticaCancelacion politica = reserva.getPropiedad().getPoliticaCancelacion();
        BigDecimal penalidad = politica.calcularPenalidad(reserva, LocalDate.now());
        reserva.setMontoPenalidad(penalidad);
        reserva.setEstado(EstadoReserva.CANCELADA);
        return reservaRepo.save(reserva);
    }

    public List<Reserva> obtenerTodas() { 
        return reservaRepo.findAllOptimizadas(); 
    }

    public Reserva obtenerPorId(Long id) {
        return reservaRepo.findById(id).orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
    }

    // --- REPORTES HU-13 ---
    public List<Reserva> obtenerHistorialPorPropiedad(Long propiedadId) {
        return reservaRepo.findByPropiedadIdOrderByFechaInicioDesc(propiedadId);
    }

    public IngresosPropietarioDTO calcularIngresosPropietario(Long propietarioId, LocalDate inicio, LocalDate fin) {
        List<Reserva> reservas = reservaRepo.findReservasParaIngresos(propietarioId, inicio, fin);
        BigDecimal ingresosTotales = reservas.stream()
                .map(r -> r.getMontoTotal().subtract(r.getComisionInmobiliaria()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new IngresosPropietarioDTO(propietarioId, reservas.size(), ingresosTotales);
    }

    // --- MÉTODO PARA EL BOTÓN LIQUIDAR ---
    public ReporteLiquidacionDTO generarLiquidacion(Long propietarioId, int mes, int anio) {
        List<Reserva> reservas = reservaRepo.buscarPorPropietarioYPeriodo(propietarioId, mes, anio);
        List<com.inmobiliaria.alquileres_temporarios.gastos.model.GastoMantenimiento> gastos = gastoRepo.buscarGastosPorPropietarioYPeriodo(propietarioId, mes, anio);

        ReporteLiquidacionDTO dto = new ReporteLiquidacionDTO();

        dto.setDetalleReservas(reservas.stream().map(r -> new ReservaDetalleDTO(
                r.getFechaInicio(), r.getPropiedad().getDireccion(),
                r.getInquilino() != null ? r.getInquilino() : "Inquilino Final",
                java.time.temporal.ChronoUnit.DAYS.between(r.getFechaInicio(), r.getFechaFin()),
                r.getMontoTotal())).toList());

        dto.setDetalleGastos(gastos.stream().map(g -> new GastoDetalleDTO(g.getConcepto(), g.getMonto())).toList());

        BigDecimal estadias = reservas.stream().map(Reserva::getMontoTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal penalidades = reservas.stream().filter(r -> r.getMontoPenalidad() != null).map(Reserva::getMontoPenalidad).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal comision = reservas.stream().map(Reserva::getComisionInmobiliaria).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalGastos = gastos.stream().map(g -> g.getMonto()).reduce(BigDecimal.ZERO, BigDecimal::add);

        dto.setIngresosEstadias(estadias);
        dto.setIngresosPenalidades(penalidades);
        dto.setTotalIngresosBrutos(estadias.add(penalidades));
        dto.setComisionAgencia(comision);
        dto.setGastosMantenimiento(totalGastos);
        dto.setTotalALiquidar(dto.getTotalIngresosBrutos().subtract(comision).subtract(totalGastos));

        return dto;
    }
}