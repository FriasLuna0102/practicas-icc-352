package org.example.clases;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Etiqueta implements Serializable {

    @Id
    private long id;
    private String etiqueta;

    public Etiqueta(){

    }
    public Etiqueta(long id, String etiqueta) {
        this.id = id;
        this.etiqueta = etiqueta;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
