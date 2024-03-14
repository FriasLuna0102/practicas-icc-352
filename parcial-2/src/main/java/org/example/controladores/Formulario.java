package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.Registro;
import org.example.clases.Usuario;
import org.example.services.RegistroServices;
import org.example.services.UsuarioServices;
import org.example.util.ControladorClass;

import java.util.HashMap;
import java.util.Map;

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
                        Map<String, Object> model = new HashMap<>();
                        model.put("usuario",currentUser);
                        context.render("publico/html/formulario.html",model);
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

                    String user = context.formParam("usuario");
                    assert user != null;
                    Usuario usuario = UsuarioServices.getInstancia().findById(Long.parseLong(user));

                    // Implementar seguridad para que esto no sea null en caso de que el usuario bloquee la ubicacion
                    double altitud = Double.parseDouble(context.formParam("altitud"));
                    double longitud = Double.parseDouble(context.formParam("longitud"));

                    System.out.println(usuario.getNombre());
                    System.out.println(sector);
                    System.out.println(nivelEscolar);
                    System.out.println(altitud);
                    System.out.println(longitud);

                    Registro nuevoRegistro = new Registro(nombre,sector,nivelEscolar,usuario,altitud,longitud);
                    RegistroServices.getInstancia().crear(nuevoRegistro);

                    context.redirect("/plantillaGeneral/administrarRegistros");
                });

            });

        });

    }
}
