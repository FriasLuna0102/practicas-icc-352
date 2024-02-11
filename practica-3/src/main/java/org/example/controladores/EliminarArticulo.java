package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.Articulo;
import org.example.clases.Blog;
import org.example.util.ControladorClass;

import java.util.List;

public class EliminarArticulo extends ControladorClass {

    public EliminarArticulo(Javalin app) {
        super(app);
    }

    List<Articulo> listaArticulos = Blog.getInstance().getArticuloList();
    @Override
    public void aplicarRutas() {

        // Manejar la solicitud POST para eliminar un artículo
        app.post("/eliminarArticulo", ctx -> {
            // Obtener el ID del artículo a eliminar desde el formulario
            String idTituloo = ctx.formParam("idArticulo");

            System.out.println(idTituloo);
            // Eliminar el artículo de la lista de artículos
            boolean eliminado = Articulo.eliminarArti(listaArticulos,idTituloo);

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
