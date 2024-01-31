package org.example.clases;

import java.util.ArrayList;
import java.util.List;

public class Comentario {

    long id;
    String comentario;
    Usuario autor;
    Articulo articulo;

    public Comentario(long id, String comentario, Usuario autor, Articulo articulo) {
        this.id = id;
        this.comentario = comentario;
        this.autor = autor;
        this.articulo = articulo;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}
