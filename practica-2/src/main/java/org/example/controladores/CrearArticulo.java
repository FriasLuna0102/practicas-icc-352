package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.Articulo;
import org.example.clases.Comentario;
import org.example.clases.Etiqueta;
import org.example.clases.Usuario;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CrearArticulo extends ControladorClass{
    public CrearArticulo(Javalin app) {
        super(app);
    }

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


            // Obtenemos la fecha del sistema:
            Date fechaActual = Calendar.getInstance().getTime();

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

            Articulo newArticulo = new Articulo(titulo,cuerpo,autor,fechaActual,listaComentarios,listaEtiquetas);

            //Almacenando etiquetas.
            for(Etiqueta etique: listaEtiquetas){
                listaEtiquetas = Etiqueta.setEtiqueta(etique);
            }

            Articulo.setArticulos(newArticulo);
            cxt.redirect("/blogUsuario");

        });



    }
}
