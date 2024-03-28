package org.example.controladores;

import io.javalin.Javalin;
import org.example.encapsulaciones.Usuario;
import org.example.servicios.mongo.UsuarioODM;
import org.example.utils.ControladorClass;

public class RegistroControlador extends ControladorClass {

	public RegistroControlador (Javalin app){
		super(app);
	}

	@Override
	public void aplicarRutas() {

		app.get("/registro", context -> {
			context.render("publico/html/registro.html");
		});

		app.post("/registro", context -> {
			String nombre = context.formParam("nombre");
			String username = context.formParam("username");
			String password = context.formParam("password");

			Usuario usuario = new Usuario(username,nombre,password,true);

			UsuarioODM UsuarioODM = new UsuarioODM();
			UsuarioODM.guardarUsuario(usuario);
		});
	}
}
