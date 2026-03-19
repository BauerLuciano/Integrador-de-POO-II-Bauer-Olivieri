package com.inmobiliaria.alquileres_temporarios.propiedades.repository;

import com.inmobiliaria.alquileres_temporarios.propiedades.model.Propiedad;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PropiedadRepository extends JpaRepository<Propiedad, Long> {
	@Query("SELECT p FROM Propiedad p " +
           "LEFT JOIN FETCH p.propietario " +
           "LEFT JOIN FETCH p.politicaCancelacion")
    List<Propiedad> findAllOptimizadas();
}