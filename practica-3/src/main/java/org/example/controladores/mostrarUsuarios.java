package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.Blog;
import org.example.clases.Usuario;
import org.example.services.UsuarioServices;
import org.example.util.ControladorClass;

import javax.print.attribute.standard.JobOriginatingUserName;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class mostrarUsuarios extends ControladorClass {
    public mostrarUsuarios(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarRutas() {


        app.routes(()->{

            path("/blogUsuario", ()->{

                get("/mostrarUsuario", cxt ->{
                    if(cxt.sessionAttribute("currentUser") == null){
                        cxt.redirect("/login");
                    }else{
                        List<Usuario> listUsuario = UsuarioServices.getInstancia().obtenerTodosLosUsuarios();
                        Map<String, Object> model = new HashMap<>();
                        model.put("listUsuario",listUsuario);
                        cxt.render("publico/html/listarUsuarios.html",model);
                    }
                });

                get("/eliminarUsuario", context -> {
                    String username = context.queryParam("username");

                    context.redirect("/blogUsuario/mostrarUsuario");
                });

            });

        });

    }
}
