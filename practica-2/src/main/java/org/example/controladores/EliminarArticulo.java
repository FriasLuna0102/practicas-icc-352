package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.Articulo;

import java.util.List;
import java.util.UUID;

public class EliminarArticulo extends ControladorClass{

    public EliminarArticulo(Javalin app) {
        super(app);
    }

    List<Articulo> listaArticulos = Articulo.getArticulos();
    @Override
    public void aplicarRutas() {

        // Manejar la solicitud POST para eliminar un artículo
        app.post("/eliminarArticulo", ctx -> {
            // Obtener el ID del artículo a eliminar desde el formulario
            String titulo = ctx.formParam("idTitulo");

            // Eliminar el artículo de la lista de artículos
            boolean eliminado = Articulo.eliminarArti(listaArticulos,titulo);

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
