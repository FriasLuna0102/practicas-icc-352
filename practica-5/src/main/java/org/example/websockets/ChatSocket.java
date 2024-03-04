package org.example.websockets;

import io.javalin.Javalin;
import org.eclipse.jetty.websocket.api.Session;
import org.example.util.ControladorClass;
import org.thymeleaf.processor.xmldeclaration.AbstractXMLDeclarationProcessor;

import java.io.IOException;
import java.util.*;

public class ChatSocket extends ControladorClass {

    private static Map<Session,String> adminSesions = new HashMap<>();
    private static Map<Session, Set<Session>> adminToUserSessions = new HashMap<>();
    private static Map<Session,String> userSessions = new HashMap<>();

    public ChatSocket(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarRutas(){

        app.ws("/admin-chat", wsConfig -> {
            wsConfig.onConnect(ctx -> {
                String nombreAdmin = ctx.queryParam("adminName");
                adminSesions.put(ctx.session, nombreAdmin);
                adminToUserSessions.put(ctx.session, new HashSet<>());
            });

            wsConfig.onMessage(ctx -> {

                System.out.println("Este es el mensaje: " + ctx.message());
            });

            wsConfig.onClose(ctx -> {
                adminSesions.remove(ctx.session);
                adminToUserSessions.remove(ctx.session);
            });
        });

        app.ws("/user-chat", wsConfig -> {
            wsConfig.onConnect(ctx -> {
                String nombreUser = ctx.queryParam("username");
                userSessions.put(ctx.session, nombreUser);
            });

            wsConfig.onMessage(ctx -> {
                System.out.println("Este es el mensaje: " + ctx.message());

                enviarMensajeToAdmin(ctx.message(), ctx.session);
            });

            wsConfig.onClose(ctx -> {
                userSessions.remove(ctx.session);
            });
        });
    }

    private static void enviarMensajeToAdmin(String mensaje, Session sessionUsuario){

        for (Map.Entry<Session, String> entry : adminSesions.entrySet()){
            Session sessionAdmin = entry.getKey();

            // Verificar si el administrador tiene al usuario conectado
            Set<Session> userSessionsAdmin = adminToUserSessions.get(sessionAdmin);
            if (userSessionsAdmin.contains(sessionUsuario)) {
                try {
                    // Enviar el mensaje al administrador
                    sessionAdmin.getRemote().sendString(mensaje);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
