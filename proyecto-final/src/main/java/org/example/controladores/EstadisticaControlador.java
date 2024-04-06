package org.example.controladores;

import io.javalin.Javalin;
import org.example.encapsulaciones.ShortURL;
import org.example.servicios.URLServices;
import org.example.servicios.mongo.URLODM;
import org.example.utils.ControladorClass;

import java.util.HashMap;
import java.util.Map;

public class EstadisticaControlador extends ControladorClass {

	public EstadisticaControlador(Javalin app){
		super(app);
	}

	@Override
	public void aplicarRutas() {

		app.get("/estadistica/{codigo}", context -> {
			String codigoUrl = context.pathParam("codigo");
			ShortURL shortURL = URLODM.getInstance().buscarUrlByCodig(codigoUrl);

			Map<String,Object> model = new HashMap<>();
			model.put("url", shortURL);

			context.render("publico/html/estadistica.html", model);
		});
	}
}
