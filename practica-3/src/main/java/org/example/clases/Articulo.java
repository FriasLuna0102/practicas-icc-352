package org.example.clases;

import jakarta.persistence.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

@Entity
public class Articulo implements Serializable {


    private static long contador = 0;

    @Id
    private String id;
    private String titulo;
    private String cuerpo;
    @OneToOne
    private Usuario autor;
    private Date fecha;
    @OneToMany
    private List<Comentario> listaComentarios;

    @OneToMany
    private List<Etiqueta> listaEtiquetas;

    public Articulo(){

    }

    public Articulo(String titulo, String cuerpo, Usuario autor, Date fecha, List<Comentario> listaComentarios, List<Etiqueta> listaEtiquetas) {
        this.id = String.valueOf(++contador);
        this.titulo = titulo;
        this.cuerpo = cuerpo;
        this.autor = autor;
        this.fecha = fecha;
        this.listaComentarios = listaComentarios;
        this.listaEtiquetas = listaEtiquetas;
    }

    public String getId() {
        return id;
    }

   /* static public long setId() {
        ++contador;
        return contador;
    }
*/
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public List<Comentario> getListaComentarios() {
        return listaComentarios;
    }

    public void setListaComentarios(List<Comentario> listaComentarios) {
        this.listaComentarios = listaComentarios;
    }

    public List<Etiqueta> getListaEtiquetas() {
        return listaEtiquetas;
    }

    public void setListaEtiquetas(List<Etiqueta> listaEtiquetas) {
        this.listaEtiquetas = listaEtiquetas;
    }

    //Metodo para eliminar un articulo de una lista mediante el titulo.
    static public boolean eliminarArti(List<Articulo> lista, String id) {
        int i = 0;
        for (i = 0; i < lista.size(); i++) {
            Articulo articulo = lista.get(i);

            if (Objects.equals(articulo.getId(), id)) {
                lista.remove(i);
                i=0;
                return true;
            }
        }
        return false;
    }



}
