package com.inmobiliaria.propietarios.controller;

import com.inmobiliaria.propietarios.model.Propietario;
import com.inmobiliaria.propietarios.service.PropietarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/propietarios")
public class PropietarioController {

    private final PropietarioService service;

    public PropietarioController(PropietarioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Propietario> crearPropietario(@RequestBody Propietario propietario) {
        Propietario nuevo = service.guardarPropietario(propietario);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Propietario>> listarPropietarios() {
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Propietario> obtenerPropietario(@PathVariable Long id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Propietario> actualizarPropietario(@PathVariable Long id, @RequestBody Propietario propietario) {
        return service.obtenerPorId(id).map(existente -> {
            propietario.setId(id); // Aseguramos que se actualice el correcto
            return ResponseEntity.ok(service.guardarPropietario(propietario));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPropietario(@PathVariable Long id) {
        try {
            service.eliminarPropietario(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}