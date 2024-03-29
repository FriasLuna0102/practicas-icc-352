package org.example.controladores;

import io.javalin.Javalin;
import org.example.encapsulaciones.EstadisticaURL;
import org.example.encapsulaciones.ShortURL;
import org.example.servicios.URLServices;
import org.example.servicios.mongo.URLODM;
import org.example.utils.ControladorClass;

import java.util.*;

import static io.javalin.apibuilder.ApiBuilder.*;

public class UrlControlador extends ControladorClass {
    public UrlControlador(Javalin app) {
        super(app);
    }


    @Override
    public void aplicarRutas() {

        List<ShortURL> listUrlsBase = new ArrayList<>();

        EstadisticaURL esta = new EstadisticaURL();
        Date date = new Date();
        URLServices.getInstancia().crearUrl(new ShortURL("ddd","www.com","com",date,esta,"foto"));

        app.routes(() -> {
           path("/url", () -> {

               post("generar", context -> {
                    String url = context.formParam("urlBase");
                    ShortURL shortURL = URLODM.getInstance().buscarUrlByUrlLarga(url);
                   listUrlsBase.add(shortURL);
                    if (shortURL == null){
                        shortURL = new ShortURL(url,null);
                        listUrlsBase.add(shortURL);
                        URLODM.getInstance().guardarURL(shortURL);
                    }

                    context.result(shortURL.getUrlCorta());
               });

               get("misUrl", cxt ->{
                   Map<String, Object> model = new HashMap<>();
                   model.put("listUrl", listUrlsBase);

                   cxt.render("publico/html/misUrl.html",model);
               });

           });


        });

        app.get("/{codigo}", context -> {
            String codigo = context.pathParam("codigo");
            String url = "";
            try {
                //url tira null pero no le hagan caso
                url = URLODM.getInstance().buscarUrlByCodigo(codigo);
            }catch (Exception ignored){
            }

            if (url == null){
                context.result("Pagina no encontrada").status(404);
            }

	        assert url != null;
	        context.redirect(url);
        });
    }
}
