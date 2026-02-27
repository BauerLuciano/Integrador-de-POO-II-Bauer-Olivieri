package com.inmobiliaria.propietarios.repository;

import com.inmobiliaria.propietarios.model.Propietario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropietarioRepository extends JpaRepository<Propietario, Long> {
    boolean existsByDni(String dni);
}