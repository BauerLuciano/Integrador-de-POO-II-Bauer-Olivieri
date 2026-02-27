package com.inmobiliaria.alquileres_temporarios.propietarios.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_esquema")
// Magia de Jackson para que el JSON se convierta en la clase correcta
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "tipo_esquema")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ComisionFija.class, name = "FIJA"),
    @JsonSubTypes.Type(value = ComisionEscalonada.class, name = "ESCALONADA")
})
public abstract class EsquemaComision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Contrato del patrón Strategy
    public abstract BigDecimal calcularComision(BigDecimal montoBase);

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}