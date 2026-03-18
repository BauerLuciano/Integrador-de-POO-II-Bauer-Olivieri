package com.inmobiliaria.alquileres_temporarios.gastos.controller;

import com.inmobiliaria.alquileres_temporarios.gastos.model.GastoMantenimiento;
import com.inmobiliaria.alquileres_temporarios.gastos.service.GastoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gastos")
@RequiredArgsConstructor
public class GastoController {

    private final GastoService service;

    @PostMapping
    public ResponseEntity<GastoMantenimiento> crearGasto(@RequestBody GastoMantenimiento gasto) {
        return ResponseEntity.ok(service.registrarGasto(gasto));
    }

    @GetMapping("/propiedad/{propiedadId}")
    public ResponseEntity<List<GastoMantenimiento>> listarPorPropiedad(@PathVariable Long propiedadId) {
        return ResponseEntity.ok(service.listarPorPropiedad(propiedadId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarGasto(@PathVariable Long id) {
        service.eliminarGasto(id);
        return ResponseEntity.noContent().build();
    }
}