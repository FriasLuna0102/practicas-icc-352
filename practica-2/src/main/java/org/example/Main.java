package org.example;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import  org.example.clases.Articulo;
import  org.example.clases.Comentario;
import  org.example.clases.Usuario;
import  org.example.clases.Etiqueta;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        Etiqueta prueba = new Etiqueta(1014,"SuperHeroes");

        System.out.println(prueba.getId());

        prueba.setId(256);

        System.out.println(prueba.getId());

//Creando la instancia del servidor y configurando.
        Javalin app = Javalin.create(config ->{
            //configurando los documentos estaticos.
            config.staticFiles.add(staticFileConfig -> {
                staticFileConfig.hostedPath = "/";
                staticFileConfig.directory = "/publico";
                staticFileConfig.location = Location.CLASSPATH;
                staticFileConfig.precompress=false;
                staticFileConfig.aliasCheck=null;

            });

        });

        app.start(getHerokuAssignedPort());

        app.post("/login", cxt -> {
            String usuario = cxt.formParam("username");
            String password = cxt.formParam("password");

            System.out.println("Username: " + usuario);
            System.out.println("Password: " + password);

            if(usuario == null & password == null){
                cxt.redirect("login.html");
            }else{
                System.out.println("good job");
            }

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