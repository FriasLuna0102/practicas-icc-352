package org.example.controladores;

import io.javalin.Javalin;
import org.example.servicios.REST.ServicesRest;
import org.example.utils.ControladorClass;

import static io.javalin.apibuilder.ApiBuilder.after;
import static io.javalin.apibuilder.ApiBuilder.*;

public class ApiControlador extends ControladorClass {
    public ApiControlador(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarRutas() {


          app.after("/list",ctx -> {
              ctx.header("Content-Type", "application/json");
          });


          app.get("/list/{nombre}",context -> {
              System.out.println("Entroo");
              context.json(ServicesRest.getInstacia().listUrlConEstadisticaParaUnUsuario(context.pathParamAsClass("nombre",String.class).get()));
          });


    }
}
