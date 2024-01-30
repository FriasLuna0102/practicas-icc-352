package org.example;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.FileRenderer;
import io.javalin.rendering.template.JavalinThymeleaf;
import  org.example.clases.Articulo;
import  org.example.clases.Comentario;
import  org.example.clases.Usuario;
import  org.example.clases.Etiqueta;
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



        //System.out.println(usuarios.getFirst().getNombre());
        //List<Usuario> usuarios = new ArrayList<>();
        List<Articulo> articulos = new ArrayList<>();
        List<Comentario> comentarios = new ArrayList<>();
        List<Etiqueta> etiquetas = new ArrayList<>();


        //List<Usuario> usuarios = getUsuarios(usuario1);

        /*
        Etiqueta etiqueta = new Etiqueta(1, "Etiqueta1");
        etiquetas.add(etiqueta);

        Comentario comentario = new Comentario(1, "Este es un comentario", usuario1, null); // El artículo se establecerá más adelante
        comentarios.add(comentario);

        Articulo articulo = new Articulo(1, "Título del artículo", "Cuerpo del artículo...", usuario1, new Date(), comentarios, etiquetas);
        articulos.add(articulo);

        // Establecer el artículo en el comentario
        comentario.setArticulo(articulo);

*/

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













        // Configurar el manejo de sesiones
        /*app.before("/login",ctx -> {
            // Verificar si hay una sesión activa para cada solicitud
            if (!ctx.path().startsWith("/login") && !ctx.path().startsWith("/public")) {
                if (ctx.sessionAttribute("currentUser") == null) {
                    // Redirigir al usuario a la página de inicio de sesión si no hay una sesión activa
                    ctx.redirect("/login");
                }else{
                    System.out.println(Optional.ofNullable(ctx.sessionAttribute("currentUser")));
                }
            }
        });*/



        //Si intenta acceder a blogUsuario sin login.
       /* app.get("/blogUsuario", cxt -> {
          //  if()
            cxt.redirect("/login");
        });
*/
        //Creaar metodo para validar login, ese metodo debe agregarse en el metodo get /login y en el metodo /blogUsuario.


        app.get("/blog", cxt -> {
            cxt.redirect("blog.html");
        });



        /*
        app.post("/login", cxt -> {
            if (usuarios.isEmpty()) {
                cxt.redirect("login.html");
                return;
            }

            String usuarioLogin = cxt.formParam("username");
            String passwordLogin = cxt.formParam("password");

            for (Usuario usuario : usuarios) {
                if (usuario.getUsername().equals(usuarioLogin) && usuario.getPassword().equals(passwordLogin)) {
                    cxt.sessionAttribute("currentUser", usuario);
                    System.out.println("Nombre de usuario establecido en la sesión: " + usuario.getNombre());
                    cxt.redirect("/blogUsuario");
                    return;
                }
            }

            // Si ninguna credencial coincide, redirigir de nuevo a la página de inicio de sesión
            cxt.redirect("/login");
        });
*/




    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 7000; //Retorna el puerto por defecto en caso de no estar en Heroku.
    }



}