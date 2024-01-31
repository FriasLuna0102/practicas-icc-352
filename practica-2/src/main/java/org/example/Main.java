package org.example;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.FileRenderer;
import io.javalin.rendering.template.JavalinThymeleaf;
import  org.example.clases.Articulo;
import  org.example.clases.Comentario;
import  org.example.clases.Usuario;
import  org.example.clases.Etiqueta;
import org.example.controladores.CrearArticulo;
import org.example.controladores.CrearUsuario;
import org.example.controladores.Login;
import org.example.controladores.PlantillasControlador;
import org.thymeleaf.TemplateEngine;
import io.javalin.rendering.template.JavalinThymeleaf;
import io.javalin.rendering.JavalinRenderer;

import java.util.*;

import static org.example.clases.Usuario.getUsuarios;
import static org.example.clases.Usuario.setUsuario;


public class Main {
    public static void main(String[] args) {

        //Creacion de Usuario admin:

        Usuario usuario1 = new Usuario("star","Starlin","123",true,true);
        Usuario.setUsuario(usuario1);


        //Creando la instancia del servidor y configurando.
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

        });
        app.start(getHerokuAssignedPort());
        new Login(app).aplicarRutas();
        new PlantillasControlador(app).aplicarRutas();
        new CrearUsuario(app).aplicarRutas();
        new CrearArticulo(app).aplicarRutas();


        app.get("/blog", cxt -> {
            cxt.redirect("blog.html");
        });



    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 7000; //Retorna el puerto por defecto en caso de no estar en Heroku.
    }



}