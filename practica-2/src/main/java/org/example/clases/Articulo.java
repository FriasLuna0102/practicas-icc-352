package org.example.clases;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Articulo {

    private static long contador = 0;

    long id;
    String titulo;
    String cuerpo;
    Usuario autor;
    Date fecha;
    List<Comentario> listaComentarios;
    List<Etiqueta> listaEtiquetas;

    public Articulo(long id, String titulo, String cuerpo, Usuario autor, Date fecha, List<Comentario> listaComentarios, List<Etiqueta> listaEtiquetas) {
        this.id = id;
        this.titulo = titulo;
        this.cuerpo = cuerpo;
        this.autor = autor;
        this.fecha = fecha;
        this.listaComentarios = listaComentarios;
        this.listaEtiquetas = listaEtiquetas;
    }

    static public long getId() {
        contador = setId() - 1;
        return contador;
    }

    static public long setId() {
        contador += 1;
        return contador;
    }

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
    static public List<Articulo> setArticulos(Articulo articulo){
        articulos.add(articulo);
        return articulos;
    }


    static public void generarPaginaHTML(Articulo articulo) throws IOException {
        String contenidoHTML = "<html><head><title>" + articulo.getTitulo() + "</title></head><body>"
                + "<h1>" + articulo.getTitulo() + "</h1>"
                + "<p>" + articulo.getCuerpo() + "</p>"
                + "</body></html>";

        // Guardar el HTML en un archivo temporal en el directorio de trabajo
        String rutaDirectorio = System.getProperty("user.dir"); // Obtener el directorio de trabajo
        String ruta = rutaDirectorio + "/src/main/resources/publico/temp";
        //String nombreArchivo = articulo.getId() + articulo.getTitulo().replaceAll("\\s+", "_") + ".html"; // Nombre único del archivo
        String nombreArchivo = articulo.getId() + ".html"; // Nombre único del archivo
        String rutaArchivo = ruta + File.separator + nombreArchivo; // Ruta completa del archivo

        FileWriter writer = new FileWriter(rutaArchivo);
        writer.write(contenidoHTML);
        writer.close();
    }





}
