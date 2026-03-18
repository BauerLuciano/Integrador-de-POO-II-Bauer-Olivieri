package com.inmobiliaria.alquileres_temporarios.reservas.repository;

import com.inmobiliaria.alquileres_temporarios.reservas.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByPropiedadId(Long propiedadId);

    @Query("SELECT r FROM Reserva r WHERE r.propiedad.propietario.id = :propietarioId " +
       "AND EXTRACT(MONTH FROM r.fechaInicio) = :mes " +
       "AND EXTRACT(YEAR FROM r.fechaInicio) = :anio")
    List<Reserva> buscarPorPropietarioYPeriodo(Long propietarioId, int mes, int anio);


}
