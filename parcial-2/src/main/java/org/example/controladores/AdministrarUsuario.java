package org.example.controladores;

import io.javalin.Javalin;
import io.javalin.http.UploadedFile;
import org.example.clases.Contraladora;
import org.example.clases.Usuario;
import org.example.services.RolesServices;
import org.example.services.UsuarioServices;
import org.example.util.ControladorClass;
import org.example.util.RolesApp;

import java.util.*;

import static io.javalin.apibuilder.ApiBuilder.*;

public class AdministrarUsuario extends ControladorClass {
    public AdministrarUsuario(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarRutas() {

        app.routes(() ->{

            path("/plantillaGeneral", ()->{

                get("/administrarUsuarios", cxt ->{
                    Usuario currentUser = cxt.sessionAttribute("currentUser");
                    if(currentUser != null){
                        List<Usuario> listUsuario = UsuarioServices.getInstancia().obtenerTodosLosUsuarios();
                        Map<String, Object> model = new HashMap<>();
                        model.put("listUsuario", listUsuario);
                        model.put("usuario", Contraladora.getInstance().getUsuario());

                        cxt.render("publico/html/administrarUsuarios.html",model);
                    }else {
                        cxt.redirect("/login");
                    }
                });

                get("/plantillaCrearUsuario", context -> {
                    Usuario currentUser = context.sessionAttribute("currentUser");
                    if(currentUser != null){
                        List<RolesApp> listRoles = RolesServices.getInstancia().obtenerTodosLosRoles();
                        Map<String, Object> model = new HashMap<>();
                        model.put("listRoles", listRoles);

                        context.render("publico/html/crearUsuario.html",model);
                    }else {
                        context.redirect("/login");
                    }
                });


                post("/crearUsuario", ctx -> {
                    Usuario currentUser = ctx.sessionAttribute("currentUser");
                    assert currentUser != null;
                    if (currentUser.nombreRoles().contains("admin")) {
                        // El usuario actual es administrador, se le permite crear un nuevo usuario
                        String username = ctx.formParam("username");
                        String nombre = ctx.formParam("nombre");
                        String password = ctx.formParam("password");
                        List<String> rolesSeleccionados = ctx.formParams("roles");
                        List<RolesApp> rolesDescripcion = new ArrayList<>();

                        for(String lisrRol : rolesSeleccionados){
                            RolesApp rol = RolesServices.getInstancia().findByCodigo(lisrRol);
                            rolesDescripcion.add(rol);
                        }

                        // Crear el nuevo usuario y agregarlo a la lista de usuarios
                        Usuario nuevoUsuario = null;

                        nuevoUsuario = new Usuario(username, nombre, password, rolesDescripcion);

                        UsuarioServices.getInstancia().crear(nuevoUsuario);

                        Contraladora.getInstance().addUsuario(nuevoUsuario);
                        // Redirigir a la página de administración u otra página según corresponda
                        ctx.redirect("/plantillaGeneral/administrarUsuarios");
                    } else {
                        // El usuario actual no tiene permisos para crear un nuevo usuario, redirigir o mostrar un mensaje de error
                        ctx.result("No tienes permiso para realizar esta accion.");
                    }
                });




                get("/plantillaUsuarioModificar", context -> {
                    Usuario currentUser = context.sessionAttribute("currentUser");
                    assert currentUser != null;
                    if(currentUser == null){
                        context.redirect("/login");
                    }
                    String username = context.queryParam("username");
                    Usuario usuario = UsuarioServices.getInstancia().findByUsername(username);
                    List<RolesApp> rolesUsuario = RolesServices.getInstancia().obtenerTodosLosRoles();

                    Map<String, Object> model = new HashMap<>();

                    model.put("usuario", usuario);
                    model.put("listRoles",rolesUsuario);
                    context.render("publico/html/modificarUsuario.html",model);
                });


                post("/modificarUsuario", context -> {
                    long userId = Long.parseLong(context.formParam("idUsername")); // Recupera el ID del usuario existente
                    Usuario usuarioExistente = UsuarioServices.getInstancia().findById(userId); // Busca el usuario por su ID

                    String username = context.formParam("username");
                    String nombre = context.formParam("nombre");
                    String password = context.formParam("password");
                    List<String> rolesSeleccionados = context.formParams("roles");
                    List<RolesApp> rolesDescripcion = new ArrayList<>();

                    System.out.println("Este es el user:" +username);


                    for(String lisrRol : rolesSeleccionados){
                        RolesApp rol = RolesServices.getInstancia().findByCodigo(lisrRol);
                        rolesDescripcion.add(rol);
                    }
                   // Actualiza los campos del usuario existente con los nuevos valores
                    usuarioExistente.setUsuario(username);
                    usuarioExistente.setNombre(nombre);
                    usuarioExistente.setPassword(password);
                    usuarioExistente.setListaRoles(rolesDescripcion);

                    // Guarda los cambios en la base de datos
                    UsuarioServices.getInstancia().editar(usuarioExistente);

                    context.redirect("/plantillaGeneral/administrarUsuarios");
                });


                get("/eliminarUsuario", context -> {
                    String username = context.queryParam("username");
                    Usuario usuario = UsuarioServices.getInstancia().findByUsername(username);


                    UsuarioServices.getInstancia().eliminar(usuario.getId());
                    context.redirect("/plantillaGeneral/administrarUsuarios");
                });

            });

        });


    }
}
