package org.example;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.FileRenderer;
import io.javalin.rendering.template.JavalinThymeleaf;
import  org.example.clases.Articulo;
import  org.example.clases.Comentario;
import  org.example.clases.Usuario;
import  org.example.clases.Etiqueta;
import org.thymeleaf.TemplateEngine;
import io.javalin.rendering.template.JavalinThymeleaf;
import io.javalin.rendering.JavalinRenderer;

import java.util.*;

import static org.example.clases.Usuario.getUsuarios;


public class Main {
    public static void main(String[] args) {

        List<Usuario> usuarios = getUsuarios();

        System.out.println(usuarios.getFirst().getNombre());
        //List<Usuario> usuarios = new ArrayList<>();
        List<Articulo> articulos = new ArrayList<>();
        List<Comentario> comentarios = new ArrayList<>();
        List<Etiqueta> etiquetas = new ArrayList<>();

        Usuario usuario1 = new Usuario("star","Starlin","123",true,true);
        usuarios.add(usuario1);

        Etiqueta etiqueta = new Etiqueta(1, "Etiqueta1");
        etiquetas.add(etiqueta);

        Comentario comentario = new Comentario(1, "Este es un comentario", usuario1, null); // El artículo se establecerá más adelante
        comentarios.add(comentario);

        Articulo articulo = new Articulo(1, "Título del artículo", "Cuerpo del artículo...", usuario1, new Date(), comentarios, etiquetas);
        articulos.add(articulo);

        // Establecer el artículo en el comentario
        comentario.setArticulo(articulo);



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


        // Configurar la ruta para renderizar una plantilla Thymeleaf
        app.get("/crearArticulo", ctx -> {
            Map<String, Object> model = new HashMap<>();
            // Obtener la lista de usuarios
            List<Usuario> usuarios2 = getUsuarios();
            model.put("usuarios", usuarios2);
            ctx.render("/html/crearArticulo.html", model);
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

        //Si intenta acceder a blogUsuario sin login.
        app.get("/blogUsuario", cxt -> {
            cxt.redirect("login.html");
        });


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


        // Lógica para permitir la creación de nuevos usuarios por parte de administradores
        app.post("/crearUsuario", ctx -> {
            Usuario currentUser = ctx.sessionAttribute("currentUser");
            if (currentUser != null && currentUser.isAdministrator()) {
                // El usuario actual es administrador, se le permite crear un nuevo usuario
                String username = ctx.formParam("username");
                String nombre = ctx.formParam("nombre");
                String password = ctx.formParam("password");
                boolean isAdmin = ctx.formParam("isAdmin") != null; // Check si se marcó como administrador
                boolean isAutor = ctx.formParam("isAutor") != null; // Check si se marcó como autor

                // Crear el nuevo usuario y agregarlo a la lista de usuarios
                Usuario nuevoUsuario = new Usuario(username, nombre, password, isAdmin, isAutor);
                usuarios.add(nuevoUsuario);
                System.out.println(nuevoUsuario);
                // Redirigir a la página de administración u otra página según corresponda
                ctx.redirect("/html/blogUsuario.html");
            } else {
                // El usuario actual no tiene permisos para crear un nuevo usuario, redirigir o mostrar un mensaje de error
                ctx.result("No tienes permiso para realizar esta acción.");
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