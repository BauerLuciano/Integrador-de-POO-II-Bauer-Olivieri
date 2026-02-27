package com.inmobiliaria.alquileres_temporarios.propiedades.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

import com.inmobiliaria.alquileres_temporarios.propietarios.model.Propietario;

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

    @ManyToOne(fetch = FetchType.EAGER) 
    @JoinColumn(name = "propietario_id")
    private Propietario propietario;

    @Transient 
    private List<BloqueoCalendario> bloqueos = new ArrayList<>();

    public boolean estaDisponible(java.time.LocalDate inicio, java.time.LocalDate fin) {
        return bloqueos.stream().noneMatch(b -> b.seSolapaCon(inicio, fin));
    }
}