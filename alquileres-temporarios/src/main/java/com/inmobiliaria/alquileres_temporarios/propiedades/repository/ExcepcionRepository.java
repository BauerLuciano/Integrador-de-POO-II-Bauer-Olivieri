package com.inmobiliaria.alquileres_temporarios.propiedades.repository;

import com.inmobiliaria.alquileres_temporarios.propiedades.model.ExcepcionCalendario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExcepcionRepository extends JpaRepository<ExcepcionCalendario, Long> {
    List<ExcepcionCalendario> findByPropiedadId(Long propiedadId);
}