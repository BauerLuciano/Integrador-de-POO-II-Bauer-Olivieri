package com.inmobiliaria.alquileres_temporarios.propiedades.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data 
public class Propiedad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String direccion;
    private Double precioPorNoche;
    private Double porcentajeDeposito;
    private String politicaCancelacion;

    // Aquí Bauer (tu amigo) inyectará el Propietario en la HU-04
    // @ManyToOne ... private Propietario propietario;

    // Para la Iteración 1, usaremos una lista simple de bloqueos
    // (En la base de datos esto se mapeará con herencia en la HU-05/07)
    @Transient 
    private List<BloqueoCalendario> bloqueos = new ArrayList<>();

    // HU-06: El motor de disponibilidad
    public boolean estaDisponible(java.time.LocalDate inicio, java.time.LocalDate fin) {
        return bloqueos.stream().noneMatch(b -> b.seSolapaCon(inicio, fin));
    }
}