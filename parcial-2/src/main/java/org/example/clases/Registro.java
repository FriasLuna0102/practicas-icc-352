package org.example.clases;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class Registro implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;

    private String sector;
    private String nivelEscolar;
    @OneToOne
    private Usuario usuario;
    private double altitud;
    private double longitud;

    private boolean estado;


    public Registro(String nombre, String sector, String nivelEscolar, Usuario usuario, double latitud, double longitud, boolean estado) {
        this.nombre = nombre;
        this.sector = sector;
        this.nivelEscolar = nivelEscolar;
        this.usuario = usuario;
        this.altitud = latitud;
        this.longitud = longitud;
        this.estado = estado;
    }

    public Registro() {

    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Registro(boolean estado) {

        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getNivelEscolar() {
        return nivelEscolar;
    }

    public void setNivelEscolar(String nivelEscolar) {
        this.nivelEscolar = nivelEscolar;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public double getAltitud() {
        return altitud;
    }

    public void setAltitud(double latitud) {
        this.altitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
