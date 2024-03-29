package org.example;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.JavalinRenderer;
import io.javalin.rendering.template.JavalinThymeleaf;
import javassist.CtBehavior;
import org.example.controladores.*;
import org.example.encapsulaciones.Usuario;
import org.example.servicios.URLServices;
import org.example.servicios.mongo.UsuarioODM;

public class Main {
	public static void main(String[] args) {
		Javalin app = Javalin.create(config ->{
			//configurando los documentos estaticos.
			config.staticFiles.add(staticFileConfig -> {
				staticFileConfig.hostedPath = "/";
				staticFileConfig.directory = "/publico";
				staticFileConfig.location = Location.CLASSPATH;
				staticFileConfig.precompress=false;
				staticFileConfig.aliasCheck=null;
				JavalinRenderer.register(new JavalinThymeleaf(), ".html");

			});

			//Habilitando el CORS. Ver: https://javalin.io/plugins/cors#getting-started para más opciones.
			config.plugins.enableCors(corsContainer -> {
				corsContainer.add(corsPluginConfig -> {
					corsPluginConfig.anyHost();
				});
			});
		});
		app.start(7000);

		new IndexControlador(app).aplicarRutas();
		new LoginControlador(app).aplicarRutas();
		new RegistroControlador(app).aplicarRutas();
		new UsuarioControlador(app).aplicarRutas();
		new UrlControlador(app).aplicarRutas();

		// Crear usuario administrador
		if (UsuarioODM.getInstance().buscarUsuarioByUsername("admin") == null){
			Usuario admin = new Usuario("admin","admin","admin", false);
			UsuarioODM.getInstance().guardarUsuario(admin);
		}
	}

}