package com.inmobiliaria.alquileres_temporarios.propiedades.model;

import java.util.ArrayList;
import java.util.List;

import com.inmobiliaria.alquileres_temporarios.propietarios.model.Propietario;
import com.inmobiliaria.alquileres_temporarios.propietarios.model.politicas.PoliticaCancelacion;
import com.inmobiliaria.alquileres_temporarios.propietarios.model.politicas.PoliticaEstricta;
import com.inmobiliaria.alquileres_temporarios.propietarios.model.politicas.PoliticaFlexible;

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
    
    public enum TipoPolitica {
        ESTRICTA, FLEXIBLE
    }

    // Corregimos el nombre para que coincida con tu columna de la DB
    @Enumerated(EnumType.STRING)
    @Column(name = "politica_cancelacion", nullable = false) 
    private TipoPolitica tipoPolitica = TipoPolitica.FLEXIBLE;

    @ManyToOne(fetch = FetchType.EAGER) 
    @JoinColumn(name = "propietario_id")
    private Propietario propietario;

    @Transient 
    private List<BloqueoCalendario> bloqueos = new ArrayList<>();

    public boolean estaDisponible(java.time.LocalDate inicio, java.time.LocalDate fin) {
        return bloqueos.stream().noneMatch(b -> b.seSolapaCon(inicio, fin));
    }

    public PoliticaCancelacion obtenerEstrategiaCancelacion() {
        if (this.tipoPolitica != null && this.tipoPolitica == TipoPolitica.ESTRICTA) {
            return new PoliticaEstricta();
        }
        return new PoliticaFlexible();
    }
}