package org.example.controladores;

import io.javalin.Javalin;
import io.javalin.http.UploadedFile;
import org.example.clases.Blog;
import org.example.clases.Foto;
import org.example.clases.Usuario;
import org.example.services.ArticuloServices;
import org.example.services.FotoServices;
import org.example.services.UsuarioServices;
import org.example.util.ControladorClass;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;

public class mostrarUsuarios extends ControladorClass {
    public mostrarUsuarios(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarRutas() {

        app.routes(()->{

            path("/blogUsuario", ()->{

                get("/mostrarUsuario", cxt ->{
                    if(cxt.sessionAttribute("currentUser") == null){
                        cxt.redirect("/login");
                    }else{
                        List<Usuario> listUsuario = UsuarioServices.getInstancia().obtenerTodosLosUsuarios();
                        Map<String, Object> model = new HashMap<>();
                        model.put("listUsuario",listUsuario);
                        model.put("usuario", Blog.getInstance().getUsuario());
                        cxt.render("publico/html/listarUsuarios.html",model);
                    }
                });

                get("/eliminarUsuario", context -> {
                    String username = context.queryParam("username");
                    Usuario usuario = UsuarioServices.getInstancia().findByUsername(username);

                    ArticuloServices.getInstancia().eliminarArticuloByAutor(username);
                    UsuarioServices.getInstancia().eliminar(usuario.getId());
                    context.redirect("/blogUsuario/mostrarUsuario");
                });

                get("/plantillaUsuarioModificar", context -> {
                    String username = context.queryParam("username");
                    Usuario usuario = UsuarioServices.getInstancia().findByUsername(username);

                    Map<String, Object> model = new HashMap<>();

                    model.put("usuario", usuario);

                    context.render("publico/temp/editarUsuario.html",model);
                });


                post("/modificarUsuario", context -> {
                    long userId = Long.parseLong(context.formParam("idUsername")); // Recupera el ID del usuario existente
                    Usuario usuarioExistente = UsuarioServices.getInstancia().findById(userId); // Busca el usuario por su ID

                    System.out.println(usuarioExistente.getId());
                    // Obtiene los nuevos valores del formulario
                    String username = context.formParam("username");
                    String nombre = context.formParam("nombre");
                    String password = context.formParam("password");
                    boolean isAdmin = context.formParam("isAdmin") != null; // Verifica si se marcó como administrador
                    boolean isAutor = context.formParam("isAutor") != null; // Verifica si se marcó como autor
                    UploadedFile file = context.uploadedFile("foto");

//                    // Si se ha subido una nueva foto, la procesa y la guarda
                    Foto foto = null;
                    if (file != null) {
                        try {
                            byte[] bytes = file.content().readAllBytes();
                            String encodedString = Base64.getEncoder().encodeToString(bytes);
                            foto = new Foto(file.filename(), file.contentType(), encodedString);
                            FotoServices.getInstancia().crear(foto);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

//                    // Actualiza los campos del usuario existente con los nuevos valores
                    usuarioExistente.setUsername(username);
                    usuarioExistente.setNombre(nombre);
                    usuarioExistente.setPassword(password);
                    usuarioExistente.setAdministrator(isAdmin);
                    usuarioExistente.setAutor(isAutor);
                    if (foto != null) {
                        usuarioExistente.setFoto(foto);
                    }

                    // Guarda los cambios en la base de datos
                    UsuarioServices.getInstancia().editar(usuarioExistente);

                    context.redirect("/blogUsuario/mostrarUsuario");
                });


            });

        });

    }
}
