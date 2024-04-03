package org.example.controladores;

import io.javalin.Javalin;
import org.example.encapsulaciones.Usuario;
import org.example.servicios.UsuarioServices;
import org.example.servicios.mongo.UsuarioODM;
import org.example.utils.ControladorClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsuarioControlador extends ControladorClass {
    public UsuarioControlador(Javalin app) {
        super(app);
    }

    List<Usuario> listUsuario = new ArrayList<>();
    @Override
    public void aplicarRutas() {

        app.get("/registrarUsuarios", cxt ->{
            cxt.render("publico/html/registrarUsuarios.html");
        });

        app.get("/listarUsuarios", cxt ->{
            Usuario user =  UsuarioServices.getInstancia().getUsuarioLogueado();
            listUsuario = UsuarioODM.getInstance().buscarTodosLosUsuarios();
            Map<String,Object> model = new HashMap<>();
            model.put("listUsuarios",listUsuario);
            model.put("usuario",user);


            cxt.render("publico/html/ListarUsuarios.html", model);
        });

        app.post("/registrarUsuarios", cxt ->{

            String nombre = cxt.formParam("nombre");
            String username = cxt.formParam("username");
            String password = cxt.formParam("password");
            String isUserParam = cxt.formParam("isUser");
            boolean user = isUserParam != null && !isUserParam.isEmpty();

            if (verificarExistenciaOfUsuario(username)){
                cxt.redirect("/registro");
                return;
            }
            Usuario tpm = new Usuario(username,nombre,password,user);
            UsuarioODM.getInstance().guardarUsuario(tpm);
            UsuarioServices.getInstancia().setUsuarioLogueado(tpm);
            cxt.redirect("/");

        });


        app.get("/eliminarUsuario", ctx -> {

            String username = ctx.queryParam("username");
            //Usuario user = UsuarioODM.getInstance().buscarUsuarioByUsername(username);
            UsuarioODM.getInstance().eliminarUsuario(username);
            ctx.redirect("/");
        });

        app.post("/cambiarPermisos", cxt ->{
            String username = cxt.queryParam("username");
            Usuario user = UsuarioODM.getInstance().buscarUsuarioByUsername(username);
            if(user.isUser()){
                user.setUser(false);
            }else {
                user.setUser(true);
            }
            UsuarioODM.getInstance().actualizarPermisos(user);
            cxt.redirect("/");
        });

    }

    public boolean verificarExistenciaOfUsuario(String username){
        return UsuarioODM.getInstance().buscarUsuarioByUsername(username) != null;
    }
}
