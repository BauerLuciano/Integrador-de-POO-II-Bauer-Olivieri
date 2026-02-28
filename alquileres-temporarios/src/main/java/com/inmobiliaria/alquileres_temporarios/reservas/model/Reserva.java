package com.inmobiliaria.alquileres_temporarios.reservas.model;

import com.inmobiliaria.alquileres_temporarios.propiedades.model.BloqueoCalendario;
import com.inmobiliaria.alquileres_temporarios.propiedades.model.Propiedad;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Data
public class Reserva implements BloqueoCalendario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    
    private BigDecimal montoTotal;
    private BigDecimal depositoRetenido;
    private BigDecimal comisionInmobiliaria;

    @ManyToOne
    private Propiedad propiedad;

    public void calcularCostos() {
        long noches = ChronoUnit.DAYS.between(fechaInicio, fechaFin);
        
        double total = noches * propiedad.getPrecioPorNoche();
        this.montoTotal = BigDecimal.valueOf(total);
        
        double deposito = total * (propiedad.getPorcentajeDeposito() / 100);
        this.depositoRetenido = BigDecimal.valueOf(deposito);
        
        this.comisionInmobiliaria = propiedad.getPropietario().calcularComision(this.montoTotal);
    }
}