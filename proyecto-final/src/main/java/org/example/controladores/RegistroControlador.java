package org.example.controladores;

import io.javalin.Javalin;
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

		});
	}
}
