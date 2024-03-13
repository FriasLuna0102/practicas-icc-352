package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.Contraladora;
import org.example.clases.Usuario;
import org.example.services.UsuarioServices;
import org.example.util.ControladorClass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class AdministrarUsuario extends ControladorClass {
    public AdministrarUsuario(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarRutas() {

        app.routes(() ->{

            path("/plantillaGeneral", ()->{

                get("/administrarUsuarios", cxt ->{
                    Usuario currentUser = cxt.sessionAttribute("currentUser");
                    if(currentUser != null){
                        List<Usuario> listUsuario = UsuarioServices.getInstancia().obtenerTodosLosUsuarios();
                        Map<String, Object> model = new HashMap<>();
                        model.put("listUsuario", listUsuario);
                        model.put("usuario", Contraladora.getInstance().getUsuario());

                        cxt.render("publico/html/administrarUsuarios.html",model);
                    }else {
                        cxt.redirect("/login");
                    }
                });

                get("/plantillaUsuarioModificar", context -> {
                    String username = context.queryParam("username");
                    Usuario usuario = UsuarioServices.getInstancia().findByUsername(username);

                    Map<String, Object> model = new HashMap<>();

                    model.put("usuario", usuario);

                    context.render("publico/temp/editarUsuario.html",model);
                });

            });

        });


    }
}
