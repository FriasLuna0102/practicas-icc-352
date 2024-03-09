package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.Blog;
import org.example.clases.HistorialChatUsuario;
import org.example.util.ControladorClass;
import org.example.websockets.ChatSocket;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

import static io.javalin.apibuilder.ApiBuilder.path;

public class ChatControlador extends ControladorClass {

	public ChatControlador(Javalin app) {
		super(app);
	}

	@Override
	public void aplicarRutas() {

		app.routes(() ->{
			path("/chat", ()-> {


				app.post("/chat/admin", context -> {

					Map<String,Object> modelo = new HashMap<>();
					modelo.put("usuario", Blog.getInstance().getUsuario());
					context.render("publico/html/adminchat.html", modelo);
				});

				app.post("/chat/user", context -> {
					String nombre = context.formParam("chateador");
					String idChat = UUID.randomUUID().toString();

					context.redirect("/chat/user" + "?id=" + idChat + "&nombre=" + nombre);
				});

				app.get("/chat/user", context -> {

					String nombre = context.queryParam("nombre");

					Map<String,Object> modelo = new HashMap<>();
					modelo.put("nombre", nombre);

					context.render("publico/html/userchat.html", modelo);
				});

                app.get("/desconectar", context -> {
                    String idSessionAdmin = context.queryParam("idSessionAdmin");

                    for (HistorialChatUsuario historialChatUsuario: ChatSocket.historialChatUsuarios){
                        if (historialChatUsuario.getAdminSession().equals(idSessionAdmin)){
                            historialChatUsuario.setAdminSession(null);
                        }
                    }

                    context.result("Desconectado con éxito");
                });

                app.get("/historial", context -> {
                    String socket = context.queryParam("websocket");
                    String idSessionAdmin = context.queryParam("idSessionAdmin");
                    StringBuilder stringBuilder = new StringBuilder();

                    // Eliminar el idSessionAdmin de todos los historialChatUsuario
                    for (HistorialChatUsuario historialChatUsuario: ChatSocket.historialChatUsuarios){
                        if (historialChatUsuario.getAdminSession() != null && historialChatUsuario.getAdminSession().equals(idSessionAdmin)){
                            historialChatUsuario.setAdminSession(null);
                        }
                    }

                    // Establecer el idSessionAdmin para el chat actual
                    for (HistorialChatUsuario historialChatUsuario: ChatSocket.historialChatUsuarios){
                        if (historialChatUsuario.getSession().equals(socket)){
                            int i = historialChatUsuario.getMensajes().size();
                            historialChatUsuario.setAdminSession(idSessionAdmin);

                            for (int k = 0; k < i; k++){
                                stringBuilder.append(historialChatUsuario.getMensajes().get(k));
                                stringBuilder.append("\n");
                            }
                        }
                    }

                    String resultado = invertirChat(stringBuilder.toString());
                    context.result(resultado);
                });

            });
		});
	}

	public String invertirChat(String mensaje){

		Document document = Jsoup.parse(mensaje);

		Elements mensajesToIzquierda = document.getElementsByClass("chat-message-right");
		Elements mensajesToDerecha = document.getElementsByClass("chat-message-left");

		for (Element element: mensajesToIzquierda){
			element.removeClass("chat-message-right");
			element.addClass("chat-message-left");
		}

		for (Element element: mensajesToDerecha){
			element.removeClass("chat-message-left");
			element.addClass("chat-message-right");
		}

		return document.toString();
  }
}
