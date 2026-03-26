package com.inmobiliaria.alquileres_temporarios.gastos.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.inmobiliaria.alquileres_temporarios.gastos.model.GastoMantenimiento;

public interface GastoRepository extends JpaRepository<GastoMantenimiento, Long> {
    
    List<GastoMantenimiento> findByPropiedadId(Long propiedadId);

    @Query("SELECT g FROM GastoMantenimiento g WHERE g.propiedad.propietario.id = :propietarioId " +
       "AND EXTRACT(MONTH FROM g.fecha) = :mes " +
       "AND EXTRACT(YEAR FROM g.fecha) = :anio " +
       "AND g.liquidado = false")
    List<GastoMantenimiento> buscarGastosPorPropietarioYPeriodoPendientes(
        @Param("propietarioId") Long propietarioId, 
        @Param("mes") int mes, 
        @Param("anio") int anio
    );

    @Query("SELECT g FROM GastoMantenimiento g WHERE g.propiedad.propietario.id = :propietarioId " +
           "AND g.fecha BETWEEN :inicio AND :fin")
    List<GastoMantenimiento> buscarGastosPorPropietarioYFechas(
        @Param("propietarioId") Long propietarioId, 
        @Param("inicio") LocalDate inicio, 
        @Param("fin") LocalDate fin
    );
}