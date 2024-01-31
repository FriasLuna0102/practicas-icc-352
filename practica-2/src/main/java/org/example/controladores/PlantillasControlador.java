package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.Articulo;
import org.example.clases.Usuario;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlantillasControlador extends ControladorClass{
    public PlantillasControlador(Javalin app) {
        super(app);
    }

    List<Usuario> usuarios = Usuario.getUsuarios();
    List<Articulo> listArticulos = Articulo.getArticulos();

    @Override
    public void aplicarRutas() {

        // Configurar la ruta para renderizar una plantilla Thymeleaf.

        app.get("/crearArticulo", ctx -> {
            if(ctx.sessionAttribute("currentUser") == null){
                ctx.redirect("/login");
            }else {

                Map<String, Object> model = new HashMap<>();
                model.put("usuarios", usuarios);
                ctx.render("publico/html/crearArticulo.html", model);
            }
        });


        app.get("/blogUsuario", ctx -> {
            // Retrieve the currentUser session attribute
            Usuario currentUser = ctx.sessionAttribute("currentUser");
            if (currentUser != null) {
                // Set the currentUser attribute in the template context
                ctx.attribute("currentUser", currentUser);

                if(listArticulos.isEmpty()){
                    ctx.render("publico/html/blogUsuario.html");
                }else{
                    System.out.println("Entro");
                    Map<String, Object> model = new HashMap<>();
                    model.put("listArticulos", listArticulos);
                    ctx.render("publico/html/blogUsuario.html", model);
                }
            } else {
                // If the currentUser session attribute is not set, redirect to the login page
                ctx.redirect("/login");
            }
        });


    }
}
