package com.inmobiliaria.alquileres_temporarios.propiedades.controller;

import com.inmobiliaria.alquileres_temporarios.propiedades.model.Propiedad;
import com.inmobiliaria.alquileres_temporarios.propiedades.repository.PropiedadRepository;
import com.inmobiliaria.alquileres_temporarios.propiedades.service.PropiedadService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/propiedades")
@RequiredArgsConstructor
public class PropiedadController {

	private final PropiedadService service;

    @Autowired
    private PropiedadRepository repository;

    @PostMapping
    public Propiedad crearPropiedad(@RequestBody Propiedad propiedad) {
        return repository.save(propiedad);
    }

    @GetMapping
    public List<Propiedad> listarTodas() {
        return repository.findAll();
    }

	@GetMapping("/{id}/disponibilidad")
    public boolean consultar(@PathVariable Long id, 
                             @RequestParam LocalDate inicio, 
                             @RequestParam LocalDate fin) {
        return service.verificarDisponibilidad(id, inicio, fin);
    }
}