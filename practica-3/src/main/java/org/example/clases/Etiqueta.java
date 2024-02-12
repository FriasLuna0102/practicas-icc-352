package org.example.clases;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.example.services.EtiquetaServices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Etiqueta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String etiqueta;

    public Etiqueta(){

    }
    public Etiqueta(String etiqueta) {
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


    public List<Etiqueta> getListEtiqueta() {
        return EtiquetaServices.getInstancia().obtenerTodasLasEtiquetas();
    }

    static public Etiqueta buscarEtiquet(String nombre){
        List<Etiqueta> listEtiqueta = EtiquetaServices.getInstancia().findAllByNombre(nombre);

        for(Etiqueta etique: listEtiqueta){
            if(etique.getEtiqueta().equals(nombre)){
                return etique;
            }
        }

        return null;
    }
}
