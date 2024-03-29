package org.example.controladores;

import io.javalin.Javalin;
import org.example.encapsulaciones.EstadisticaURL;
import org.example.encapsulaciones.ShortURL;
import org.example.servicios.URLServices;
import org.example.servicios.mongo.URLODM;
import org.example.utils.ControladorClass;

import java.util.Date;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class UrlControlador extends ControladorClass {
    public UrlControlador(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarRutas() {
        EstadisticaURL esta = new EstadisticaURL();
        Date date = new Date();
        URLServices.getInstancia().crearUrl(new ShortURL("ddd","www.com","com",date,esta,"foto"));

        app.routes(() -> {
           path("/url", () -> {

               post("generar", context -> {
                    String url = context.formParam("urlBase");
                    ShortURL shortURL = URLODM.getInstance().buscarUrlByUrlLarga(url);

                    if (shortURL == null){
                        shortURL = new ShortURL(url,null);
                        URLODM.getInstance().guardarURL(shortURL);
                    }

                    context.result(shortURL.getUrlCorta());
               });
           });
        });

        app.get("/{codigo}", context -> {

        });
    }
}
