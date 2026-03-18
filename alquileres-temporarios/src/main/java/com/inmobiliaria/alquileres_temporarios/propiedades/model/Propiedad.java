package com.inmobiliaria.alquileres_temporarios.propiedades.model;

import java.util.ArrayList;
import java.util.List;

import com.inmobiliaria.alquileres_temporarios.propietarios.model.Propietario;
import com.inmobiliaria.alquileres_temporarios.propietarios.model.politicas.PoliticaCancelacion;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data 
public class Propiedad {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String direccion;
    private Double precioPorNoche;
    private Double porcentajeDeposito;
    
    @ManyToOne(fetch = FetchType.EAGER) 
    @JoinColumn(name = "propietario_id")
    private Propietario propietario;

    @Column(name = "estado")
    private String estado = "Disponible";

    @ManyToOne(cascade = jakarta.persistence.CascadeType.ALL)
    @JoinColumn(name = "politica_cancelacion_id")
    private PoliticaCancelacion politicaCancelacion;

    @Transient 
    private List<BloqueoCalendario> bloqueos = new ArrayList<>();

    public boolean estaDisponible(java.time.LocalDate inicio, java.time.LocalDate fin) {
        return bloqueos.stream().noneMatch(b -> b.seSolapaCon(inicio, fin));
    }
}