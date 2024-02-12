package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.Blog;
import org.example.clases.Usuario;
import org.example.services.UsuarioServices;
import org.example.util.ControladorClass;

import java.util.List;

public class Login extends ControladorClass {


    public Login(Javalin app) {
        super(app);
    }

    List<Usuario> usuarios = Blog.getInstance().getUsuarioList();

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
            String usuarioLogin = cxt.formParam("username");
            String passwordLogin = cxt.formParam("password");

            // Buscar el usuario en la base de datos utilizando Hibernate
            List<Usuario> usuarios = UsuarioServices.getInstancia().findAllByNombre(usuarioLogin);


            if (!usuarios.isEmpty()) {
                Usuario usuario = usuarios.get(0); // Suponiendo que hay solo un usuario con el mismo nombre

                if (usuario.getPassword().equals(passwordLogin)) {
                    // Si las credenciales coinciden, establecer el usuario en la sesión y redirigir
                    cxt.sessionAttribute("currentUser", usuario);
                    cxt.redirect("/blogUsuario");
                    return;
                }
            }

            // Si ninguna credencial coincide o no se encuentra el usuario, redirigir de nuevo a la página de inicio de sesión
            cxt.redirect("/login");
        });

    }
}
