package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.Usuario;
import org.example.jdbc.CockroachDB;
import org.example.services.UsuarioServices;
import org.example.util.ControladorClass;
import org.jasypt.util.text.BasicTextEncryptor;

public class Login extends ControladorClass {

    public Login(Javalin app) {
        super(app);
    }

    private static BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
    private Usuario usuario;

    @Override
    public void aplicarRutas() {

        textEncryptor.setPassword("encriptacion");
        app.get("/login", cxt ->{
            if (cxt.cookie("username") != null){
                String username = textEncryptor.decrypt(cxt.cookie("username"));
                usuario = UsuarioServices.getInstancia().findByUsername(username);
                CockroachDB.insertarDataLogueo(usuario.getUsername());
                cxt.sessionAttribute("currentUser", usuario);
                cxt.redirect("/blogUsuario");
                return;
            }

            cxt.render("/publico/login.html");
        });

        app.before("/logout", ctx -> {
            ctx.cookie("username", "invalido", 0);
            //invalidando la sesion.
            ctx.req().getSession().invalidate();
            ctx.redirect("/login");
        });

        app.post("/login", cxt -> {
            String usuarioLogin = cxt.formParam("username");
            String passwordLogin = cxt.formParam("password");
            boolean remember = cxt.formParam("remember") != null;

            if ((usuario = UsuarioServices.getInstancia().findByUsername(usuarioLogin)) != null) {

                if (usuario.getPassword().equals(passwordLogin)) {
                    if (remember){
                        cxt.cookie("username", textEncryptor.encrypt(usuario.getUsername()), 604800);
                    }
                    CockroachDB.insertarDataLogueo(usuario.getUsername());
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
