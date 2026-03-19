package com.inmobiliaria.alquileres_temporarios.propietarios.controller;

import com.inmobiliaria.alquileres_temporarios.propietarios.dto.LiquidacionResponse;
import com.inmobiliaria.alquileres_temporarios.propietarios.model.Liquidacion;
import com.inmobiliaria.alquileres_temporarios.propietarios.service.LiquidacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/liquidaciones")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class LiquidacionController {

    private final LiquidacionService service;

    @GetMapping("/propietario/{id}")
    public LiquidacionResponse getLiquidacion(
            @PathVariable Long id,
            @RequestParam int mes,
            @RequestParam int anio) {
        return service.calcularLiquidacion(id, mes, anio);
    }

    @PostMapping("/propietario/{id}/confirmar")
    public Liquidacion confirmarLiquidacion(
            @PathVariable Long id,
            @RequestParam int mes,
            @RequestParam int anio) {
        return service.confirmarLiquidacion(id, mes, anio);
    }

    @GetMapping("/propietario/{id}/historial")
    public List<Liquidacion> getHistorialLiquidaciones(@PathVariable Long id) {
        return service.obtenerHistorial(id);
    }
}