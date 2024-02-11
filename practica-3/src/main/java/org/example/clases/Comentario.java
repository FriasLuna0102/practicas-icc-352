package org.example.clases;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Comentario implements Serializable {


    private static long contador = 0;

    @Id
    private String id;
    private String comentario;

    @OneToOne
    private Usuario autor;

    @OneToOne
    private Articulo articulo;

    public Comentario (){

    }

    public Comentario(String comentario, Usuario autor, Articulo articulo) {
        this.id = String.valueOf(++contador);

        this.comentario = comentario;
        this.autor = autor;
        this.articulo = articulo;
    }


    public String getId() {
        return id;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }


    static List<Comentario> comentarios = new ArrayList<>();

    public static List<Comentario> setComentario(Comentario comentario) {
        comentarios.add(comentario);
        return comentarios;
    }

    public static List<Comentario> getComentarios() {
        return comentarios;
    }

    //Buscar comentarios por id
    static public Comentario buscarComentarioId(String id){
        for (Comentario coment : comentarios){
            if(coment.getId().equals(id)){
                return coment;
            }
        }
        return null;
    }

    //Buscar los comentarios relacionado a un articulo.
    static public List<Comentario> buscarComentPorArticulo(Articulo articulo){
        List<Comentario> listForArticulo = new ArrayList<>();
        for(Comentario cometarios : comentarios){
            if(cometarios.getArticulo().getId().equals(articulo.getId())){
                listForArticulo.add(cometarios);
            }
        }

        return listForArticulo;
    }
}
