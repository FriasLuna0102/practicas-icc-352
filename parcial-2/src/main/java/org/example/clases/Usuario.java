package org.example.clases;

import jakarta.persistence.*;
import org.example.util.RolesApp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String nombre;
    private String password;
    //lo estaremos utilizando para los roles.
//    @ManyToMany
//    private Set<RolesApp> listaRoles;
    @ManyToMany (fetch = FetchType.EAGER)
    private List<RolesApp> listaRoles;

    public Usuario(String usuario, String nombre, String password, List<RolesApp> listaRoles) {
        this.username = usuario;
        this.nombre = nombre;
        this.password = password;
        this.listaRoles = listaRoles;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Usuario() {

    }

    public String getUsuario() {
        return username;
    }

    public void setUsuario(String usuario) {
        this.username = usuario;
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

    public List<RolesApp> getListaRoles() {
        return listaRoles;
    }

    public void setListaRoles(List<RolesApp> listaRoles) {
        this.listaRoles = listaRoles;
    }

    public List<String> nombreRoles() {
        List<String> nombresRoles = new ArrayList<>();
        for(RolesApp rol : listaRoles){
            nombresRoles.add(rol.getDescripcion());
        }
        return nombresRoles;
    }
}
