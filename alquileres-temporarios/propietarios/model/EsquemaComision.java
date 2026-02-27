package com.inmobiliaria.propietarios.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_esquema")
// Magia de Jackson para que el JSON se convierta en la clase correcta
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "tipo_esquema")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ComissionFija.class, name = "FIJA"),
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