package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.Articulo;
import org.example.clases.Blog;
import org.example.services.ArticuloServices;
import org.example.util.ControladorClass;

import java.util.List;

public class EliminarArticulo extends ControladorClass {

    public EliminarArticulo(Javalin app) {
        super(app);
    }

    List<Articulo> listaArticulos =ArticuloServices.getInstancia().obtenerTodosLosArticulos();
    @Override
    public void aplicarRutas() {

        // Manejar la solicitud POST para eliminar un artículo
        app.post("/eliminarArticulo", ctx -> {
            // Obtener el ID del artículo a eliminar desde el formulario
            long idArticulo = Long.parseLong(ctx.formParam("idArticulo"));

            System.out.println(idArticulo);
            // Eliminar el artículo de la lista de artículos
            boolean eliminado = ArticuloServices.getInstancia().eliminarArticuloConComentarios(idArticulo);

            if (eliminado) {
                // Redirigir a la página del blog del usuario
                ctx.redirect("/blogUsuario");
            } else {
                // Manejar el caso en que el artículo no se pudo eliminar
                ctx.result("Error al eliminar el articulo.");
            }
        });

    }
}
