package org.example.clases;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Articulo {

    private static long contador = -1;

    static String id;
    String titulo;
    String cuerpo;
    Usuario autor;
    Date fecha;
    List<Comentario> listaComentarios;
    List<Etiqueta> listaEtiquetas;

    public Articulo(String id, String titulo, String cuerpo, Usuario autor, Date fecha, List<Comentario> listaComentarios, List<Etiqueta> listaEtiquetas) {
        this.id = UUID.randomUUID().toString();
        this.titulo = titulo;
        this.cuerpo = cuerpo;
        this.autor = autor;
        this.fecha = fecha;
        this.listaComentarios = listaComentarios;
        this.listaEtiquetas = listaEtiquetas;
    }

    static public String getId() {
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
        articulos.add(articulo);
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
        String nombreArchivo = id + ".html"; // Nombre único del archivo
        String rutaArchivo = ruta + File.separator + nombreArchivo; // Ruta completa del archivo

        FileWriter writer = new FileWriter(rutaArchivo);
        writer.write(contenidoHTML);
        writer.close();
    }


    static public boolean eliminarArti(List<Articulo> lista, String title) {
        int i = 0;
        for (i = 0; i < lista.size(); i++) {
            Articulo articulo = lista.get(i);

            if (Objects.equals(articulo.titulo, title)) {
                //System.out.println(articulo.getTitulo());
                lista.remove(i);
                i=0;
                return true;
            }
        }
        System.out.println("No se encontro.");
        return false;
    }


    static public Articulo obtenerArticuloPorId(String idEn){
        for(Articulo articulo: articulos){
            if(articulo.id.equals(idEn)){
                return articulo;
            }
        }
        return null; // Devuelve null si no se encuentra ningún artículo con ese ID
    }
}
