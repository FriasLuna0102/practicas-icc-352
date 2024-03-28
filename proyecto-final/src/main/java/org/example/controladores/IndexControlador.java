package org.example.controladores;

import io.javalin.Javalin;
import org.example.utils.ControladorClass;

public class IndexControlador extends ControladorClass {

	public IndexControlador(Javalin app){
		super(app);
	}

	@Override
	public void aplicarRutas() {

		app.get("/", context -> {
			context.render("publico/html/index.html");
		});
	}
}
