package org.example.controladores;

import io.javalin.Javalin;
import org.example.encapsulaciones.Usuario;
import org.example.servicios.UsuarioServices;
import org.example.utils.ControladorClass;

public class UsuarioControlador extends ControladorClass {
    public UsuarioControlador(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarRutas() {

        UsuarioServices.getInstancia().crearEstudiante(new Usuario("star","starlin","123",false));
    }
}
