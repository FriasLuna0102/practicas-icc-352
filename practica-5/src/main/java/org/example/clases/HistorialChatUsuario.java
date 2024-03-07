package org.example.clases;

import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;
import java.util.List;

public class HistorialChatUsuario {
    private String session;
    private String nombre;
    private String uri;
    private List<String> mensajes;

    public HistorialChatUsuario(String session, String nombre, String uri) {
        this.session = session;
        this.nombre = nombre;
        this.uri = uri;
        mensajes = new ArrayList<>();
    }

    public String getSession() {
        return session;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUri() {
        return uri;
    }

    public List<String> getMensajes() {
        return mensajes;
    }

    public void addMensaje(String mensaje) {
        this.mensajes.add(mensaje);
    }
}
