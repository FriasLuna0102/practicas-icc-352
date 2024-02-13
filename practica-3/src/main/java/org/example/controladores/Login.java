package org.example.controladores;

import io.javalin.Javalin;
import io.javalin.validation.Validator;
import org.eclipse.jetty.util.IO;
import org.example.clases.Blog;
import org.example.clases.Usuario;
import org.example.services.UsuarioServices;
import org.example.util.ControladorClass;
import org.jasypt.util.text.BasicTextEncryptor;
import org.jasypt.util.text.TextEncryptor;

import javax.print.attribute.standard.JobOriginatingUserName;
import java.util.List;

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
                usuario = UsuarioServices.getInstancia().findByNombre(username);
                System.out.println(usuario.getPassword() + 1);
                cxt.sessionAttribute("currentUser", usuario);
                cxt.redirect("/blogUsuario");
                return;
            }
            cxt.render("/publico/login.html");
        });

        app.before("/logout", ctx -> {
            System.out.println("Invalidando");
            ctx.cookie("username", "invalido", 0);
            //invalidando la sesion.
            ctx.req().getSession().invalidate();
            ctx.redirect("/login");
        });

        app.post("/login", cxt -> {
            String usuarioLogin = cxt.formParam("username");
            String passwordLogin = cxt.formParam("password");
            boolean remember = cxt.formParam("remember") != null;

            // Buscar el usuario en la base de datos utilizando Hibernate
            // Que sea una lista esta de mas
            List<Usuario> usuarios = UsuarioServices.getInstancia().findAllByNombre(usuarioLogin);

            if (!usuarios.isEmpty()) {
                usuario = usuarios.get(0); // Suponiendo que hay solo un usuario con el mismo nombre

                if (usuario.getPassword().equals(passwordLogin)) {

                    if (remember){
                        cxt.cookie("username", textEncryptor.encrypt(usuario.getUsername()), 604800);
                    }
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
