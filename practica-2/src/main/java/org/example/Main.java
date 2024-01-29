package org.example;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import  org.example.clases.Articulo;
import  org.example.clases.Comentario;
import  org.example.clases.Usuario;
import  org.example.clases.Etiqueta;

import java.util.*;


public class Main {
    public static void main(String[] args) {

        List<Usuario> usuarios = new ArrayList<>();
        List<Articulo> articulos = new ArrayList<>();
        List<Comentario> comentarios = new ArrayList<>();
        List<Etiqueta> etiquetas = new ArrayList<>();



        Usuario usuario1 = new Usuario("star","Starlin","123",true,true);
        Etiqueta etiqueta = new Etiqueta(1, "Etiqueta1");
        Articulo articulo = new Articulo(1, "Título del artículo", "Cuerpo del artículo...", usuario1, new Date(),comentarios,etiquetas);
        Comentario comentario = new Comentario(1, "Este es un comentario", usuario1, articulo);


        usuarios.add(usuario1);
        articulos.add(articulo);
        comentarios.add(comentario);
        etiquetas.add(etiqueta);

        System.out.println(usuarios.getFirst().getNombre());

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

        app.start(getHerokuAssignedPort());


        app.get("/blog", cxt -> {
            cxt.redirect("blog.html");
        });


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
                    cxt.redirect("/html/blogUsuario.html");
                    return;
                }
            }

            // Si ninguna credencial coincide, redirigir de nuevo a la página de inicio de sesión
            cxt.redirect("login.html");
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