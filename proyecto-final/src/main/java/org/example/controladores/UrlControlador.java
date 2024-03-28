package org.example.controladores;

import io.javalin.Javalin;
import org.example.encapsulaciones.EstadisticaURL;
import org.example.encapsulaciones.ShortURL;
import org.example.servicios.URLServices;
import org.example.utils.ControladorClass;

import java.util.Date;

public class UrlControlador extends ControladorClass {
    public UrlControlador(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarRutas() {
        EstadisticaURL esta = new EstadisticaURL();
        Date date = new Date();
        URLServices.getInstancia().crearUrl(new ShortURL("ddd","www.com","com",date,esta,"foto"));

        app.get("/{codigo}", context -> {

        });
    }
}
