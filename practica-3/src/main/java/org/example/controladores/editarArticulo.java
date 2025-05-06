package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.Articulo;
import org.example.clases.Blog;
import org.example.clases.Etiqueta;
import org.example.clases.Usuario;
import org.example.services.ArticuloServices;
import org.example.services.UsuarioServices;
import org.example.util.ControladorClass;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


public class editarArticulo extends ControladorClass {
    public editarArticulo(Javalin app) {
        super(app);
    }

    List<Articulo> listArticulos = Blog.getInstance().getArticuloList();

    @Override
    public void aplicarRutas() {

        app.post("/editarArticulo", ctx -> {
            long idArticulo = Long.parseLong(ctx.formParam("idArticulo"));
            Articulo articuloEditar = Blog.getInstance().obtenerArticuloPorId(idArticulo);

            Map<String, Object> model = new HashMap<>();

            List<String> etiquetas = articuloEditar.getListaEtiquetas().stream()
                    .map(Etiqueta::getEtiqueta)
                    .collect(Collectors.toList());

            String etiquetasStr = String.join(",", etiquetas);

            model.put("articulo", articuloEditar);
            model.put("etiquetasStr", etiquetasStr);

            List<Usuario> listUsuarios = UsuarioServices.getInstancia().obtenerTodosLosUsuarios();
            model.put("usuarios", listUsuarios);

            ctx.render("publico/temp/editarArticulos.html", model);
        });

        app.post("/actualizarArticulo", cxt -> {
            long id = Long.parseLong(cxt.formParam("idArticulo"));
            String titulo = cxt.formParam("titulo");
            String cuerpo = cxt.formParam("cuerpo");
            String autor = cxt.formParam("autor");

            Usuario autorr = UsuarioServices.getInstancia().findByUsername(autor);

            String fecha = cxt.formParam("fecha");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaDate = sdf.parse(fecha);

            String etiqueta = cxt.formParam("etiquetas");
            List<Etiqueta> lisEtiquetas = Blog.getInstance().stringToEtiqueta(etiqueta);

            Articulo actiEditado = Blog.getInstance().obtenerArticuloPorId(id);
            actiEditado.setTitulo(titulo);
            actiEditado.setCuerpo(cuerpo);
            actiEditado.setAutor(autorr);
            actiEditado.setFecha(fechaDate);
            actiEditado.setListaEtiquetas(lisEtiquetas);

            ArticuloServices.getInstancia().actualizar(actiEditado);

            boolean eliminado = Blog.getInstance().eliminarArticuloById(id);

            listArticulos.add(0, actiEditado);  // ✅ CORREGIDO: addFirst → add(0, ...)

            if (eliminado) {
                cxt.redirect("/blogUsuario");
            } else {
                cxt.result("No se pudo editar");
            }
        });
    }
}
