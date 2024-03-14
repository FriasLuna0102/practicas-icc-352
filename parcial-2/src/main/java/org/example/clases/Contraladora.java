package org.example.clases;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.example.services.UsuarioServices;

import java.util.List;

public class Contraladora {
    private static Contraladora instancia;

    private Usuario usuario;


    public static Contraladora getInstance(){
        if (instancia == null){
            instancia = new Contraladora();
        }
        return instancia;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void addUsuario(Usuario usuario){

        // Buscar el usuario en la base de datos utilizando Hibernate
        List<Usuario> usuarios = UsuarioServices.getInstancia().findAllByUsername(usuario.getUsuario());

        for (Usuario user: usuarios){
            if(user.getUsuario().equals(usuario.getUsuario()) || user.getNombre().equals(usuario.getNombre())){
                return;
            }
        }
        UsuarioServices.getInstancia().crear(new Usuario(usuario.getUsuario(),usuario.getNombre(),usuario.getPassword()
                ,usuario.getListaRoles()));
        //usuarioList.add(usuario);
    }




}
