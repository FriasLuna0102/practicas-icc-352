package org.example.clases;

import java.util.ArrayList;
import java.util.List;

public class Etiqueta {
    private long id;
    private String etiqueta;

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
