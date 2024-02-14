package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.Foto;
import org.example.services.FotoServices;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class FotoControlador {

	private Javalin app;
	private FotoServices fotoServices = FotoServices.getInstancia();

	public FotoControlador(Javalin app){
		this.app = app;
	}

	public void aplicarRutas(){

		app.routes(() ->{
			path("/fotos",() -> {

				get("/add",context -> {

				});

				get("/eliminar", context -> {
					try {
						long id = context.pathParamAsClass("idfoto", long.class).get();
						Foto foto = fotoServices.find(id);
						if (foto !=  null){
							fotoServices.eliminar(foto);
						}
					}catch(Exception e) {
						System.out.println(e.getMessage());
					}
				});

			});
		});
	}
}
