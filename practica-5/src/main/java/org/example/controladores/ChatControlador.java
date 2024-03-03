package org.example.controladores;

import io.javalin.Javalin;
import org.example.util.ControladorClass;

import java.util.HashMap;
import java.util.Map;

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

					context.render("publico/html/adminchat.html");
				});

				app.post("/chat/user", context -> {
					String nombre = context.formParam("chateador");

					Map<String,Object> modelo = new HashMap<>();
					modelo.put("nombre", nombre);

					context.render("publico/html/userchat.html", modelo);
				});

			});
		});
	}
}
