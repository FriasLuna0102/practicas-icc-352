package org.example.controladores;

import io.javalin.Javalin;
import org.example.encapsulaciones.EstadisticaURL;
import org.example.encapsulaciones.ShortURL;
import org.example.encapsulaciones.Usuario;
import org.example.servicios.UsuarioServices;
import org.example.servicios.mongo.EstadisticaODM;
import org.example.servicios.mongo.URLODM;
import org.example.servicios.mongo.UsuarioODM;
import org.example.servicios.mongo.VisitanteODM;
import org.example.utils.ControladorClass;

import java.util.*;

import static io.javalin.apibuilder.ApiBuilder.*;

public class UrlControlador extends ControladorClass {

    Usuario usuarioLogueado;
    List<ShortURL> listUrlsBase = URLODM.getInstance().obtenerTodasLasUrl();

    public UrlControlador(Javalin app) {
        super(app);
    }


    @Override
    public void aplicarRutas() {



//        EstadisticaURL esta = new EstadisticaURL();
//        Date date = new Date();
//        URLServices.getInstancia().crearUrl(new ShortURL("ddd","www.com","com",date,esta,"foto"));

        app.routes(() -> {
           path("/url", () -> {


               post("generar", context -> {
                    String url = context.formParam("urlBase");
                    ShortURL shortURL = URLODM.getInstance().buscarUrlByUrlLarga(url);

                    if (shortURL == null){
                        shortURL = new ShortURL(url,null);
                        URLODM.getInstance().guardarURL(shortURL);
                        usuarioLogueado = UsuarioServices.getInstancia().getUsuarioLogueado();
                        if (usuarioLogueado != null){
                            usuarioLogueado.getUrlList().add(shortURL);
                            UsuarioODM.getInstance().guardarUsuario(usuarioLogueado);
                        }else {
                            UsuarioServices.getInstancia().getVisitanteActual().getUrlList().add(shortURL);
                            VisitanteODM.getInstance().guardarVisitante(UsuarioServices.getInstancia().getVisitanteActual());
                        }
                        EstadisticaURL estadisticaURL = new EstadisticaURL(shortURL);
                        EstadisticaODM.getInstance().guardarEstadistica(estadisticaURL);
                    }

                    context.result(shortURL.getUrlCorta());
               });

               get("misUrl", cxt ->{
                   Map<String, Object> model = new HashMap<>();
                   usuarioLogueado = UsuarioServices.getInstancia().getUsuarioLogueado();
                   if (usuarioLogueado != null){
                       if (usuarioLogueado.isUser()){
                           listUrlsBase = usuarioLogueado.getUrlList();
                       }else {
                           listUrlsBase = URLODM.getInstance().obtenerTodasLasUrl();
                       }
                   }else {
                        listUrlsBase = UsuarioServices.getInstancia().getVisitanteActual().getUrlList();
                   }
                   model.put("listUrl", listUrlsBase);
                   Usuario user = UsuarioServices.getInstancia().getUsuarioLogueado();
                   model.put("usuario",user);

                   cxt.render("publico/html/misUrl.html",model);
               });

               get("eliminarURL", cxt ->{
                   String codigoUrl = cxt.queryParam("codigoUrl");
                   ShortURL url = URLODM.getInstance().buscarUrlByCodig(codigoUrl);
                   URLODM.getInstance().eliminarUrl(url);
                   cxt.redirect("/");
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

            try {
                EstadisticaURL estadisticaURL = EstadisticaODM.getInstance().buscarEstadisticaByCodigoOfUrl(codigo);
                estadisticaURL.aumentarCantAcceso();
                EstadisticaODM.getInstance().guardarEstadistica(estadisticaURL);
            }catch (Exception ignored){}
            System.out.println(context.ip());
            System.out.println(context.userAgent());

	        assert url != null;
	        context.redirect(url);
        });
    }
}
