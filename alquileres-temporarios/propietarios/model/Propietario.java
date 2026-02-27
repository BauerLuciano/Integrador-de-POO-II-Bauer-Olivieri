package com.inmobiliaria.propietarios.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class Propietario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dni;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "esquema_comision_id")
    private EsquemaComision esquemaComision;

    public Propietario() {}

    // Delegación (Strategy en acción)
    public BigDecimal calcularComision(BigDecimal montoReserva) {
        if (esquemaComision == null) {
            throw new IllegalStateException("El propietario no tiene esquema de comisión.");
        }
        return esquemaComision.calcularComision(montoReserva);
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public EsquemaComision getEsquemaComision() { return esquemaComision; }
    public void setEsquemaComision(EsquemaComision esquemaComision) { this.esquemaComision = esquemaComision; }
}