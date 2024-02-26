package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.*;
import org.example.services.ArticuloServices;
import org.example.services.ComentarioServices;
import org.example.services.EtiquetaServices;
import org.example.services.UsuarioServices;
import org.example.util.ControladorClass;

import java.util.*;
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

                        // Get the page number from the query parameters, or default to 1 if it's not present
                        int numeroPagina = Optional.ofNullable(ctx.queryParam("pagina")).map(Integer::parseInt).orElse(1);

                        // Fetch the articles for the current page
                        List<Articulo> listArticulos = ArticuloServices.getInstancia().obtenerArticulosConEtiquetasPorPagina(numeroPagina, 5);

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

                        // Add the current page number and total pages to the model
                        model.put("paginaActual", numeroPagina);
                        model.put("totalPaginas", (int) Math.ceil((double) ArticuloServices.getInstancia().contarArticulos() / 5));

                        ctx.render("publico/html/blogUsuario.html", model);

                    } else {
                        // If the currentUser session attribute is not set, redirect to the login page
                        ctx.redirect("/login");
                    }
                });

                get("/articuloAjax", cxt ->{

                    //List<Articulo> lisAr = ArticuloServices.getInstancia().obtenerArticulosConEtiquetasPorPagina(1,5);
                    List<Comentario> lisU = UsuarioServices.getInstancia().obtenerUsuariosConComentarios();
                    cxt.json(lisU);
                    System.out.println("hey");
                });

                // Controlador para manejar solicitudes AJAX de paginación
                get("/blogUsuarioAjax", ctx -> {
                    int numeroPagina = Optional.ofNullable(ctx.queryParam("pagina")).map(Integer::parseInt).orElse(1);

                    //int numeroPagina = Integer.parseInt(ctx.queryParam("pagina"));
                    List<Articulo> listArticulos = ArticuloServices.getInstancia().obtenerArticulosConEtiquetasPorPagina(numeroPagina, 5);
                    List<ArticuloDTO> articulosDTO = listArticulos.stream().map(articulo -> {
                        ArticuloDTO dto = new ArticuloDTO();
                        dto.setId(articulo.getId());
                        dto.setTitulo(articulo.getTitulo());
                        dto.setCuerpo(articulo.getCuerpo());
                        dto.setNombreAutor(articulo.getAutor().getNombre()); // Aquí asumo que hay un método getNombre() en la clase Usuario
                        dto.setFecha(articulo.getFecha());
                        //dto.setListaComentarios(articulo.getListaComentarios().stream().map(Comentario::getComentario).collect(Collectors.toList()));
                        //dto.setListaEtiquetas(articulo.getListaEtiquetas().stream().map(Etiqueta::getEtiqueta).collect(Collectors.toList()));
                        return dto;
                    }).collect(Collectors.toList());

                    System.out.println(articulosDTO.size());
                    for(ArticuloDTO ar : articulosDTO){
                        System.out.println(ar.getTitulo());
                    }
                    Map<String, Object> model = new HashMap<>();
                    model.put("paginaActual", numeroPagina);
                    model.put("totalPaginas", (int) Math.ceil((double) ArticuloServices.getInstancia().contarArticulos() / 5));
                    ctx.render("publico/html/blogUsuario.html", model);
                    ctx.json(articulosDTO); // Serializar los datos como JSON y enviar como respuesta
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
                    Articulo articulo = ArticuloServices.getInstancia().obtenerArticuloConEtiquetasPorId(id);

                    List<Comentario> listBD = ComentarioServices.getInstancia().obtenerTodosLosComentariosConArticulos();

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
