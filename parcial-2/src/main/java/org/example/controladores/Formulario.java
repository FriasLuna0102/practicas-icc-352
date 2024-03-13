package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.Usuario;
import org.example.util.ControladorClass;

public class Formulario extends ControladorClass {
    public Formulario(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarRutas() {

        app.get("/formulario",cxt -> {
            Usuario currentUser = cxt.sessionAttribute("currentUser");
            if(currentUser != null){
                cxt.redirect("/html/formulario.html");
            }else {
                cxt.redirect("/login");
            }
        });
    }
}
