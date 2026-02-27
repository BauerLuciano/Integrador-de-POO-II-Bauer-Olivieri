package com.inmobiliaria.alquileres_temporarios.propietarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inmobiliaria.alquileres_temporarios.propietarios.model.Propietario;

@Repository
public interface PropietarioRepository extends JpaRepository<Propietario, Long> {
    boolean existsByDni(String dni);
}