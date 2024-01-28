package org.example.clases;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Usuario {

    String username;
    String nombre;
    String password;
    boolean administrator;

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
//lo estaremos utilizando para los roles.


    public List<Usuario> getUsuarios() {
        //listando los estudiantes..
        List<Usuario> listaUsuarios = new ArrayList<>();
        listaUsuarios.add(new Usuario("dd", "Carlos Camacho", "123",true, true));
        listaUsuarios.add(new Usuario("dd2", "Otro Estudiante", "ISC",true,true));
        listaUsuarios.add(new Usuario("dd3", "Otro otro", "ISC",true,true));
        return listaUsuarios;
    }
}