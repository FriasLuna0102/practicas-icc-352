package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.*;
import org.example.services.ArticuloServices;
import org.example.util.ControladorClass;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CrearArticulo extends ControladorClass {
    public CrearArticulo(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarRutas() {

        app.post("/crearArticulo", cxt ->{

            //Lista de comentarios:
            List<Comentario> listaComentarios = Comentario.getComentarios();

            String titulo = cxt.formParam("titulo");
            String cuerpo = cxt.formParam("cuerpo");
            Usuario autor = cxt.sessionAttribute("currentUser");

            // Obtenemos la fecha del sistema:
            Date fechaActual = Calendar.getInstance().getTime();

            String etiquetas = cxt.formParam("etiquetas");

            List<Etiqueta> listaEtiquetas = Blog.getInstance().stringToEtiqueta(etiquetas);

            Articulo newArticulo = new Articulo(titulo,cuerpo,autor,fechaActual,listaComentarios,listaEtiquetas);

            // Guarda el nuevo Articulo en la base de datos
            ArticuloServices.getInstancia().crear(newArticulo);

            Blog.getInstance().getArticuloList().addFirst(newArticulo);
            cxt.redirect("/blogUsuario");

        });

    }
}
