package com.inmobiliaria.alquileres_temporarios.gastos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.inmobiliaria.alquileres_temporarios.gastos.model.GastoMantenimiento;

public interface GastoRepository extends JpaRepository<GastoMantenimiento, Long> {
    List<GastoMantenimiento> findByPropiedadId(Long propiedadId);

    // MODIFICADO: Solo trae gastos NO liquidados
    @Query("SELECT g FROM GastoMantenimiento g WHERE g.propiedad.propietario.id = :propietarioId " +
       "AND EXTRACT(MONTH FROM g.fecha) = :mes " +
       "AND EXTRACT(YEAR FROM g.fecha) = :anio " +
       "AND g.liquidado = false")
    List<GastoMantenimiento> buscarGastosPorPropietarioYPeriodoPendientes(Long propietarioId, int mes, int anio);

}