package com.inmobiliaria.alquileres_temporarios.propiedades.controller;

import com.inmobiliaria.alquileres_temporarios.propiedades.model.ExcepcionCalendario;
import com.inmobiliaria.alquileres_temporarios.propiedades.model.Propiedad;
import com.inmobiliaria.alquileres_temporarios.propiedades.service.PropiedadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/propiedades")
@RequiredArgsConstructor
public class PropiedadController {

    private final PropiedadService service;

    @PostMapping
    public Propiedad crearPropiedad(@RequestBody Propiedad propiedad) {
        // Ahora sí pasa por tu validación de precios antes de ir a la DB
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
}