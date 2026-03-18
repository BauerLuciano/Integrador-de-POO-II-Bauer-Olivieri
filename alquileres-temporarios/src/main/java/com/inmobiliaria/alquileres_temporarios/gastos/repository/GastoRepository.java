package com.inmobiliaria.alquileres_temporarios.gastos.repository;

import com.inmobiliaria.alquileres_temporarios.gastos.model.GastoMantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GastoRepository extends JpaRepository<GastoMantenimiento, Long> {
    List<GastoMantenimiento> findByPropiedadId(Long propiedadId);

    @Query("SELECT g FROM GastoMantenimiento g WHERE g.propiedad.propietario.id = :propietarioId " +
       "AND EXTRACT(MONTH FROM g.fecha) = :mes " +
       "AND EXTRACT(YEAR FROM g.fecha) = :anio")
    List<GastoMantenimiento> buscarGastosPorPropietarioYPeriodo(Long propietarioId, int mes, int anio);

}