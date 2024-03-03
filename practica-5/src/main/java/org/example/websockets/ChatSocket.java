package org.example.websockets;

import io.javalin.Javalin;
import org.example.util.ControladorClass;
import org.thymeleaf.processor.xmldeclaration.AbstractXMLDeclarationProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatSocket extends ControladorClass {

	private static Map<String,String> adminSesions = new HashMap<>();
	private static Map<String,String> userSessions = new HashMap<>();

	public ChatSocket(Javalin app) {
		super(app);
	}

	@Override
	public void aplicarRutas(){

		app.ws("/admin-chat", wsConfig -> {
			wsConfig.onConnect(ctx -> {
				String nombreAdmin = ctx.queryParam("adminName");
				adminSesions.put(ctx.getSessionId(), nombreAdmin);
			});


			wsConfig.onClose(ctx -> {
				adminSesions.remove(ctx.getSessionId());
			});
		});

		app.ws("/user-chat", wsConfig -> {
			wsConfig.onConnect(ctx -> {
				String nombreUser = ctx.queryParam("username");
				userSessions.put(ctx.getSessionId(), nombreUser);
			});

			wsConfig.onClose(ctx -> {
				userSessions.remove(ctx.getSessionId());
			});
		});
	}

}
