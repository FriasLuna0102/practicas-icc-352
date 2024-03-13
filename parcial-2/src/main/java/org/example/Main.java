package org.example;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.JavalinRenderer;
import io.javalin.rendering.template.JavalinThymeleaf;
import org.example.clases.Usuario;
import org.example.controladores.AdministrarUsuario;
import org.example.controladores.Formulario;
import org.example.controladores.Login;
import org.example.controladores.PlantillaGeneral;
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


        List<RolesApp> listRole = new ArrayList<>();
        listRole.add(new RolesApp());

        RolesServices.getInstancia().crear(listRole.getFirst());

        Usuario usuario1 = new Usuario("admin","RanStar","admin",listRole );

        //if (UsuarioServices.getInstancia().findAllByUsername(usuario1.getUsuario()) == null) {
            UsuarioServices.getInstancia().crear(usuario1);
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

        });
        app.start(7000);

        //Llmadas de controladores:
        new Login(app).aplicarRutas();
        new Formulario(app).aplicarRutas();
        new PlantillaGeneral(app).aplicarRutas();
        new AdministrarUsuario(app).aplicarRutas();

        app.get("/", cxt ->{
            cxt.redirect("/login");
        });

    }


    public static String getModoConexion(){
        return modoConexion;
    }

}