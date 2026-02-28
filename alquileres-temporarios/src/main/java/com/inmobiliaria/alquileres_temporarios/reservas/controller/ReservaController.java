package com.inmobiliaria.alquileres_temporarios.reservas.controller;

import com.inmobiliaria.alquileres_temporarios.reservas.model.Reserva;
import com.inmobiliaria.alquileres_temporarios.reservas.service.ReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService service;

    @PostMapping
    public ResponseEntity<?> crearReserva(@RequestBody Reserva reserva) {
        try {
            Reserva nuevaReserva = service.crearReserva(reserva);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaReserva);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}