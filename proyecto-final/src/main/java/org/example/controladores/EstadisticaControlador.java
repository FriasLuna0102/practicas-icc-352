package org.example.controladores;

import com.google.gson.Gson;
import io.javalin.Javalin;
import org.example.encapsulaciones.EstadisticaURL;
import org.example.encapsulaciones.ShortURL;
import org.example.servicios.URLServices;
import org.example.servicios.mongo.EstadisticaODM;
import org.example.servicios.mongo.URLODM;
import org.example.utils.ControladorClass;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class EstadisticaControlador extends ControladorClass {

	private ShortURL shortURL;

	public EstadisticaControlador(Javalin app){
		super(app);
	}

	@Override
	public void aplicarRutas() {

		app.routes(() ->{
			path("/estadistica", () -> {

				get("{codigo}", context -> {
					String codigoUrl = context.pathParam("codigo");
					shortURL = URLODM.getInstance().buscarUrlByCodig(codigoUrl);

					Map<String,Object> model = new HashMap<>();
					model.put("url", shortURL);

					obtenerDataByDiasOfSemana();

					context.render("publico/html/estadistica.html", model);
				});

				get("dias", context -> {
					context.result(obtenerDataByDiasOfSemana());
				});

			});
		});

	}

	public String obtenerDataByDiasOfSemana(){
		String json = "";
		Gson gson = new Gson();
		EstadisticaURL estadisticaURL = EstadisticaODM.getInstance().buscarEstadisticaByCodigoOfUrl(shortURL.getCodigo());
		Map<String, Integer> contador = new LinkedHashMap<>();
		inicializarContadorOfDias(contador);

		// Formateador para analizar las fechas
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		for (Map.Entry<String, Integer> entry: estadisticaURL.getHorasAcceso().entrySet()){
			LocalDate date = LocalDate.parse(entry.getKey(), formatter);
			DayOfWeek dayOfWeek = date.getDayOfWeek();

			// Convertir el día de la semana a String (Lunes, Martes, etc.)
			String dayOfWeekString = dayOfWeek.toString();

			contador.put(dayOfWeekString, contador.getOrDefault(dayOfWeekString, 0) + 1);
		}

		json = gson.toJson(contador.values());
		System.out.println(json);
		return json;
	}

	public void inicializarContadorOfDias(Map<String, Integer> contador){
		// Inicializar el mapa con los siete días de la semana en inglés y en mayúsculas
		contador.put("MONDAY", 0);
		contador.put("TUESDAY", 0);
		contador.put("WEDNESDAY", 0);
		contador.put("THURSDAY", 0);
		contador.put("FRIDAY", 0);
		contador.put("SATURDAY", 0);
		contador.put("SUNDAY", 0);
	}
}
