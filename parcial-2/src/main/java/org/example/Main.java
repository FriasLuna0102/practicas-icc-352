package org.example;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.JavalinRenderer;
import io.javalin.rendering.template.JavalinThymeleaf;
import org.example.services.BootStrapServices;

public class Main {
    private static String modoConexion = "";

    public static void main(String[] args) {



        if(args.length >= 1){
            modoConexion = args[0];
            System.out.println("Modo de Operacion: "+modoConexion);
        }

        //Iniciando la base de datos.
        if(modoConexion.isEmpty()) {
            System.out.println("hey");
            BootStrapServices.getInstancia().init();
        }

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

        app.get("/", cxt ->{
            cxt.redirect("/html/formulario.html");
        });

        app.post("/captura", cxt ->{
            String nombre = cxt.formParam("nombre");
            String sector = cxt.formParam("sector");
            String nivelEscolar = cxt.formParam("nivelEscolar");

            System.out.println(nombre);
            System.out.println(sector);
            System.out.println(nivelEscolar);

        });
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 7000; //Retorna el puerto por defecto en caso de no estar en Heroku.
    }


    public static String getModoConexion(){
        return modoConexion;
    }

}