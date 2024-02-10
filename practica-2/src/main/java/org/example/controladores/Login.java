package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.Usuario;
import org.example.util.ControladorClass;

import java.util.List;

public class Login extends ControladorClass {


    public Login(Javalin app) {
        super(app);
    }

    List<Usuario> usuarios = Usuario.getUsuarios();


    @Override
    public void aplicarRutas() {

        app.get("/login", cxt ->{
            cxt.redirect("/login.html");
        });


        app.get("/logout", ctx -> {
            //invalidando la sesion.
            ctx.req().getSession().invalidate();
            ctx.redirect("/login");
        });

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
                    cxt.redirect("/blogUsuario");
                    return;
                }
            }
            // Si ninguna credencial coincide, redirigir de nuevo a la página de inicio de sesión
            cxt.redirect("/login");
        });

    }
}
