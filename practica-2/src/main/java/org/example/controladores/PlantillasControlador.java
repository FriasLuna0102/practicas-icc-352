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
    }
}
