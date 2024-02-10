package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.Articulo;
import org.example.clases.Comentario;
import org.example.clases.Usuario;
import org.example.util.ControladorClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class agregarComentario extends ControladorClass {
    public agregarComentario(Javalin app) {
        super(app);
    }

    List<Comentario> listComentarios = Comentario.getComentarios();
    @Override
    public void aplicarRutas() {

        app.post("/agregarComentario", cxt ->{

            //Obtengo el id del formulario y busco el articul al cual se comentara por su id.
            String idArticulo = cxt.formParam("idArticulo");
            Articulo articulo = Articulo.obtenerArticuloPorId(idArticulo);

            //Lo mando al seccionComentario html, para comentar.
            Map<String, Object> model = new HashMap<>();
            model.put("articulo", articulo);

            cxt.render("publico/html/seccionComentario.html", model);

        });


        app.post("/comentar", cxt ->{

            List<Comentario> comenForArticulo = new ArrayList<>();
            String comentario = cxt.formParam("contenidoComentario");
            Usuario autor = cxt.sessionAttribute("currentUser");
            String idArticulo = cxt.formParam("idArticulo");
            Articulo articulo = Articulo.obtenerArticuloPorId(idArticulo);


            //Creo el comentario.
            Comentario newComent = new Comentario(comentario,autor,articulo);

            comenForArticulo.add(newComent);

            //Creo el nuevo articulo con los comentarios asignado.
            Articulo newArticulo = new Articulo(articulo.getTitulo(),articulo.getCuerpo(),articulo.getAutor()
                    , articulo.getFecha(), comenForArticulo,articulo.getListaEtiquetas());

            //Agrego los comentarios a la lista de comentario
            Comentario.setComentario(newComent);

            //Obtengo la lista de comentario para un articulo en comun.
            List<Comentario> listComentario = Comentario.buscarComentPorArticulo(articulo);


            Map<String, Object> model = new HashMap<>();
            model.put("articulo", newArticulo);
            model.put("listComentarios", listComentario); // Añadir la lista de comentarios al modelo

            cxt.render("publico/temp/articulo_plantila.html", model);

        });
    }
}
