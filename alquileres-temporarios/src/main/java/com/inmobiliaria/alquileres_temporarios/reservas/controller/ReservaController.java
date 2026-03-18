package com.inmobiliaria.alquileres_temporarios.reservas.controller;

import com.inmobiliaria.alquileres_temporarios.reservas.model.Pago;
import com.inmobiliaria.alquileres_temporarios.reservas.model.Reserva;
import com.inmobiliaria.alquileres_temporarios.reservas.service.ReservaService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200") 
public class ReservaController {

    private final ReservaService service;

    @Autowired 
    private ReservaService reservaService; 

    @PostMapping
    public ResponseEntity<?> crearReserva(@RequestBody Reserva reserva) {
        try {
            Reserva nuevaReserva = service.crearReserva(reserva);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaReserva);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/pagos")
    public ResponseEntity<?> registrarPago(@PathVariable Long id, @RequestBody Pago pago) {
        try {
            Reserva reservaActualizada = service.registrarPago(id, pago);
            return ResponseEntity.ok(reservaActualizada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}/saldo")
    public ResponseEntity<?> consultarSaldo(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.consultarSaldo(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- NUEVO ENDPOINT DE CANCELACIÓN ---
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarReserva(@PathVariable Long id) {
        try {
            Reserva reservaCancelada = service.cancelarReserva(id);
            return ResponseEntity.ok(reservaCancelada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public List<Reserva> listarTodas() {
        return reservaService.obtenerTodas(); // Ahora Java sí sabe qué es esto
    }
}