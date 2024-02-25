package org.example.clases;

import jakarta.persistence.*;
import org.example.services.ArticuloServices;
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

    @ManyToMany(mappedBy = "listaEtiquetas")
    private List<Articulo> articulos;

    public Etiqueta(){

    }

    private static Etiqueta instancia;

    public static Etiqueta getInstancia(){
        if(instancia==null){
            instancia = new Etiqueta();
        }
        return instancia;
    }
    public Etiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public long getId() {
        return id;
    }

    public List<Articulo> getListArticulos() {
        return articulos;
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
