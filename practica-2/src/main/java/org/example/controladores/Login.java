package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.Usuario;

import java.util.List;

public class Login extends ControladorClass{


    public Login(Javalin app) {
        super(app);
    }

    List<Usuario> usuarios = Usuario.getUsuarios();


    @Override
    public void aplicarRutas() {
        app.post("/login", cxt -> {
            if (usuarios.isEmpty()) {
                cxt.redirect("login.html");
                return;
            }

            String usuarioLogin = cxt.formParam("username");
            String passwordLogin = cxt.formParam("password");

            for (Usuario usuario : usuarios) {
                if (usuario.getUsername().equals(usuarioLogin) && usuario.getPassword().equals(passwordLogin)) {
                    cxt.sessionAttribute("currentUser", usuario);
                    System.out.println("Nombre de usuario establecido en la sesión: " + usuario.getNombre());
                    cxt.redirect("/html/blogUsuario.html");
                    return;
                }
            }

            // Si ninguna credencial coincide, redirigir de nuevo a la página de inicio de sesión
            cxt.redirect("/login");
        });
    }
}
