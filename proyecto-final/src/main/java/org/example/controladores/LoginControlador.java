package org.example.controladores;

import io.javalin.Javalin;
import org.example.utils.ControladorClass;

public class LoginControlador extends ControladorClass {

	public LoginControlador (Javalin app){
		super(app);
	}

	@Override
	public void aplicarRutas() {

		app.get("/login", context -> {
			context.render("publico/html/login.html");
		});

		app.post("/login", context -> {
			String username = context.formParam("username");
			String password = context.formParam("password");


		});
	}
}
