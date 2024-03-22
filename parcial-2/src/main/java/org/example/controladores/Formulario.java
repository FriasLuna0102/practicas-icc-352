package org.example.controladores;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import org.example.clases.Registro;
import org.example.clases.Usuario;
import org.example.services.RegistroServices;
import org.example.services.UsuarioServices;
import org.example.util.ControladorClass;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
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

                ws("/sincronizar", wsConfig -> {
                    wsConfig.onConnect(context -> {
                        String sessionId = context.getSessionId();
                        System.out.println("Se conectó: " + sessionId);

                        // Mandar al socket su id de sesion
                        context.session.getRemote().sendString(sessionId + "[@#Id#@]");
                    });

                    wsConfig.onMessage(cxt -> {
                        // Manejar el mensaje recibido del cliente
                        System.out.println("Mensaje recibido del cliente: " + cxt.message());

                        // Analizar el mensaje JSON recibido
                        ObjectMapper objectMapper = new ObjectMapper();

                        try {
                            // Convierte el mensaje JSON en una lista de registros
                            List<Registro> registros = objectMapper.readValue(cxt.message(), new TypeReference<List<Registro>>() {});


                            for(Registro registro : registros){
                                Usuario user = registro.getUsuario();

                                Usuario usuar = UsuarioServices.getInstancia().findByUsername(user.getUsername());

                                System.out.println(usuar.getNombre());
                                System.out.println(usuar.getUsuario());
                                System.out.println(usuar.getPassword());

                                registro.setUsuario(usuar);
                                registro.setEstado(true);
                                RegistroServices.getInstancia().crear(registro);
                            }

                            // Enviar una respuesta al cliente si es necesario
                            cxt.session.getRemote().sendString("Mensaje recibido por el servidor: " + cxt.message());
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                            cxt.session.getRemote().sendString("Error al procesar el mensaje JSON" + e.getMessage());
                        }
                    });
                });


//                post("/sincronizar", ctx -> {
//                    try {
//                        ObjectMapper objectMapper = new ObjectMapper();
//                        System.out.println(ctx.body());
//                        List<Registro> registros = objectMapper.readValue(ctx.body(), new TypeReference<List<Registro>>() {});
//                        for(Registro reg : registros){
//                            Usuario user = reg.getUsuario();
////
//                            Usuario usuar = UsuarioServices.getInstancia().findByUsername(user.getUsername());
//                            reg.setUsuario(usuar);
//                            reg.setEstado(true);
//                            RegistroServices.getInstancia().crear(reg);
//                        }
//
//                        // Manejar la respuesta del servidor
//                        if (registros != null && !registros.isEmpty()) {
//                            System.out.println("Sincronización exitosa");
//                            ctx.status(200).result("Sincronización exitosa");
//                        } else {
//                            System.err.println("Error en la solicitud AJAX: " + " - " + "No se encontraron registros para sincronizar");
//                            ctx.status(500).result("Error en la sincronización: " + "No se encontraron registros para sincronizar");
//                        }
//                    } catch (JsonProcessingException e) {
//                        e.printStackTrace();
//                        ctx.status(500).result("Error al procesar el mensaje JSON: " + e.getMessage());
//                    }
//                });

                get("/obtenerRegistros", cxt ->{
                    List<Registro> todoRegistros = RegistroServices.getInstancia().obtenerTodosLosRegistros();
                    cxt.json(todoRegistros);
                });


            });

        });

    }
}
