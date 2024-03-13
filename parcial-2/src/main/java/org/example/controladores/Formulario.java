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

        app.before("/formulario", context -> {
            Usuario currentUser = context.sessionAttribute("currentUser");
            if(currentUser != null){
                context.redirect("/html/formulario.html");
            }else {
                context.redirect("/login");
            }
        });
        app.get("/formulario",context -> {

            context.render("publico/html/formulario.html");
        });

        app.post("/captura", context ->{
            String nombre = context.formParam("nombre");
            String sector = context.formParam("sector");
            String nivelEscolar = context.formParam("nivelEscolar");
            double altitud = Double.parseDouble(context.formParam("altitud"));
            double longitud = Double.parseDouble(context.formParam("longitud"));

            System.out.println(nombre);
            System.out.println(sector);
            System.out.println(nivelEscolar);
            System.out.println(altitud);
            System.out.println(longitud);

        });
    }
}
