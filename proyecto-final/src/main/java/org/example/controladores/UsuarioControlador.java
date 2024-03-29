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
            System.out.println(user.getNombre());
            listUsuario = UsuarioODM.getInstance().buscarTodosLosUsuarios();
            Map<String,Object> model = new HashMap<>();
            model.put("listUsuarios",listUsuario);
            model.put("usuario",user);


            cxt.render("publico/html/ListarUsuarios.html", model);
        });

        app.post("/registrarUsuarios", cxt ->{

            String username = cxt.formParam("username");
            String nombre = cxt.formParam("nombre");
            String password = cxt.formParam("password");
            String isUserParam = cxt.formParam("isUser");
            boolean user = isUserParam != null && !isUserParam.isEmpty();
            System.out.println(user);

            Usuario tpm = new Usuario(username,nombre,password,user);

            UsuarioODM.getInstance().guardarUsuario(tpm);
            //UsuarioServices.getInstancia().crearEstudiante(tpm);

        });

    }
}
