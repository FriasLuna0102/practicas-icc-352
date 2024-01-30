package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.Usuario;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlantillasControlador extends ControladorClass{
    public PlantillasControlador(Javalin app) {
        super(app);
    }

    List<Usuario> usuarios = Usuario.getUsuarios();

    @Override
    public void aplicarRutas() {

        // Configurar la ruta para renderizar una plantilla Thymeleaf.

        app.get("/crearArticulo", ctx -> {
            Map<String, Object> model = new HashMap<>();
            // Obtener la lista de usuarios

            System.out.println("Imprimiendo Usuarios:");
            //for(Usuario iterador: usuarios ){
            //    System.out.println(iterador.getNombre());
            // }
            model.put("usuarios", usuarios);
            ctx.render("publico/html/crearArticulo.html", model);
        });


        app.get("/blogUsuario", ctx -> {
            System.out.println("Hello");
            // Retrieve the currentUser session attribute
            Usuario currentUser = ctx.sessionAttribute("currentUser");
            if (currentUser != null) {
                // Set the currentUser attribute in the template context
                ctx.attribute("currentUser", currentUser);
                ctx.render("publico/html/blogUsuario.html");
            } else {
                // If the currentUser session attribute is not set, redirect to the login page
                ctx.redirect("/login");
            }
        });


    }
}
