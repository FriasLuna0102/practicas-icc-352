package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.Encuesta;
import org.example.clases.Registro;
import org.example.clases.Usuario;
import org.example.services.RegistroServices;
import org.example.services.UsuarioServices;
import org.example.util.ControladorClass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class AdministrarRegistros extends ControladorClass {
    public AdministrarRegistros(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarRutas() {

        app.routes(() ->{

           path("/plantillaGeneral", ()->{

               get("/administrarRegistros",cxt ->{
                   Usuario currentUser = cxt.sessionAttribute("currentUser");
                   if(currentUser != null && currentUser.nombreRoles().contains("admin")){
                       List<Registro> listRegistros = RegistroServices.getInstancia().obtenerTodosLosRegistros();
                       Map<String, Object> model = new HashMap<>();
                       model.put("listRegistro", listRegistros);

                       cxt.render("publico/html/administrarRegistros.html",model);
                   }else {
                       cxt.redirect("/login");
                   }
               });
           });
        });
    }
}
