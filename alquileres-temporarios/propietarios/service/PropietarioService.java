package com.inmobiliaria.propietarios.service;

import com.inmobiliaria.propietarios.model.Propietario;
import com.inmobiliaria.propietarios.repository.PropietarioRepository;
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
        if (propietario.getId() == null && repository.existsByDni(propietario.getDni())) {
            throw new IllegalArgumentException("Ya existe un propietario con el DNI: " + propietario.getDni());
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