package org.example.controladores;

import io.javalin.Javalin;
import org.example.servicios.UsuarioServices;
import org.example.utils.ControladorClass;

import java.util.HashMap;
import java.util.Map;

public class IndexControlador extends ControladorClass {

	public IndexControlador(Javalin app){
		super(app);
	}

	@Override
	public void aplicarRutas() {

		app.get("/", context -> {

			Map<String,Object> model = new HashMap<>();
			model.put("usuario", UsuarioServices.getInstancia().getUsuarioLogueado());

			context.render("publico/html/index.html", model);
		});
	}
}
