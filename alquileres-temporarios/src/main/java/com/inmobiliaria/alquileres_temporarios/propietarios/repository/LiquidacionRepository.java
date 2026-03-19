package com.inmobiliaria.alquileres_temporarios.propietarios.repository;

import com.inmobiliaria.alquileres_temporarios.propietarios.model.Liquidacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LiquidacionRepository extends JpaRepository<Liquidacion, Long> {
    List<Liquidacion> findByPropietarioIdOrderByFechaEmisionDesc(Long propietarioId);
}