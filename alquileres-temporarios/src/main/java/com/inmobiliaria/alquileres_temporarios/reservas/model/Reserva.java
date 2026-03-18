package com.inmobiliaria.alquileres_temporarios.reservas.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.inmobiliaria.alquileres_temporarios.propiedades.model.BloqueoCalendario;
import com.inmobiliaria.alquileres_temporarios.propiedades.model.Propiedad;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString(exclude = "pagos") 
public class Reserva implements BloqueoCalendario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    
    private BigDecimal montoTotal = BigDecimal.ZERO;
    private BigDecimal depositoRetenido = BigDecimal.ZERO;
    private BigDecimal comisionInmobiliaria = BigDecimal.ZERO;
    
    // --- NUEVO CAMPO PARA PENALIDAD (HU-10) ---
    private BigDecimal montoPenalidad = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoReserva estado = EstadoReserva.ACTIVA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propiedad_id")
    private Propiedad propiedad;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pago> pagos = new ArrayList<>();

    // --- MÉTODOS DE NEGOCIO ---

    public boolean seSolapaCon(LocalDate otraFechaInicio, LocalDate otraFechaFin) {
        if (this.estado == EstadoReserva.CANCELADA) {
            return false; 
        }
        return otraFechaInicio.isBefore(this.fechaFin) && otraFechaFin.isAfter(this.fechaInicio);
    }

    public void registrarPago(Pago nuevoPago) {
        if (nuevoPago.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto del pago debe ser mayor a cero.");
        }
        
        BigDecimal saldoPendiente = getSaldoPendiente();
        if (nuevoPago.getMonto().compareTo(saldoPendiente) > 0) {
            throw new IllegalStateException("El pago supera el saldo pendiente de la reserva. Saldo actual: $" + saldoPendiente);
        }
        
        this.pagos.add(nuevoPago);
        nuevoPago.setReserva(this); 
    }

    public BigDecimal getSaldoPendiente() {
        BigDecimal totalPagado = pagos.stream()
                .map(Pago::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return this.montoTotal.subtract(totalPagado);
    }

    public void calcularCostos() {
        long noches = ChronoUnit.DAYS.between(fechaInicio, fechaFin);
        
        BigDecimal precioPorNoche = BigDecimal.valueOf(propiedad.getPrecioPorNoche());
        this.montoTotal = precioPorNoche.multiply(BigDecimal.valueOf(noches));
        
        BigDecimal porcentajeDepo = BigDecimal.valueOf(propiedad.getPorcentajeDeposito())
                                              .divide(BigDecimal.valueOf(100));
        this.depositoRetenido = this.montoTotal.multiply(porcentajeDepo);
        
        this.comisionInmobiliaria = propiedad.getPropietario().calcularComision(this.montoTotal);
    }
}