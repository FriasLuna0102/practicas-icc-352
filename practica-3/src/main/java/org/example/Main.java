package org.example;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.template.JavalinThymeleaf;
import org.example.clases.Blog;
import  org.example.clases.Usuario;
import org.example.controladores.*;
import io.javalin.rendering.JavalinRenderer;
import org.example.services.BootStrapServices;
import org.example.services.UsuarioServices;


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

        for(int i=0;i<2;i++){
            UsuarioServices.getInstancia().crear(new Usuario("star","Starlin","123",true,false));

        }
        //Creacion de Usuarios admin:
        Usuario usuario1 = new Usuario("star","Starlin","123",true,false);
        Usuario usuario2 = new Usuario("admin", "randae", "admin", true, false);
        Blog.getInstance().addUsuario(usuario1);
        Blog.getInstance().addUsuario(usuario2);


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
        new EliminarArticulo(app).aplicarRutas();
        new editarArticulo(app).aplicarRutas();
        new agregarComentario(app).aplicarRutas();
        new mostrarUsuarios(app).aplicarRutas();
        new CookiesSeccionesControlador(app).aplicarRutas();


        app.get("/blog", cxt -> {
            cxt.redirect("blog.html");
        });

        app.get("/", cxt->{

            if (cxt.sessionAttribute("currentUser") == null){
                cxt.redirect("/login");
            }else{
                cxt.redirect("/blogUsuario");
            }

        });

        /*
        //Importante esto:
        app.before("/html/{none}", cxt -> {
            cxt.redirect("/");
        });*/
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