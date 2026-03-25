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
    private final GastoRepository gastoRepo;

    @Transactional
public Reserva crearReserva(Reserva reserva) {
    if (reserva.getFechaInicio().isAfter(reserva.getFechaFin()) || reserva.getFechaInicio().isEqual(reserva.getFechaFin())) {
        throw new IllegalArgumentException("La fecha de inicio debe ser anterior a la de fin.");
    }

    boolean ocupada = reservaRepo.existeSolapamiento(
        reserva.getPropiedad().getId(), 
        reserva.getFechaInicio(), 
        reserva.getFechaFin()
    );

    if (ocupada) {
        throw new IllegalStateException("¡Error! La propiedad ya se encuentra reservada o bloqueada en esas fechas.");
    }

    Propiedad propiedad = propiedadRepo.findById(reserva.getPropiedad().getId())
            .orElseThrow(() -> new RuntimeException("Propiedad no encontrada"));
            
    reserva.setPropiedad(propiedad);
    reserva.calcularCostos();

    if(reserva.getFechaInicio().isEqual(LocalDate.now())) {
        propiedad.setEstado("Alquilada");
        propiedadRepo.save(propiedad);
    }

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
    public Reserva cancelarReserva(Long reservaId, String motivo, String detalle) {
        Reserva reserva = reservaRepo.findById(reservaId).orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        
        if (reserva.isLiquidada()) {
            throw new IllegalStateException("La reserva ya fue liquidada. No se puede cancelar.");
        }
        if (reserva.getEstado() == EstadoReserva.CANCELADA) {
            throw new IllegalStateException("La reserva ya se encuentra cancelada.");
        }
        reserva.setMotivoCancelacion(motivo);
        reserva.setDetalleCancelacion(detalle);

        PoliticaCancelacion politica = reserva.getPropiedad().getPoliticaCancelacion();
        if (politica != null) {
            BigDecimal penalidad = politica.calcularPenalidad(reserva, LocalDate.now());
            reserva.setMontoPenalidad(penalidad);
        } else {
            reserva.setMontoPenalidad(BigDecimal.ZERO);
        }

        reserva.setEstado(EstadoReserva.CANCELADA);
        Propiedad prop = reserva.getPropiedad();
        prop.setEstado("Disponible");
        propiedadRepo.save(prop);
        
        return reservaRepo.save(reserva);
    }

    public List<Reserva> obtenerTodas() { 
        return reservaRepo.findAllOptimizadas(); 
    }

    public Reserva obtenerPorId(Long id) {
        return reservaRepo.findById(id).orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
    }

    public List<Reserva> obtenerHistorialPorPropiedad(Long propiedadId) {
        return reservaRepo.findByPropiedadIdOrderByFechaInicioDesc(propiedadId);
    }

    public IngresosPropietarioDTO calcularIngresosPropietario(Long propietarioId, LocalDate inicio, LocalDate fin) {
        List<Reserva> reservasValidas = reservaRepo.findReservasParaIngresos(propietarioId, inicio, fin)
                .stream()
                .filter(r -> r.getEstado() != EstadoReserva.PENDIENTE)
                .filter(r -> r.getEstado() == EstadoReserva.CANCELADA || r.getSaldoPendiente().compareTo(BigDecimal.ZERO) == 0)
                .toList();

        BigDecimal ingresosEstadias = reservasValidas.stream()
                .filter(r -> r.getEstado() != EstadoReserva.CANCELADA)
                .map(r -> r.getMontoTotal().subtract(r.getDepositoRetenido()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
                
        BigDecimal ingresosPenalidades = reservasValidas.stream()
                .filter(r -> r.getMontoPenalidad() != null)
                .map(Reserva::getMontoPenalidad)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
                
        BigDecimal ingresosBrutos = ingresosEstadias.add(ingresosPenalidades);
        BigDecimal comisionTotal = reservasValidas.stream()
                .filter(r -> r.getEstado() != EstadoReserva.CANCELADA) 
                .map(Reserva::getComisionInmobiliaria)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return new IngresosPropietarioDTO(propietarioId, reservasValidas.size(), ingresosBrutos, comisionTotal);
    }

    public ReporteLiquidacionDTO generarLiquidacion(Long propietarioId, int mes, int anio) {
        List<Reserva> reservas = reservaRepo.buscarPorPropietarioYPeriodoPendientes(propietarioId, mes, anio)
                .stream()
                .filter(r -> r.getEstado() != EstadoReserva.PENDIENTE) // Afuera las pendientes
                .filter(r -> r.getEstado() == EstadoReserva.CANCELADA || r.getSaldoPendiente().compareTo(BigDecimal.ZERO) == 0) // Solo pagadas 100% o canceladas
                .toList();
                
        List<com.inmobiliaria.alquileres_temporarios.gastos.model.GastoMantenimiento> gastos = gastoRepo.buscarGastosPorPropietarioYPeriodoPendientes(propietarioId, mes, anio);

        ReporteLiquidacionDTO dto = new ReporteLiquidacionDTO();

        dto.setDetalleReservas(reservas.stream().map(r -> {
            BigDecimal estadiaPura = r.getMontoTotal().subtract(r.getDepositoRetenido());
            return new ReservaDetalleDTO(
                r.getFechaInicio(), r.getPropiedad().getDireccion(),
                r.getInquilino() != null ? r.getInquilino() : "Inquilino Final",
                java.time.temporal.ChronoUnit.DAYS.between(r.getFechaInicio(), r.getFechaFin()),
                estadiaPura); 
        }).toList());

        dto.setDetalleGastos(gastos.stream().map(g -> new GastoDetalleDTO(g.getConcepto(), g.getMonto())).toList());

        BigDecimal estadias = reservas.stream()
                .filter(r -> r.getEstado() != EstadoReserva.CANCELADA) // No sumamos estadía de canceladas
                .map(r -> r.getMontoTotal().subtract(r.getDepositoRetenido()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
                
        BigDecimal penalidades = reservas.stream().filter(r -> r.getMontoPenalidad() != null).map(Reserva::getMontoPenalidad).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal comision = reservas.stream().filter(r -> r.getEstado() != EstadoReserva.CANCELADA).map(Reserva::getComisionInmobiliaria).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalGastos = gastos.stream().map(g -> g.getMonto()).reduce(BigDecimal.ZERO, BigDecimal::add);

        dto.setIngresosEstadias(estadias);
        dto.setIngresosPenalidades(penalidades);
        dto.setTotalIngresosBrutos(estadias.add(penalidades));
        dto.setComisionAgencia(comision);
        dto.setGastosMantenimiento(totalGastos);
        dto.setTotalALiquidar(dto.getTotalIngresosBrutos().subtract(comision).subtract(totalGastos));

        return dto;
    }

    @Transactional
    public Reserva finalizarReserva(Long reservaId) {
        Reserva reserva = reservaRepo.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        
        if (reserva.getEstado() == EstadoReserva.CANCELADA || reserva.getEstado() == EstadoReserva.FINALIZADA) {
            throw new IllegalStateException("La reserva ya se encuentra " + reserva.getEstado());
        }
        reserva.setEstado(EstadoReserva.FINALIZADA);
        
        Propiedad prop = reserva.getPropiedad();
        prop.setEstado("Disponible");
        propiedadRepo.save(prop);
        
        return reservaRepo.save(reserva);
    }
}