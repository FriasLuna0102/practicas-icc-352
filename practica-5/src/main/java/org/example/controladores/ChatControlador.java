package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.Blog;
import org.example.clases.Usuario;
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

					Map<String,Object> modelo = new HashMap<>();
					modelo.put("usuario", Blog.getInstance().getUsuario());
					context.render("publico/html/adminchat.html", modelo);
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
