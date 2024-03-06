package org.example.websockets;

import io.javalin.Javalin;
import org.eclipse.jetty.websocket.api.Session;
import org.example.clases.HistorialChatUsuario;
import org.example.util.ControladorClass;
import org.thymeleaf.processor.xmldeclaration.AbstractXMLDeclarationProcessor;

import java.io.IOException;
import java.util.*;

public class ChatSocket extends ControladorClass {

    private static Map<Session,String> adminSesions = new HashMap<>();
    private static Map<Session, Set<Session>> adminToUserSessions = new HashMap<>();
    private static Map<Session,String> userSessions = new HashMap<>();
    private static List<HistorialChatUsuario> historialChatUsuarios = new ArrayList<>();

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

                System.out.println("Admin conectado");
            });

            wsConfig.onMessage(ctx -> {
                String nombreAdmin = ctx.queryParam("adminName");

                for (Map.Entry<Session,String> entry : userSessions.entrySet()){
                    entry.getKey().getRemote().sendString(ctx.message() + "," + nombreAdmin);
                }

                System.out.println("Este es el mensaje: " + ctx.message());
            });

            wsConfig.onClose(ctx -> {
                adminSesions.remove(ctx.session);
                adminToUserSessions.remove(ctx.session);
                System.out.println("Admin desconectado");
            });
        });

        app.ws("/user-chat", wsConfig -> {
            wsConfig.onConnect(ctx -> {
                String ruta = ctx.queryParam("ruta");
                String nombreUser = ctx.queryParam("nombre");
                userSessions.put(ctx.session, nombreUser);
                System.out.println("Usuario conectado");

                //Enviando id a usuario para que este lo almacene
                ctx.session.getRemote().sendString(ctx.getSessionId() + "[ID]");

                //Enviando nombre de usuario para visualizar en chat de admin
                for (Map.Entry<Session,String> entry : adminSesions.entrySet()){
                    // URGENTE : 1 sera el identificador para nombre de usuario, cambiar
                    entry.getKey().getRemote().sendString("1" + nombreUser + ":" + ruta);
                }

                // Guardando nuevo chat
                HistorialChatUsuario historial = new HistorialChatUsuario(ctx.session, nombreUser, ruta);
                historialChatUsuarios.add(historial);
            });

            wsConfig.onMessage(ctx -> {
                String nombreUser = ctx.queryParam("nombre");

                for (Map.Entry<Session,String> entry : adminSesions.entrySet()){
                    entry.getKey().getRemote().sendString(ctx.message() + "," + nombreUser);
                }

                for (HistorialChatUsuario historial : historialChatUsuarios){
                    if (historial.getSession().equals(ctx.session)){
                        historial.addMensaje(ctx.message());
                        System.out.println("Este es el mensaje guardado: " + ctx.message());
                    }
                }


                //enviarMensajeToAdmin(ctx.message(), ctx.session);
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
