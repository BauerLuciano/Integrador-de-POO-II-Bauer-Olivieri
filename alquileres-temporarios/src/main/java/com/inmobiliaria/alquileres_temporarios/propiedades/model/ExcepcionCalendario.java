package com.inmobiliaria.alquileres_temporarios.propiedades.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class ExcepcionCalendario implements BloqueoCalendario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String motivo;

    @ManyToOne
    private Propiedad propiedad;
}