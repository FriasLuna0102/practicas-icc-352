package org.example.controladores;

import io.javalin.Javalin;
import org.example.encapsulaciones.Usuario;
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

			Usuario usuario = UsuarioServices.getInstancia().getUsuarioLogueado();

			Map<String,Object> model = new HashMap<>();
			model.put("usuario", usuario);

			context.render("publico/html/index.html", model);
		});
	}
}
