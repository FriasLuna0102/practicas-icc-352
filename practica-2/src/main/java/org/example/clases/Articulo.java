package org.example.clases;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Articulo {

    private static long contador = 0;

    String id;
    String titulo;
    String cuerpo;
    Usuario autor;
    Date fecha;
    List<Comentario> listaComentarios;
    List<Etiqueta> listaEtiquetas;

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

    //Lista para almacenar articulos:
    static List<Articulo> articulos = new ArrayList<>();

    //Para obtener lista de articulos.
    static public List<Articulo> getArticulos(){
        return articulos;
    }

    //Para agregar articulos a la lista.
    static public void setArticulos(Articulo articulo){
        articulos.add(0,articulo);
    }


    //Metodo para eliminar un articulo de una lista mediante el titulo.
    static public boolean eliminarArti(List<Articulo> lista, String title) {
        int i = 0;
        for (i = 0; i < lista.size(); i++) {
            Articulo articulo = lista.get(i);

            if (Objects.equals(articulo.titulo, title)) {
                lista.remove(i);
                i=0;
                return true;
            }
        }
        return false;
    }


    //Metodo para obtener un articulo por el id.
    static public Articulo obtenerArticuloPorId(String idEn){
        for(Articulo articulo: articulos){
            if(articulo.id.equals(idEn)){
                return articulo;
            }
        }
        return null; // Devuelve null si no se encuentra ningún artículo con ese ID
    }

    static List<Usuario> listUsuarios = Usuario.getUsuarios();

    static public Usuario buscarUsuarios (String usuario){
        for(Usuario user: listUsuarios){
            if(user.nombre.equals(usuario)){
                return user;
            }
        }
        return null;
    }


    //Metodo para devolver un string de etiquetas en una lista.
    static public List<Etiqueta> devolverEtiqueta(String etiquetasString) {
        List<Etiqueta> listEtiquetas = new ArrayList<>();

        // Dividir el string en etiquetas individuales utilizando una coma como delimitador
        String[] etiquetasArray = etiquetasString.split(",");

        // Iterar sobre el array de etiquetas
        for (int i = 0; i < etiquetasArray.length; i++) {
            // Obtener la etiqueta y eliminar los espacios en blanco alrededor
            String etiqueta = etiquetasArray[i].trim();

            // Crear un nuevo objeto Etiqueta y añadirlo a la lista
            Etiqueta newEtiqueta = new Etiqueta(i + 1, etiqueta);
            listEtiquetas.add(newEtiqueta);
        }

        return listEtiquetas;
    }


    static public boolean eliminarArtiPorId(List<Articulo> lista, String id) {
        int i = 0;
        for (i = 0; i < lista.size(); i++) {
            Articulo articulo = lista.get(i);

            if (Objects.equals(articulo.id, id)) {
                lista.remove(i);
                i=0;
                return true;
            }
        }
        return false;
    }
}
