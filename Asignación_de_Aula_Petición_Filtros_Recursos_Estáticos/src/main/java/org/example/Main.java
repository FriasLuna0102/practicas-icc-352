package org.example;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.JavalinRenderer;


public class Main {
    public static void main(String[] args) {

        //Creacion de Usuario admin:


        //Creando la instancia del servidor y configurando.
        Javalin app = Javalin.create(config ->{
            //configurando los documentos estaticos.
            config.staticFiles.add(staticFileConfig -> {
                staticFileConfig.hostedPath = "/";
                staticFileConfig.directory = "/publico";
                staticFileConfig.location = Location.CLASSPATH;
                staticFileConfig.precompress=false;
                staticFileConfig.aliasCheck=null;

                //Importante para permitir renderizar los html:
                //JavalinRenderer.register(new JavalinThymeleaf(), ".html");

            });

        });
        app.start(getHerokuAssignedPort());

        app.get("/", cxt -> {
            String session = cxt.sessionAttribute("currentUser");
            if(session == null){
                cxt.redirect("/login");
            }else{
                // Ajusta la ruta del archivo según la estructura de tu proyecto
                cxt.render("publico/bienvenido.html");
            }
        });


        app.get("/login", cxt ->{
            String session = cxt.sessionAttribute("currentUser");
            if(session == null){
                cxt.render("publico/login.html");
            }else{
                // Ajusta la ruta del archivo según la estructura de tu proyecto
                cxt.redirect("/bienvenido");
            }
        });


        //Redirrecionar a la barra.
        app.get("/bienvenido", ctx ->{
            if(ctx.sessionAttribute("currentUser") == null){
                ctx.redirect("/login");
            }else {
                ctx.redirect("/");
            }
        });

        app.post("/formulario", cxt ->{

            String username = cxt.formParam("name");
            String password = cxt.formParam("password");

            if(username.equals("star") && password.equals("123")){
                cxt.sessionAttribute("currentUser", username);
                cxt.redirect("/bienvenido");
            }else {
                cxt.redirect("/login");
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