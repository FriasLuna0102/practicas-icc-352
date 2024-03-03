package org.example.websockets;

import io.javalin.Javalin;
import org.eclipse.jetty.websocket.api.Session;
import org.example.util.ControladorClass;
import org.thymeleaf.processor.xmldeclaration.AbstractXMLDeclarationProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatSocket extends ControladorClass {

	private static Map<Session,String> adminSesions = new HashMap<>();
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
			});


			wsConfig.onClose(ctx -> {
				adminSesions.remove(ctx.session);
			});
		});

		app.ws("/user-chat", wsConfig -> {
			wsConfig.onConnect(ctx -> {
				String nombreUser = ctx.queryParam("username");
				userSessions.put(ctx.session, nombreUser);
			});

			wsConfig.onMessage(ctx -> {
				System.out.println("Este es el mensaje: " + ctx.message());
				enviarMensaje(ctx.message(), ctx.session);
			});

			wsConfig.onClose(ctx -> {
				userSessions.remove(ctx.session);
			});
		});
	}

	public static void enviarMensaje(String mensaje, Session session) throws IOException {

		session.getRemote().sendString(mensaje);
	}
}
