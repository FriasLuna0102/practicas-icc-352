package org.example;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.JavalinRenderer;
import io.javalin.rendering.template.JavalinThymeleaf;
import javassist.CtBehavior;
import org.example.controladores.IndexControlador;
import org.example.controladores.LoginControlador;
import org.example.controladores.UsuarioControlador;
import org.example.encapsulaciones.Usuario;

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
        new UsuarioControlador(app).aplicarRutas();

        Usuario user1 = new Usuario("star","Starlin","123",false);


	}

}