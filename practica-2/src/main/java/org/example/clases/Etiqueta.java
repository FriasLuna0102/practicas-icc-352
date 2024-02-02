package org.example.clases;

import java.util.ArrayList;
import java.util.List;

public class Etiqueta {
    long id;
    String etiqueta;

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

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }


    static List<Etiqueta> etiquetas = new ArrayList<>();

    public static List<Etiqueta> setEtiqueta(Etiqueta etiqueta) {
        etiquetas.add(etiqueta);

        return etiquetas;
    }


    public static List<Etiqueta> getEtiquetas() {
        return etiquetas;
    }



}
