package com.inmobiliaria.alquileres_temporarios.propietarios.controller;

import com.inmobiliaria.alquileres_temporarios.propietarios.dto.LiquidacionResponse;
import com.inmobiliaria.alquileres_temporarios.propietarios.service.LiquidacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/liquidaciones")
@RequiredArgsConstructor
public class LiquidacionController {

    private final LiquidacionService service;

    @GetMapping("/propietario/{id}")
    public LiquidacionResponse getLiquidacion(
            @PathVariable Long id,
            @RequestParam int mes,
            @RequestParam int anio) {
        return service.calcularLiquidacion(id, mes, anio);
    }
}