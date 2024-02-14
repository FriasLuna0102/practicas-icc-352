package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.*;
import org.example.services.ArticuloServices;
import org.example.services.ComentarioServices;
import org.example.services.EtiquetaServices;
import org.example.util.ControladorClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class PlantillasControlador extends ControladorClass {
    public PlantillasControlador(Javalin app) {
        super(app);
    }

    List<Usuario> usuarios = Blog.getInstance().getUsuarioList();
    List<Articulo> listArticulos = Blog.getInstance().getArticuloList();

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

                        List<Etiqueta> listEtiqueta = EtiquetaServices.getInstancia().obtenerTodasLasEtiquetas();

                        Map<String, Object> model = new HashMap<>();
                        model.put("listArticulos", listArticulos);

                        for (Articulo articulo : listArticulos) {
                            List<Etiqueta> nuevasEtiquetas = articulo.getListaEtiquetas().stream()
                                    .filter(etiqueta -> !listEtiqueta.contains(etiqueta))
                                    .collect(Collectors.toList());
                            articulo.setListaEtiquetas(nuevasEtiquetas);
                        }

                        model.put("listEtiquetas", listEtiqueta);
                        model.put("foto", currentUser.getFoto());


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
                    long id = Long.parseLong(ctx.pathParam("id"));
                    // Busca el artículo por ID
                    Articulo articulo = Blog.getInstance().obtenerArticuloPorId(id);

                    List<Comentario> listBD = ComentarioServices.getInstancia().obtenerTodosLosComentarios();

                    List<Comentario> listComeEnParticular = new ArrayList<>();
                    for(Comentario coment : listBD){
                        if(coment.getArticulo().getId() == articulo.getId()){
                            listComeEnParticular.add(coment);
                        }
                    }

                    Map<String, Object> model = new HashMap<>();
                    model.put("articulo", articulo);
                    model.put("listComentarios",listComeEnParticular);

                    ctx.render("publico/temp/articulo_plantila.html", model);
                });



                get("/buscar", ctx -> {

                    if(ctx.sessionAttribute("currentUser") == null){

                        ctx.redirect("/login");

                    }else{

                        Map<String, Object> model = new HashMap<>();

                        String etiqueta = ctx.queryParam("etiqueta");
                        Etiqueta etique = Etiqueta.buscarEtiquet(etiqueta);

                        List<Articulo> listArticuloos = etique.getListArticulos();

                        model.put("listArticulos", listArticuloos);

                        ctx.render("publico/html/blogPorEtiqueta.html", model);
                    }

                });


            });



        });

    }
}
