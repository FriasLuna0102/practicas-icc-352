package org.example.controladores;

import io.javalin.Javalin;
import org.example.clases.Articulo;
import org.example.clases.Etiqueta;

import java.text.SimpleDateFormat;
import java.util.*;


public class editarArticulo extends ControladorClass{
    public editarArticulo(Javalin app) {
        super(app);
    }

    List<Articulo> listArticulos = Articulo.getArticulos();
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


        app.post("/actualizarArticulo", cxt ->{

            String id = cxt.formParam("idArticulo");
            String titulo = cxt.formParam("titulo");
            String cuerpo = cxt.formParam("cuerpo");

            String fecha = cxt.formParam("fecha");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Date fechaDate = sdf.parse(fecha);

            String etiqueta = cxt.formParam("etiquetas");
            List<Etiqueta> lisEtiquetas = Articulo.devolverEtiqueta(etiqueta);

            Articulo actiEditado = Articulo.obtenerArticuloPorId(id);
            actiEditado.setTitulo(titulo);
            actiEditado.setCuerpo(cuerpo);
            actiEditado.setFecha(fechaDate);
            actiEditado.setListaEtiquetas(lisEtiquetas);

            boolean eliminado = Articulo.eliminarArtiPorId(listArticulos,id);

            listArticulos.add(actiEditado);

            if(eliminado){
                System.out.println(Articulo.obtenerArticuloPorId(id).getTitulo());
                System.out.println(id);
                System.out.println(actiEditado.getTitulo());
                System.out.println(actiEditado.getCuerpo());
                System.out.println(fechaDate);
                System.out.println(etiqueta);
                // Map<String, Object> model = new HashMap<>();
                //model.put("listArticulos", listArticulos);
                cxt.redirect("/blogUsuario");
            }else {
                cxt.result("No se pudo editar");
            }




        });
    }
}
