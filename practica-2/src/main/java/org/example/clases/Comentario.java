package org.example.clases;

import java.util.ArrayList;
import java.util.List;

public class Comentario {
    private static long contador = 0;
    String id;
    String comentario;
    Usuario autor;
    Articulo articulo;

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
            if(coment.id.equals(id)){
                return coment;
            }
        }
        return null;
    }

    //Buscar los comentarios relacionado a un articulo.
    static public List<Comentario> buscarComentPorArticulo(Articulo articulo){
        List<Comentario> listForArticulo = new ArrayList<>();
        for(Comentario cometarios : comentarios){
            if(cometarios.articulo.getId().equals(articulo.id)){
                listForArticulo.add(cometarios);
            }
        }

        return listForArticulo;
    }
}
