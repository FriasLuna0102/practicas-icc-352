package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.Articulo;
import org.example.clases.Etiqueta;
import org.example.clases.Usuario;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


public class editarArticulo extends ControladorClass{
    public editarArticulo(Javalin app) {
        super(app);
    }

    List<Articulo> listArticulos = Articulo.getArticulos();
    List<Usuario> listUsuarios = Usuario.getUsuarios();
    @Override
    public void aplicarRutas() {

        // Manejar la solicitud POST para eliminar un artículo
        app.post("/editarArticulo", ctx -> {
            // Obtener el ID del artículo a eliminar desde el formulario
            String idArticulo = ctx.formParam("idArticulo");


            // Eliminar el artículo de la lista de artículos
            Articulo articuloEditar = Articulo.obtenerArticuloPorId(idArticulo);


            Map<String, Object> model = new HashMap<>();

            List<String> etiquetas = articuloEditar.getListaEtiquetas().stream()
                    .map(Etiqueta::getEtiqueta)
                    .collect(Collectors.toList());


            String etiquetasStr = String.join(",", etiquetas);

            //Este nombre de "articulo" sera el que la plantilla podra identificar:
            //Ejemplo: th:value="${articulo.titulo}", deben ser iguales.
            model.put("articulo", articuloEditar);
            model.put("etiquetasStr", etiquetasStr);
            model.put("usuarios",listUsuarios);
            ctx.render("publico/temp/editarArticulos.html", model);

        });


        app.post("/actualizarArticulo", cxt ->{

            String id = cxt.formParam("idArticulo");
            String titulo = cxt.formParam("titulo");
            String cuerpo = cxt.formParam("cuerpo");
            String autor = cxt.formParam("autor");

            Usuario user = Usuario.buscarUsuario(autor);

            Usuario autorNew = new Usuario(user.getUsername(),user.getNombre(),user.getPassword()
            ,user.isAdministrator(),user.isAutor());


            String fecha = cxt.formParam("fecha");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Date fechaDate = sdf.parse(fecha);

            String etiqueta = cxt.formParam("etiquetas");
            List<Etiqueta> lisEtiquetas = Articulo.devolverEtiqueta(etiqueta);

            Articulo actiEditado = Articulo.obtenerArticuloPorId(id);
            actiEditado.setTitulo(titulo);
            actiEditado.setCuerpo(cuerpo);
            actiEditado.setAutor(autorNew);
            actiEditado.setFecha(fechaDate);
            actiEditado.setListaEtiquetas(lisEtiquetas);

            boolean eliminado = Articulo.eliminarArtiPorId(listArticulos,id);

            listArticulos.add(0,actiEditado);

            if(eliminado){
                cxt.redirect("/blogUsuario");
            }else {
                cxt.result("No se pudo editar");
            }

        });
    }
}
