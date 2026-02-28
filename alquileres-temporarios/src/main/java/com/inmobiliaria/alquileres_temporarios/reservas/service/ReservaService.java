package com.inmobiliaria.alquileres_temporarios.reservas.service;

import com.inmobiliaria.alquileres_temporarios.propiedades.model.Propiedad;
import com.inmobiliaria.alquileres_temporarios.propiedades.repository.PropiedadRepository;
import com.inmobiliaria.alquileres_temporarios.propiedades.service.PropiedadService;
import com.inmobiliaria.alquileres_temporarios.reservas.model.Reserva;
import com.inmobiliaria.alquileres_temporarios.reservas.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepo;
    private final PropiedadRepository propiedadRepo;
    private final PropiedadService propiedadService; 

    public Reserva crearReserva(Reserva reserva) {
        // 1. Validar que las fechas tengan sentido
        if (reserva.getFechaInicio().isAfter(reserva.getFechaFin()) || reserva.getFechaInicio().isEqual(reserva.getFechaFin())) {
            throw new IllegalArgumentException("Las fechas de la reserva son inválidas.");
        }

        // 2. Buscar la propiedad en la base de datos
        Propiedad propiedad = propiedadRepo.findById(reserva.getPropiedad().getId())
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada"));
        reserva.setPropiedad(propiedad);

        // 3. CHECK DE DISPONIBILIDAD 
        // A. Verificamos que no haya un bloqueo por mantenimiento 
        boolean libreDeMantenimiento = propiedadService.verificarDisponibilidad(propiedad.getId(), reserva.getFechaInicio(), reserva.getFechaFin());
        if (!libreDeMantenimiento) {
            throw new RuntimeException("La propiedad está en mantenimiento en esas fechas.");
        }

        // B. Verificamos que no haya OTRA RESERVA en esas fechas
        List<Reserva> reservasExistentes = reservaRepo.findByPropiedadId(propiedad.getId());
        boolean solapada = reservasExistentes.stream()
                .anyMatch(r -> r.seSolapaCon(reserva.getFechaInicio(), reserva.getFechaFin()));
        
        if (solapada) {
            throw new RuntimeException("La propiedad ya está reservada por otro cliente en esas fechas.");
        }

        // 4. Calcular los costos y comisiones 
        reserva.calcularCostos();

        // 5. Guardar en PostgreSQL
        return reservaRepo.save(reserva);
    }
}