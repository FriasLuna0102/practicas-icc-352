package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.Articulo;

import java.util.List;

public class buscarArticulos extends ControladorClass {
    public buscarArticulos(Javalin app) {
        super(app);
    }

    static List<Articulo> listArticulos = Articulo.getArticulos();

    @Override
    public void aplicarRutas() {

        /*
        app.post("/buscar", cxt ->{
            String busquedad = cxt.formParam("busquedaad");

            for(Articulo articulo : listArticulos){
                if(articulo.getTitulo().equals(busquedad)){
                    return articulo.getTitulo();
                }
            }
            return articulo.getTitulo();
        });

    }*/

    }


}