package org.example;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.JavalinRenderer;
import io.javalin.rendering.template.JavalinThymeleaf;
import org.example.clases.Usuario;
import org.example.controladores.*;
import org.example.services.BootStrapServices;
import org.example.services.RolesServices;
import org.example.services.UsuarioServices;
import org.example.util.ControladorClass;
import org.example.util.RolesApp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        RolesServices.getInstancia().crear(new RolesApp("1","admin"));
        RolesServices.getInstancia().crear(new RolesApp("2","usuario"));

        List<RolesApp> listRole = new ArrayList<>();
        listRole.add(RolesServices.getInstancia().findByCodigo("1"));

        List<RolesApp> listRole2 = new ArrayList<>();
        listRole2.add(RolesServices.getInstancia().findByCodigo("1"));
        listRole2.add(RolesServices.getInstancia().findByCodigo("2"));


//        Usuario usuario1 = new Usuario("admin","RanStar","admin",listRole);
//        Usuario usuario2 = new Usuario("star","Starlin","123",listRole2);
//
//        //if (UsuarioServices.getInstancia().findAllByUsername(usuario1.getUsuario()) == null) {
//            UsuarioServices.getInstancia().crear(usuario1);
//            UsuarioServices.getInstancia().crear(usuario2);

        //}


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

        //Llmadas de controladores:
        new Login(app).aplicarRutas();
        new Formulario(app).aplicarRutas();
        new PlantillaGeneral(app).aplicarRutas();
        new AdministrarUsuario(app).aplicarRutas();
        new AdministrarRegistros(app).aplicarRutas();

        app.get("/", cxt ->{
            cxt.redirect("/login");
        });

    }


    public static String getModoConexion(){
        return modoConexion;
    }

}