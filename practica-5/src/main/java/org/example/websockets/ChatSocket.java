package org.example.websockets;

import io.javalin.Javalin;
import org.example.util.ControladorClass;
import org.thymeleaf.processor.xmldeclaration.AbstractXMLDeclarationProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatSocket extends ControladorClass {

	private static List<String> adminsesions = new ArrayList<>();
	private static List<String> usersessions = new ArrayList<>();

	public ChatSocket(Javalin app) {
		super(app);
	}

	public void aplicarRutas(){

		app.ws("/admin-chat", wsConfig -> {
			wsConfig.onConnect(ctx -> {
				String nombreAdmin = ctx.queryParam("adminName");
				adminsesions.add(nombreAdmin);
			});
		});

		app.ws("/user-chat", wsConfig -> {
			wsConfig.onConnect(ctx -> {
				String nombreUser = ctx.queryParam("username");
			});
		});
	}

}
