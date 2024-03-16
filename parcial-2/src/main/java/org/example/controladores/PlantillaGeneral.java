package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.Usuario;
import org.example.util.ControladorClass;

import java.util.HashMap;
import java.util.Map;

public class PlantillaGeneral extends ControladorClass {
    public PlantillaGeneral(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarRutas() {

        app.get("/plantillaGeneral", cxt ->{
            Usuario currentUser = cxt.sessionAttribute("currentUser");
            if(currentUser != null){
                Map<String, Object> model = new HashMap<>();
                model.put("usuario", currentUser);
                cxt.render("publico/html/plantillaGeneral.html",model);
            }else {
                cxt.redirect("/login");
            }
        });

        app.head("/autenticar", context -> {
            context.status(200);
        });

    }
}
