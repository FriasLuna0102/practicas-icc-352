package org.example.controladores;

import io.javalin.Javalin;
import org.example.util.ControladorClass;

public class CookiesSeccionesControlador extends ControladorClass {
    public CookiesSeccionesControlador(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarRutas() {

        app.post("/login-cookies", ctxContext -> {
            //Recibiendo información del formulario.
            String usuario = ctxContext.formParam("username");
            String contrasena = ctxContext.formParam("password");
            if(usuario==null || contrasena == null){
                //Error para procesar la información.
                ctxContext.redirect("/login");
                return;
            }
            //Estamos haciendo fake de un servicio de autenticacion, busque en un servicio.
            ctxContext.cookie("usuario", usuario, 120);
            ctxContext.cookie("password", contrasena, 120);
            //enviando a la vista.
            System.out.println("Funciono");
        });

    }
}
