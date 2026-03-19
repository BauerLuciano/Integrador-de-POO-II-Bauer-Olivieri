package com.inmobiliaria.alquileres_temporarios.reservas.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.inmobiliaria.alquileres_temporarios.propietarios.dto.IngresosPropietarioDTO;
import com.inmobiliaria.alquileres_temporarios.reservas.dto.ReporteLiquidacionDTO;
import com.inmobiliaria.alquileres_temporarios.reservas.model.Pago;
import com.inmobiliaria.alquileres_temporarios.reservas.model.Reserva;
import com.inmobiliaria.alquileres_temporarios.reservas.service.ReservaService;
import com.inmobiliaria.alquileres_temporarios.reservas.service.PdfService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200") 
public class ReservaController {

    private final ReservaService service;
    private final PdfService pdfService;

    @PostMapping
    public ResponseEntity<?> crearReserva(@RequestBody Reserva reserva) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.crearReserva(reserva));
        } catch (Exception e) { 
            return ResponseEntity.badRequest().body(e.getMessage()); 
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerReservaPorId(@PathVariable Long id) {
        try { 
            return ResponseEntity.ok(service.obtenerPorId(id));
        } catch (Exception e) { 
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reserva no encontrada"); 
        }
    }

    @GetMapping("/liquidacion")
    public ResponseEntity<ReporteLiquidacionDTO> obtenerLiquidacion(
            @RequestParam Long id, @RequestParam int mes, @RequestParam int anio) {
        return ResponseEntity.ok(service.generarLiquidacion(id, mes, anio));
    }

    @GetMapping("/liquidacion/pdf")
    public void exportarPdf(@RequestParam Long id, @RequestParam int mes, @RequestParam int anio, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=liquidacion_" + id + ".pdf";
        response.setHeader(headerKey, headerValue);

        ReporteLiquidacionDTO data = service.generarLiquidacion(id, mes, anio);
        pdfService.exportarLiquidacion(response, data);
    }

    @GetMapping("/historial/propiedad/{propiedadId}")
    public ResponseEntity<List<Reserva>> historialPropiedad(@PathVariable Long propiedadId) {
        return ResponseEntity.ok(service.obtenerHistorialPorPropiedad(propiedadId));
    }

    @GetMapping("/ingresos/propietario/{propietarioId}")
    public ResponseEntity<IngresosPropietarioDTO> ingresosPropietario(
            @PathVariable Long propietarioId, @RequestParam LocalDate inicio, @RequestParam LocalDate fin) {
        return ResponseEntity.ok(service.calcularIngresosPropietario(propietarioId, inicio, fin));
    }

    @GetMapping
    public List<Reserva> listarTodas() { 
        return service.obtenerTodas(); 
    }

    @PostMapping("/{id}/pagos")
    public ResponseEntity<?> registrarPago(@PathVariable Long id, @RequestBody Pago pago) {
        try { 
            return ResponseEntity.ok(service.registrarPago(id, pago));
        } catch (Exception e) { 
            return ResponseEntity.badRequest().body(e.getMessage()); 
        }
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarReserva(
            @PathVariable Long id,
            @RequestParam(required = false) String motivo,
            @RequestParam(required = false) String detalle) {
        try { 
            return ResponseEntity.ok(service.cancelarReserva(id, motivo, detalle));
        } catch (Exception e) { 
            return ResponseEntity.badRequest().body(e.getMessage()); 
        }
    }
}