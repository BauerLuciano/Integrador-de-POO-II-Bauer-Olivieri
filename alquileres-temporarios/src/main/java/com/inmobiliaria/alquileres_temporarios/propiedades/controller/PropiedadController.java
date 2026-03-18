package com.inmobiliaria.alquileres_temporarios.propiedades.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inmobiliaria.alquileres_temporarios.propiedades.model.ExcepcionCalendario;
import com.inmobiliaria.alquileres_temporarios.propiedades.model.Propiedad;
import com.inmobiliaria.alquileres_temporarios.propiedades.service.PropiedadService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/propiedades")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200") // Permite que Angular (4200) hable con Java (8080)
public class PropiedadController {

    private final PropiedadService service;

    @PostMapping
    public Propiedad crearPropiedad(@RequestBody Propiedad propiedad) {
        return service.guardarPropiedad(propiedad);
    }

    @GetMapping
    public List<Propiedad> listarTodas() {
        return service.obtenerTodas();
    }

    @GetMapping("/{id}/disponibilidad")
    public boolean consultar(@PathVariable Long id, 
                             @RequestParam LocalDate inicio, 
                             @RequestParam LocalDate fin) {
        return service.verificarDisponibilidad(id, inicio, fin);
    }

    @PostMapping("/{id}/bloqueos")
    public ExcepcionCalendario bloquear(@PathVariable Long id, 
                                        @RequestBody ExcepcionCalendario excepcion) {
        return service.bloquearFechas(id, 
                                      excepcion.getFechaInicio(), 
                                      excepcion.getFechaFin(), 
                                      excepcion.getMotivo());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPropiedad(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build(); 
    }

    @GetMapping("/{id}")
    public ResponseEntity<Propiedad> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Propiedad> actualizarPropiedad(@PathVariable Long id, @RequestBody Propiedad propiedad) {
        return ResponseEntity.ok(service.actualizar(id, propiedad));
    }
}