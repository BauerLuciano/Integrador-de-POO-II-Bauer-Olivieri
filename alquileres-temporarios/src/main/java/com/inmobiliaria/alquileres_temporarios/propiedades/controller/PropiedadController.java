package com.inmobiliaria.alquileres_temporarios.propiedades.controller;

import com.inmobiliaria.alquileres_temporarios.propiedades.model.Propiedad;
import com.inmobiliaria.alquileres_temporarios.propiedades.repository.PropiedadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/propiedades")
public class PropiedadController {

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
}