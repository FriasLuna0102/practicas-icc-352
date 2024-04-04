package org.example.controladores;

import io.javalin.Javalin;
import org.example.encapsulaciones.EstadisticaURL;
import org.example.encapsulaciones.ShortURL;
import org.example.encapsulaciones.Usuario;
import org.example.encapsulaciones.Visitante;
import org.example.servicios.URLServices;
import org.example.servicios.UsuarioServices;
import org.example.servicios.mongo.EstadisticaODM;
import org.example.servicios.mongo.URLODM;
import org.example.servicios.mongo.UsuarioODM;
import org.example.servicios.mongo.VisitanteODM;
import org.example.utils.ControladorClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static io.javalin.apibuilder.ApiBuilder.*;

public class UrlControlador extends ControladorClass {

    Usuario usuarioLogueado;
    List<ShortURL> listUrlsBase = URLODM.getInstance().obtenerTodasLasUrl();

    List<Visitante> listVisitante = new ArrayList<>();

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

//                   List<ShortURL> lis = UsuarioServices.getInstancia().getVisitanteActual().getUrlList();
//                   String idVisitante = UsuarioServices.getInstancia().getVisitanteActual().getId();
//                   String idUrl = "660ec94b3ac092111c9fbf61";
//                   System.out.println("Visitante id: "+idVisitante);
//                   VisitanteODM.getInstance().eliminarUrlDeVisitante(idVisitante,idUrl);

                   cxt.render("publico/html/misUrl.html",model);
               });


               get("eliminarURL", cxt ->{
                   String codigoUrl = cxt.queryParam("codigoUrl");
                   ShortURL url = URLODM.getInstance().buscarUrlByCodig(codigoUrl);

                   List<ShortURL> lis = UsuarioServices.getInstancia().getVisitanteActual().getUrlList();
                   String idVisitante = UsuarioServices.getInstancia().getVisitanteActual().getId();
                   String idURL = url.getId();

                   for (ShortURL ur: lis){

                       if (ur.getCodigo().equals(url.getCodigo())){
                           VisitanteODM.getInstance().eliminarUrlDeVisitante(idVisitante,idURL);
                           URLODM.getInstance().eliminarUrl(url);
                           System.out.println("Son iguales");
                       }else {
                           URLODM.getInstance().eliminarUrl(url);
                       }

                   }
                   cxt.redirect("/");
               });

           });


        });




        app.get("/{codigo}", context -> {
            String codigo = context.pathParam("codigo");
            String url = "";
            System.out.println("Entrooo");
            try {
                //url tira null pero no le hagan caso
                url = URLODM.getInstance().buscarUrlByCodigo(codigo);
            }catch (Exception ignored){
            }

            if (url == null){
                context.result("Pagina no encontrada").status(404);
            }

            // Obtener la fecha y hora actual
            LocalDateTime now = LocalDateTime.now();

            // Formatear la fecha y hora según el formato deseado
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String dateTime = now.format(formatter);

            // Obtener el identificador único de la URL
            String id = context.pathParam("codigo");

            ShortURL shorurl = URLODM.getInstance().buscarUrlByCodig(id);

            System.out.println(id);
            // Incrementar el contador de accesos

            // Registrar información sobre la solicitud
            String userAgent = context.userAgent();
            String ipAddress = context.ip();
            String clientDomain = context.host();
            String operatingSystem = parseOperatingSystem(userAgent);
            String navegador = parseBrowser(userAgent);

            EstadisticaURL estadisticaURL = new EstadisticaURL(shorurl);
            estadisticaURL.setCantidadAccesos(1);


            Map<String, Integer> direccionesIP = estadisticaURL.getDireccionesIP();
            Map<String, Integer> dominioCliente = estadisticaURL.getDominiosClientes();
            Map<String, Integer> sistemaOperativo = estadisticaURL.getPlataformasSO();
            Map<String, Integer> navegadores = estadisticaURL.getNavegadores();

            // Verificar y aumentar el valor de la clave si ya está presente
            if (direccionesIP.containsKey(ipAddress)) {
                direccionesIP.put(ipAddress, direccionesIP.get(ipAddress) + 1);
                estadisticaURL.setDireccionesIP(direccionesIP);
            } else {
                direccionesIP.put(ipAddress, 1);
                estadisticaURL.setDireccionesIP(direccionesIP);
            }

            if (dominioCliente.containsKey(clientDomain)) {
                dominioCliente.put(clientDomain, dominioCliente.get(clientDomain) + 1);
                estadisticaURL.setDominiosClientes(dominioCliente);
            } else {
                dominioCliente.put(clientDomain, 1);
                estadisticaURL.setDominiosClientes(dominioCliente);

            }

            if (sistemaOperativo.containsKey(operatingSystem)) {
                sistemaOperativo.put(operatingSystem, sistemaOperativo.get(operatingSystem) + 1);
                estadisticaURL.setPlataformasSO(sistemaOperativo);

            } else {
                sistemaOperativo.put(operatingSystem, 1);
                estadisticaURL.setPlataformasSO(sistemaOperativo);

            }

            if (navegadores.containsKey(navegador)) {
                navegadores.put(navegador, navegadores.get(navegador) + 1);
                estadisticaURL.setNavegadores(navegadores);

            } else {
                navegadores.put(navegador, 1);
                estadisticaURL.setNavegadores(navegadores);

            }


            EstadisticaODM.getInstance().guardarEstadistica(estadisticaURL);
            // Imprimir la información en la consola
            System.out.println("Fecha y hora actual: " + dateTime);
            System.out.println("Usuario Agente: " + userAgent);
            System.out.println("Dirección IP: " + ipAddress);
            System.out.println("Dominio del Cliente: " + clientDomain);
            System.out.println("Sistema Operativo: " + operatingSystem);
	        assert url != null;
	        context.redirect(url);
        });

    }


    private static String parseOperatingSystem(String userAgent) {
        String os = "Unknown";
        if (userAgent != null && !userAgent.isEmpty()) {
            if (userAgent.toLowerCase().contains("windows")) {
                os = "Windows";
            } else if (userAgent.toLowerCase().contains("macintosh") || userAgent.toLowerCase().contains("mac os")) {
                os = "Mac OS";
            } else if (userAgent.toLowerCase().contains("linux")) {
                os = "Linux";
            } else if (userAgent.toLowerCase().contains("android")) {
                os = "Android";
            } else if (userAgent.toLowerCase().contains("iphone") || userAgent.toLowerCase().contains("ipad")) {
                os = "iOS";
            }
        }
        return os;
    }

    private static String parseBrowser(String userAgent) {
        String browser = "Unknown";
        if (userAgent != null && !userAgent.isEmpty()) {
            if (userAgent.toLowerCase().contains("chrome")) {
                browser = "Chrome";
            } else if (userAgent.toLowerCase().contains("firefox")) {
                browser = "Firefox";
            } else if (userAgent.toLowerCase().contains("safari")) {
                browser = "Safari";
            } else if (userAgent.toLowerCase().contains("opera")) {
                browser = "Opera";
            } else if (userAgent.toLowerCase().contains("edge")) {
                browser = "Edge";
            } else if (userAgent.toLowerCase().contains("ie") || userAgent.toLowerCase().contains("msie")) {
                browser = "Internet Explorer";
            }
        }
        return browser;
    }
}
