package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.Usuario;
import org.example.util.ControladorClass;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Formulario extends ControladorClass {
    public Formulario(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarRutas() {

        app.routes(()->{

            path("/plantillaGeneral", () -> {

                before("/*", context -> {
                    Usuario currentUser = context.sessionAttribute("currentUser");
                    if(currentUser != null){
                        context.render("publico/html/formulario.html");
                    }else {
                        context.redirect("/login");
                    }
                });


                get("/formulario",context -> {

                    context.render("publico/html/formulario.html");
                });

                post("/captura", context ->{
                    String nombre = context.formParam("nombre");
                    String sector = context.formParam("sector");
                    String nivelEscolar = context.formParam("nivelEscolar");

                    // Implementar seguridad para que esto no sea null en caso de que el usuario bloquee la ubicacion
                    double altitud = Double.parseDouble(context.formParam("altitud"));
                    double longitud = Double.parseDouble(context.formParam("longitud"));

                    System.out.println(nombre);
                    System.out.println(sector);
                    System.out.println(nivelEscolar);
                    System.out.println(altitud);
                    System.out.println(longitud);

                    //context.redirect("/plantillaGeneral/formulario");
                });

            });

        });


    }
}
