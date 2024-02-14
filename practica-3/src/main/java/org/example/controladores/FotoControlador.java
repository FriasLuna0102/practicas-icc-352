package org.example.controladores;

import io.javalin.Javalin;
import org.example.services.FotoServices;

import static io.javalin.apibuilder.ApiBuilder.path;

public class FotoControlador {

	private Javalin app;
	private FotoServices fotoServices = FotoServices.getInstancia();

	public FotoControlador(Javalin app){
		this.app = app;
	}

	public void aplicarRutas(){

		app.routes(() ->{
			path("fotos",() -> {


			})
		});
	}
}
