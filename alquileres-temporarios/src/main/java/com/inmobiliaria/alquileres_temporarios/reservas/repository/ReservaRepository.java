package com.inmobiliaria.alquileres_temporarios.reservas.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.inmobiliaria.alquileres_temporarios.reservas.model.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    
    List<Reserva> findByPropiedadId(Long propiedadId);

    @Query("SELECT r FROM Reserva r WHERE r.propiedad.propietario.id = :propietarioId " +
       "AND EXTRACT(MONTH FROM r.fechaInicio) = :mes " +
       "AND EXTRACT(YEAR FROM r.fechaInicio) = :anio")
    List<Reserva> buscarPorPropietarioYPeriodo(Long propietarioId, int mes, int anio);

    List<Reserva> findByPropiedadIdOrderByFechaInicioDesc(Long propiedadId);

    @Query("SELECT r FROM Reserva r WHERE r.propiedad.propietario.id = :propietarioId AND r.fechaInicio >= :inicio AND r.fechaFin <= :fin AND r.estado != 'CANCELADA'")
    List<Reserva> findReservasParaIngresos(@Param("propietarioId") Long propietarioId, @Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);
}