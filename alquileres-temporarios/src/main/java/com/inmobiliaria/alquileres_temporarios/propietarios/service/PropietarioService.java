package com.inmobiliaria.alquileres_temporarios.propietarios.service;

import com.inmobiliaria.alquileres_temporarios.propietarios.model.Propietario;
import com.inmobiliaria.alquileres_temporarios.propietarios.repository.PropietarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class PropietarioService {

    private final PropietarioRepository repository;

    public PropietarioService(PropietarioRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Propietario guardarPropietario(Propietario propietario) {
        if (propietario.getId() == null) {
            if (repository.existsByDni(propietario.getDni())) {
                throw new IllegalArgumentException("Ya existe un propietario con el DNI: " + propietario.getDni());
            }
        } 
        else {
            if (repository.existsByDniAndIdNot(propietario.getDni(), propietario.getId())) {
                throw new IllegalArgumentException("El DNI " + propietario.getDni() + " ya está registrado en otro propietario distinto.");
            }
        }
        return repository.save(propietario);
    }

    public List<Propietario> obtenerTodos() {
        return repository.findAll();
    }

    public Optional<Propietario> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public void eliminarPropietario(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("El propietario con ID " + id + " no existe.");
        }
        repository.deleteById(id);
    }
}