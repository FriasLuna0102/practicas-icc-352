package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.Articulo;
import org.example.clases.Comentario;
import org.example.clases.Etiqueta;
import org.example.clases.Usuario;
import org.example.controladores.GenerarId;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CrearArticulo extends ControladorClass{
    public CrearArticulo(Javalin app) {
        super(app);
    }

    List<Articulo> articulos = new ArrayList<>();
    @Override
    public void aplicarRutas() {

        app.post("/crearArticulo", cxt ->{

            //Lista de Etiquetas:
            List<Etiqueta> listaEtiquetas = new ArrayList<>();

            //Lista de comentarios:
            List<Comentario> listaComentarios = Comentario.getComentarios();

            //Lista de articulos
            List<Articulo> listaArticulos = Articulo.getArticulos();

            String titulo = cxt.formParam("titulo");
            String cuerpo = cxt.formParam("cuerpo");
            Usuario autor = cxt.sessionAttribute("currentUser");

            //Manejo de la fecha:
            // Suponiendo que el formato de fecha recibido es "yyyy-MM-dd"
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
            String fechaString = cxt.formParam("fecha");
            Date fecha;
            try {
                fecha = formatoFecha.parse(fechaString);
            } catch (ParseException e) {
                // Manejar la excepción si la cadena no está en el formato esperado
                e.printStackTrace();
                fecha = null; // O cualquier manejo que desees hacer en caso de error
            }
            String etiquetas = cxt.formParam("etiquetas");

            //Logica pra tomar las etiquetas y añadirla a la lista de etiquetas separadas por coma.
            int startIndex = 0;
            for (int i = 0; i < etiquetas.length(); i++) {
                if (etiquetas.charAt(i) == ',') {
                    String etiqueta = etiquetas.substring(startIndex, i).trim(); // Obtener la etiqueta y eliminar los espacios en blanco alrededor
                    Etiqueta newEtiqueta = new Etiqueta(i+1,etiqueta);
                    listaEtiquetas.add(newEtiqueta);
                    startIndex = i + 1; // Establecer el nuevo índice inicial para la próxima etiqueta
                }
            }

            // Agregar la última etiqueta después de la última coma o si no hay coma al final
            String ultimaEtiqueta = etiquetas.substring(startIndex).trim();
            Etiqueta ultEtiqueta = new Etiqueta(56,ultimaEtiqueta);
            if (!ultimaEtiqueta.isEmpty()) {
                listaEtiquetas.add(ultEtiqueta);
            }

            Articulo newArticulo = new Articulo(Articulo.setId(),titulo,cuerpo,autor,fecha,listaComentarios,listaEtiquetas);

            //Almacenando etiquetas.
            for(Etiqueta etique: listaEtiquetas){
                listaEtiquetas = Etiqueta.setEtiqueta(etique);
            }


            /*
            System.out.printf("El id del articulo es: %d, su titulo: %s\n" +
                    "su cuerpo: %s\nSu autor:%s\nFecha: \nEtiquetas:%s ", newArticulo.getId(), newArticulo.getTitulo(),newArticulo.getCuerpo()
            ,newArticulo.getAutor().getNombre(),newArticulo.getFecha(),newArticulo.getListaEtiquetas().getFirst());

            for (Etiqueta listaEtiqueta : listaEtiquetas) {
                System.out.println(listaEtiqueta.getEtiqueta());
            }
*/
            listaArticulos.add(newArticulo);
            Articulo.generarPaginaHTML(newArticulo);
            cxt.redirect("/blogUsuario");

        });



    }
}
