package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.Blog;
import org.example.clases.Usuario;
import org.example.services.ArticuloServices;
import org.example.services.UsuarioServices;
import org.example.util.ControladorClass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;

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
                        model.put("usuario", Blog.getInstance().getUsuario());
                        cxt.render("publico/html/listarUsuarios.html",model);
                    }
                });

                get("/eliminarUsuario", context -> {
                    String username = context.queryParam("username");
                    Usuario usuario = UsuarioServices.getInstancia().findByUsername(username);

                    ArticuloServices.getInstancia().eliminarArticuloByAutor(username);
                    UsuarioServices.getInstancia().eliminar(usuario.getId());
                    context.redirect("/blogUsuario/mostrarUsuario");
                });

            });

        });

    }
}
