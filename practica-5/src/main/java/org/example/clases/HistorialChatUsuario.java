package org.example.clases;

import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class HistorialChatUsuario {
    private String session;
    private String nombre;
    private String uri;
    private String adminSession;
    private List<String> mensajes;
    // La cola de mensajes
    private Queue<String> colaDeMensajes = new LinkedList<>();

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

    public String getAdminSession() {
        return adminSession;
    }

    public void setAdminSession(String adminSession) {
        this.adminSession = adminSession;
    }

    // Método para agregar un mensaje a la cola
    public void agregarMensajeACola(String mensaje) {
        colaDeMensajes.add(mensaje);
    }

    // Método para obtener y eliminar el próximo mensaje de la cola
    public String obtenerSiguienteMensaje() {
        return colaDeMensajes.poll();
    }

    // Método para comprobar si hay mensajes en la cola
    public boolean hayMensajesEnCola() {
        return !colaDeMensajes.isEmpty();
    }
}
