package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.Usuario;

import static org.example.clases.Usuario.setUsuario;

public class CrearUsuario extends ControladorClass{
    public CrearUsuario(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarRutas() {


        //Para evitar que despues de hacer logout no pueda acceder a crearUsuario.
        app.get("/crearUsuario",cxt ->{
            if(cxt.sessionAttribute("currentUser") == null){
                cxt.redirect("/login");
            }else{
                cxt.redirect("/html/crearUsuario.html");
            }
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
                setUsuario(nuevoUsuario);
                // Redirigir a la página de administración u otra página según corresponda
                ctx.redirect("/blogUsuario");
            } else {
                // El usuario actual no tiene permisos para crear un nuevo usuario, redirigir o mostrar un mensaje de error
                ctx.result("No tienes permiso para realizar esta accion.");
            }
        });
    }
}
