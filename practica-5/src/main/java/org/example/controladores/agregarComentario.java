package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.Articulo;
import org.example.clases.Blog;
import org.example.clases.Comentario;
import org.example.clases.Usuario;
import org.example.services.ArticuloServices;
import org.example.services.ComentarioServices;
import org.example.services.UsuarioServices;
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
            long idArticulo = Long.parseLong(cxt.formParam("idArticulo"));
            Articulo articulo = ArticuloServices.getInstancia().obtenerArticuloConEtiquetasPorId(idArticulo);

            //Lo mando al seccionComentario html, para comentar.
            Map<String, Object> model = new HashMap<>();
            model.put("articulo", articulo);

            cxt.render("publico/html/seccionComentario.html", model);

        });


        app.post("/comentar", cxt ->{

            String comentario = cxt.formParam("contenidoComentario");
            Usuario autor = cxt.sessionAttribute("currentUser");
            long idArticulo = Long.parseLong(cxt.formParam("idArticulo"));
            Articulo articulo = ArticuloServices.getInstancia().obtenerArticuloConEtiquetasPorId(idArticulo);

            System.out.println(articulo.getTitulo());

            ComentarioServices.getInstancia().crear(new Comentario(comentario,autor,articulo));

            List<Comentario> listBD = ComentarioServices.getInstancia().obtenerTodosLosComentariosConArticulos();
            //Creo el comentario.


            List<Comentario> listComeEnParticular = new ArrayList<>();
            for(Comentario coment : listBD){
                if(coment.getArticulo().getId() == articulo.getId()){
                    listComeEnParticular.add(coment);
                }
            }

            //Creo el nuevo articulo con los comentarios asignado.
            Articulo newArticulo = new Articulo(articulo.getTitulo(),articulo.getCuerpo(),articulo.getAutor()
                    , articulo.getFecha(), listComeEnParticular,articulo.getListaEtiquetas());


            Map<String, Object> model = new HashMap<>();
            model.put("articulo", newArticulo);
            model.put("listComentarios", listComeEnParticular); // Añadir la lista de comentarios al modelo

            cxt.render("publico/temp/articulo_plantila.html", model);

        });
    }
}
