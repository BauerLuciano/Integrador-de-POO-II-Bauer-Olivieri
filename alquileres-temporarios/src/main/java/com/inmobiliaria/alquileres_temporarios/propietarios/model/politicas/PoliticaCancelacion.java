package com.inmobiliaria.alquileres_temporarios.propietarios.model.politicas;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.inmobiliaria.alquileres_temporarios.reservas.model.Reserva;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "politica_cancelacion")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_politica", discriminatorType = DiscriminatorType.STRING)

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "tipo")
@JsonSubTypes({
    @JsonSubTypes.Type(value = PoliticaFlexible.class, name = "FLEXIBLE"),
    @JsonSubTypes.Type(value = PoliticaEstricta.class, name = "ESTRICTA")
})

@Data
public abstract class PoliticaCancelacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public abstract BigDecimal calcularPenalidad(Reserva reserva, LocalDate fechaCancelacion);
}