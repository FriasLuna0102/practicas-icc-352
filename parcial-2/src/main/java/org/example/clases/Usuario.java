package org.example.clases;

import org.example.util.RolesApp;

import java.util.Set;

public class Usuario {

    private String usuario;
    private String nombre;
    private String password;
    //lo estaremos utilizando para los roles.
    private Set<RolesApp> listaRoles;

    public Usuario(String usuario, String nombre, String password, Set<RolesApp> listaRoles) {
        this.usuario = usuario;
        this.nombre = nombre;
        this.password = password;
        this.listaRoles = listaRoles;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
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

    public Set<RolesApp> getListaRoles() {
        return listaRoles;
    }

    public void setListaRoles(Set<RolesApp> listaRoles) {
        this.listaRoles = listaRoles;
    }
}
