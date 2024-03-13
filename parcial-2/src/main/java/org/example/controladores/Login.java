package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.Usuario;
import org.example.services.UsuarioServices;
import org.example.util.ControladorClass;

import java.util.List;

public class Login extends ControladorClass {

    private Usuario usuario;

    public Login(Javalin app) {
        super(app);
    }

    //List<Usuario> listUsuarios =

    @Override
    public void aplicarRutas() {

        app.get("/login",cxt->{
            System.out.println("Entro");
            cxt.render("publico/html/login.html");
        });

        app.post("/login", cxt->{
            String usuarioLogin = cxt.formParam("username");
            String passwordLogin = cxt.formParam("password");

            if ((usuario = UsuarioServices.getInstancia().findByUsername(usuarioLogin)) != null) {

                if (usuario.getPassword().equals(passwordLogin)) {

                    // Si las credenciales coinciden, establecer el usuario en la sesión y redirigir
                    //Blog.getInstance().setUsuario(usuario);
                    cxt.sessionAttribute("currentUser", usuario);
                    cxt.redirect("/formulario");
                    return;
                }
            }

            // Si ninguna credencial coincide o no se encuentra el usuario, redirigir de nuevo a la página de inicio de sesión
            cxt.redirect("/login");
        });
    }
}
