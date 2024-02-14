package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.Blog;
import org.example.clases.Foto;
import org.example.clases.Usuario;
import org.example.util.ControladorClass;

import java.io.IOException;
import java.util.Base64;

public class CrearUsuario extends ControladorClass {
    public CrearUsuario(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarRutas() {


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

                //Procesando foto
                ctx.uploadedFiles("foto").forEach(uploadedFile -> {
                    try {
                        byte[] bytes = uploadedFile.content().readAllBytes();
                        String encodedString = Base64.getEncoder().encodeToString(bytes);
                        Foto foto = new Foto(uploadedFile.filename(), uploadedFile.contentType(), encodedString);
                        fotoServices.crear(foto);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                        // Crear el nuevo usuario y agregarlo a la lista de usuarios
                Usuario nuevoUsuario = new Usuario(username, nombre, password, isAdmin, isAutor);
                Blog.getInstance().addUsuario(nuevoUsuario);
                // Redirigir a la página de administración u otra página según corresponda
                ctx.redirect("/blogUsuario");
            } else {
                // El usuario actual no tiene permisos para crear un nuevo usuario, redirigir o mostrar un mensaje de error
                ctx.result("No tienes permiso para realizar esta accion.");
            }
        });
    }
}
