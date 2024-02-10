package org.example.clases;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Usuario {

    private String username;
    private String nombre;
    private String password;
    private boolean administrator;

    boolean autor;

    public Usuario(String username, String nombre, String password, boolean administrator, boolean autor) {
        this.username = username;
        this.nombre = nombre;
        this.password = password;
        this.administrator = administrator;
        this.autor = autor;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdministrator() {
        return administrator;
    }

    public void setAdministrator(boolean administrator) {
        this.administrator = administrator;
    }

    public boolean isAutor() {
        return autor;
    }

    public void setAutor(boolean autor) {
        this.autor = autor;
    }



    static List<Usuario> listaUsuarios = new ArrayList<>();

    public static List<Usuario> getUsuarios() {
        return listaUsuarios;
    }


}