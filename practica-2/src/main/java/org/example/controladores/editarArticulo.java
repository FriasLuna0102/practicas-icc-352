package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.Articulo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class editarArticulo extends ControladorClass{
    public editarArticulo(Javalin app) {
        super(app);
    }

    List<Articulo> listaArticulos = Articulo.getArticulos();
    @Override
    public void aplicarRutas() {

        // Manejar la solicitud POST para eliminar un artículo
        app.post("/editarArticulo", ctx -> {
            // Obtener el ID del artículo a eliminar desde el formulario
            String idArticulo = ctx.formParam("idArticulo");
            System.out.println(idArticulo);

            // Eliminar el artículo de la lista de artículos
            Articulo articuloEditar = Articulo.obtenerArticuloPorId(idArticulo);

            Map<String, Object> model = new HashMap<>();

            //Este nombre de "articulo" sera el que la plantilla podra identificar:
            //Ejemplo: th:value="${articulo.titulo}", deben ser iguales.
            model.put("articulo", articuloEditar);
            ctx.render("publico/temp/editarArticulos.html", model);

        });
    }
}
