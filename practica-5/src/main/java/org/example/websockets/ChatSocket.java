package org.example.websockets;

import io.javalin.Javalin;
import org.eclipse.jetty.websocket.api.Session;
import org.example.clases.HistorialChatUsuario;
import org.example.util.ControladorClass;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class ChatSocket extends ControladorClass {

    private static Map<Session,String> adminSesions = new HashMap<>();
    private static Map<String, Session> adminInfo = new HashMap<>();
    private static Map<String, Session> userInfo = new HashMap<>();
    private static Map<Session,String> userSessions = new HashMap<>();
    public static List<HistorialChatUsuario> historialChatUsuarios = new ArrayList<>();

    public ChatSocket(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarRutas(){

        app.ws("/admin-chat", wsConfig -> {
            wsConfig.onConnect(ctx -> {
                adminSesions.put(ctx.session, ctx.getSessionId());
                adminInfo.put(ctx.getSessionId(), ctx.session);

                System.out.println("Admin conectado");

                // Mandar al socket su id de sesion
                ctx.session.getRemote().sendString(ctx.getSessionId() + "[@#Id#@]");

                // Enviar la lista de usuarios conectados al administrador recién conectado
                enviarUsuariosConectadosAdmin(ctx.session);
            });

            wsConfig.onMessage(ctx -> {

                for (HistorialChatUsuario historial: historialChatUsuarios){
                    if (ctx.getSessionId().equals(historial.getAdminSession())){

                        System.out.println("Este es el mensaje del admin: " + ctx.message());

                        String mensaje = invertirMensaje(ctx.message());
                        historial.addMensaje(mensaje);
                        userInfo.get(historial.getSession()).getRemote().sendString(mensaje);
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

                for (HistorialChatUsuario historial : historialChatUsuarios){
                    if (historial.getSession().equals(ctx.getSessionId())){
                        historial.addMensaje(ctx.message());
                        if (historial.getAdminSession() != null){
                            adminInfo.get(historial.getAdminSession()).getRemote().sendString(invertirMensaje(ctx.message()));
                            System.out.println("Este es el mensaje del user: " + ctx.message());
                        }
                    }
                }

            });

            // Eliminar usuario de la lista de historial
            wsConfig.onClose(ctx -> {
                userSessions.remove(ctx.session);
                userInfo.remove(ctx.getSessionId());
            });
        });
    }

    public String invertirMensaje(String mensaje){

        org.jsoup.nodes.Document document = Jsoup.parse(mensaje);

        Elements mensajesToIzquierda = document.getElementsByClass("chat-message-right");

        for (org.jsoup.nodes.Element element: mensajesToIzquierda){
            element.removeClass("chat-message-right");
            element.addClass("chat-message-left");
        }

        return document.toString();
    }

    private void enviarUsuariosConectadosAdmin(Session adminSession) throws IOException {
        // Itera sobre los usuarios conectados y envía sus nombres y sesiones al administrador
        for (Map.Entry<Session, String> entry : userSessions.entrySet()) {
            String nombreUser = entry.getValue();
            String sessionId = entry.getKey().getRemote().toString(); // Aquí deberías obtener el ID de la sesión del usuario de manera adecuada

            // Envía el mensaje al administrador
            adminSession.getRemote().sendString("1" + nombreUser + ":" + sessionId);
        }
    }
}
