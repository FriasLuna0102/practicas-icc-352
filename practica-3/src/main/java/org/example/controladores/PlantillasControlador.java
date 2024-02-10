package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.Articulo;
import org.example.clases.Comentario;
import org.example.clases.Usuario;
import org.example.util.ControladorClass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class PlantillasControlador extends ControladorClass {
    public PlantillasControlador(Javalin app) {
        super(app);
    }

    List<Usuario> usuarios = Usuario.getUsuarios();
    List<Articulo> listArticulos = Articulo.getArticulos();

    @Override
    public void aplicarRutas() {

        app.routes(() ->{

            path("/blogUsuario", () ->{

                get(ctx -> {
                    // Retrieve the currentUser session attribute
                    Usuario currentUser = ctx.sessionAttribute("currentUser");
                    if (currentUser != null) {
                        // Set the currentUser attribute in the template context
                        ctx.attribute("currentUser", currentUser);


                        Map<String, Object> model = new HashMap<>();
                        model.put("listArticulos", listArticulos);

                        ctx.render("publico/html/blogUsuario.html", model);

                    } else {
                        // If the currentUser session attribute is not set, redirect to the login page
                        ctx.redirect("/login");
                    }
                });

                //Para evitar que despues de hacer logout no pueda acceder a crearUsuario.
                get("/plantillaUsuario",cxt ->{
                    if(cxt.sessionAttribute("currentUser") == null){
                        cxt.redirect("/login");
                    }else{
                        cxt.render("publico/html/crearUsuario.html");
                    }
                });

                get("/crearArticulo", ctx -> {
                    if (ctx.sessionAttribute("currentUser") == null) {
                        ctx.redirect("/login");
                    } else {

                        Map<String, Object> model = new HashMap<>();
                        model.put("usuarios", usuarios);
                        ctx.render("publico/html/crearArticulo.html", model);
                    }
                });

                get("/articulo/{id}", ctx -> {
                    String id = ctx.pathParam("id");
                    // Busca el artículo por ID
                    Articulo articulo = Articulo.obtenerArticuloPorId(id);
                    List<Comentario> listDeComentario = Comentario.buscarComentPorArticulo(articulo);

                    Map<String, Object> model = new HashMap<>();
                    model.put("articulo", articulo);
                    model.put("listComentarios",listDeComentario);

                    ctx.render("publico/temp/articulo_plantila.html", model);
                });



            });



        });

    }
}
