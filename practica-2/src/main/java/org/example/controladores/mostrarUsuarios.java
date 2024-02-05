package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.Usuario;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class mostrarUsuarios extends ControladorClass{
    public mostrarUsuarios(Javalin app) {
        super(app);
    }

    List<Usuario> listUsuario = Usuario.getUsuarios();
    @Override
    public void aplicarRutas() {

        app.get("/mostrarUsuario", cxt ->{
            if(cxt.sessionAttribute("currentUser") == null){
                cxt.redirect("/login");
            }else{
                Map<String, Object> model = new HashMap<>();
                model.put("listUsuario",listUsuario);
                cxt.render("publico/html/listarUsuarios.html",model);
            }
        });
    }
}
