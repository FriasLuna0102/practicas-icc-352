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
    private static Map<String, Session> adminInfo = new HashMap<>();
    private static Map<String, Session> userInfo = new HashMap<>();
    private static Map<Session, Set<Session>> adminToUserSessions = new HashMap<>();
    private static Map<Session,String> userSessions = new HashMap<>();
    public static List<HistorialChatUsuario> historialChatUsuarios = new ArrayList<>();

    public ChatSocket(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarRutas(){

        app.ws("/admin-chat", wsConfig -> {
            wsConfig.onConnect(ctx -> {
                String nombreAdmin = ctx.queryParam("adminName");
                adminSesions.put(ctx.session, ctx.getSessionId());
                adminInfo.put(ctx.getSessionId(), ctx.session);

                System.out.println("Admin conectado");

                // Mandar al socket su id de sesion
                ctx.session.getRemote().sendString( ctx.getSessionId() + "[@#Id#@]");
            });

            wsConfig.onMessage(ctx -> {

                /*
                for (Map.Entry<Session,String> entry : userSessions.entrySet()){
                    entry.getKey().getRemote().sendString(ctx.message() + "," + "jhonny");
                }

                 */
                for (HistorialChatUsuario historial: historialChatUsuarios){
                    if (ctx.getSessionId().equals(historial.getAdminSession())){

                        System.out.println("Este es el mensaje del admin: " + ctx.message());

                        userInfo.get(historial.getSession()).getRemote().sendString(ctx.message() + "," + "diablo");
                        //ctx.session.getRemote().sendString(ctx.message());
                    }
                }

            });

            wsConfig.onClose(ctx -> {
                adminSesions.remove(ctx.session);
                adminInfo.remove(ctx.getSessionId());
                System.out.println("Admin desconectado");
            });
        });

        app.ws("/user-chat", wsConfig -> {
            wsConfig.onConnect(ctx -> {
                String ruta = ctx.queryParam("ruta");
                String nombreUser = ctx.queryParam("nombre");
                userSessions.put(ctx.session, nombreUser);
                userInfo.put(ctx.getSessionId(), ctx.session);
                System.out.println("Usuario conectado");

                //Enviando id a usuario para que este lo almacene
                ctx.session.getRemote().sendString(ctx.getSessionId() + "[ID]");

                //Enviando nombre de usuario para visualizar en chat de admin
                for (Map.Entry<Session,String> entry : adminSesions.entrySet()){
                    // URGENTE : 1 sera el identificador para nombre de usuario, cambiar
                    entry.getKey().getRemote().sendString("1" + nombreUser + ":" + ctx.getSessionId());
                }

                // Guardando nuevo chat
                HistorialChatUsuario historial = new HistorialChatUsuario(ctx.getSessionId(), nombreUser, null);
                historialChatUsuarios.add(historial);
            });

            wsConfig.onMessage(ctx -> {
                String nombreUser = ctx.queryParam("nombre");

                /*
                for (Map.Entry<Session,String> entry : adminSesions.entrySet()){
                    entry.getKey().getRemote().sendString(ctx.message() + "," + nombreUser);
                }

                 */

                for (HistorialChatUsuario historial : historialChatUsuarios){
                    if (historial.getSession().equals(ctx.getSessionId())){
                        historial.addMensaje(ctx.message());
                        if (historial.getAdminSession() != null){
                            adminInfo.get(historial.getAdminSession()).getRemote().sendString(ctx.message());
                            System.out.println("Este es el mensaje del user: " + ctx.message());
                        }
                    }
                }


                //enviarMensajeToAdmin(ctx.message(), ctx.session);
            });

            // Eliminar usuario de la lista de historial
            wsConfig.onClose(ctx -> {
                userSessions.remove(ctx.session);
                userInfo.remove(ctx.getSessionId());
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
