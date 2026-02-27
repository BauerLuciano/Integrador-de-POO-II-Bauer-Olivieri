package com.inmobiliaria.alquileres_temporarios.propiedades.repository;

import com.inmobiliaria.alquileres_temporarios.propiedades.model.Propiedad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropiedadRepository extends JpaRepository<Propiedad, Long> {
}