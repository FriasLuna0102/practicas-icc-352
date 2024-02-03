package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.Articulo;
import org.example.clases.Comentario;
import org.example.clases.Usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

public class agregarComentario extends ControladorClass{
    public agregarComentario(Javalin app) {
        super(app);
    }

    List<Comentario> listComentarios = Comentario.getComentarios();
    @Override
    public void aplicarRutas() {

        app.post("/agregarComentario", cxt ->{

            //String comentario = cxt.formParam("contenidoComentario");
            //Usuario autor = cxt.sessionAttribute("currentUser");
            String idArticulo = cxt.formParam("idArticulo");
            Articulo articulo = Articulo.obtenerArticuloPorId(idArticulo);

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
            System.out.println(comentario);
            System.out.println(autor.getNombre());



            Comentario newComent = new Comentario(comentario,autor,articulo);

            comenForArticulo.add(newComent);

            Articulo newArticulo = new Articulo(articulo.getTitulo(),articulo.getCuerpo(),articulo.getAutor()
                    , articulo.getFecha(), comenForArticulo,articulo.getListaEtiquetas());

            listComentarios.add(newComent);



            Map<String, Object> model = new HashMap<>();
            model.put("articulo", newArticulo);
            model.put("comentarios", listComentarios); // Añadir la lista de comentarios al modelo

            cxt.render("publico/temp/articulo_plantila.html", model);

        });
    }
}
