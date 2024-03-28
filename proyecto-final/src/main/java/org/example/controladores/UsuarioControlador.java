package org.example.controladores;

import io.javalin.Javalin;
import org.example.encapsulaciones.Usuario;
import org.example.servicios.UsuarioServices;
import org.example.utils.ControladorClass;

public class UsuarioControlador extends ControladorClass {
    public UsuarioControlador(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarRutas() {

        app.get("/registrarUsuarios", cxt ->{
            cxt.render("publico/html/registrarUsuarios.html");
        });

        app.post("/registrarUsuarios", cxt ->{

            String username = cxt.formParam("username");
            String nombre = cxt.formParam("nombre");
            String password = cxt.formParam("password");
            boolean user = Boolean.parseBoolean(cxt.formParam("isUser"));

            Usuario tpm = new Usuario(username,nombre,password,user);

            UsuarioServices.getInstancia().crearEstudiante(tpm);

        });

    }
}
