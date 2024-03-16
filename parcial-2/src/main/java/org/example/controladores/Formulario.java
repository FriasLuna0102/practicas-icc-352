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



            });

        });

    }
}
