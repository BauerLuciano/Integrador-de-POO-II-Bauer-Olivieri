package com.inmobiliaria.alquileres_temporarios.propietarios.model;


import java.math.BigDecimal;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Propietario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dni;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    
    @Column(name = "activo")
    private Boolean activo = true;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "esquema_comision_id")
    private EsquemaComision esquemaComision;

    public BigDecimal calcularComision(BigDecimal montoReserva) {
        if (esquemaComision == null) {
            throw new IllegalStateException("El propietario no tiene esquema de comisión.");
        }
        return esquemaComision.calcularComision(montoReserva);
    }
}