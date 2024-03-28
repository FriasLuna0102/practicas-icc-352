package org.example.controladores;

import io.javalin.Javalin;
import org.example.encapsulaciones.Usuario;
import org.example.servicios.UsuarioServices;
import org.example.servicios.mongo.UsuarioODM;
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

			Usuario usuario = UsuarioODM.getInstance().buscarUsuarioByUsername(username);

			if (usuario != null && usuario.getPassword().equals(password)){
				UsuarioServices.getInstancia().setUsuarioLogueado(usuario);
				context.redirect("/");
				return;
			}

			context.redirect("/login");
		});

		app.get("/logout", context -> {
			UsuarioServices.getInstancia().setUsuarioLogueado(null);
			context.redirect("/");
		});
	}
}
