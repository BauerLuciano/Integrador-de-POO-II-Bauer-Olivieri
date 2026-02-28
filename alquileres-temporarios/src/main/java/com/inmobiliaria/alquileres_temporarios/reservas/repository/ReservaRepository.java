package com.inmobiliaria.alquileres_temporarios.reservas.repository;

import com.inmobiliaria.alquileres_temporarios.reservas.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByPropiedadId(Long propiedadId);
}