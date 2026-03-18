package com.inmobiliaria.alquileres_temporarios.reservas.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.inmobiliaria.alquileres_temporarios.propiedades.model.Propiedad;
import com.inmobiliaria.alquileres_temporarios.propiedades.repository.PropiedadRepository;
import com.inmobiliaria.alquileres_temporarios.propiedades.service.PropiedadService;
import com.inmobiliaria.alquileres_temporarios.propietarios.model.politicas.PoliticaCancelacion;
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

    @Transactional
    public Reserva crearReserva(Reserva reserva) {
        if (reserva.getFechaInicio().isAfter(reserva.getFechaFin()) || reserva.getFechaInicio().isEqual(reserva.getFechaFin())) {
            throw new IllegalArgumentException("Las fechas de la reserva son inválidas.");
        }

        Propiedad propiedad = propiedadRepo.findById(reserva.getPropiedad().getId())
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada"));
        reserva.setPropiedad(propiedad);

        boolean libreDeMantenimiento = propiedadService.verificarDisponibilidad(propiedad.getId(), reserva.getFechaInicio(), reserva.getFechaFin());
        if (!libreDeMantenimiento) {
            throw new RuntimeException("La propiedad está en mantenimiento en esas fechas.");
        }

        List<Reserva> reservasExistentes = reservaRepo.findByPropiedadId(propiedad.getId());
        boolean solapada = reservasExistentes.stream()
                .anyMatch(r -> r.seSolapaCon(reserva.getFechaInicio(), reserva.getFechaFin()));
        
        if (solapada) {
            throw new RuntimeException("La propiedad ya está reservada por otro cliente en esas fechas.");
        }

        reserva.calcularCostos();
        propiedad.setEstado("Alquilada");
        propiedadRepo.save(propiedad);

        return reservaRepo.save(reserva);
    }

    @Transactional
    public Reserva registrarPago(Long reservaId, Pago nuevoPago) {
        Reserva reserva = reservaRepo.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        reserva.registrarPago(nuevoPago);

        return reservaRepo.save(reserva);
    }
    
    public BigDecimal consultarSaldo(Long reservaId) {
        Reserva reserva = reservaRepo.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        return reserva.getSaldoPendiente();
    }

    // --- ACTUALIZADO PARA HU-10: CÁLCULO DE PENALIDAD CON STRATEGY ---
    @Transactional
    public Reserva cancelarReserva(Long reservaId) {
        Reserva reserva = reservaRepo.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        if (reserva.getEstado() == EstadoReserva.CANCELADA) {
            throw new IllegalStateException("La reserva ya se encuentra cancelada.");
        }

        // 1. Obtener la estrategia de cancelación de la propiedad
        PoliticaCancelacion politica = reserva.getPropiedad().getPoliticaCancelacion();
        
        // 2. Calcular la penalidad usando el Strategy (pasando la fecha de hoy)
        BigDecimal penalidad = politica.calcularPenalidad(reserva, LocalDate.now());
        
        // 3. Setear valores y guardar
        reserva.setMontoPenalidad(penalidad);
        reserva.setEstado(EstadoReserva.CANCELADA);
        
        return reservaRepo.save(reserva);
    }

    public List<Reserva> obtenerTodas() {
        return reservaRepo.findAll();
    }
}