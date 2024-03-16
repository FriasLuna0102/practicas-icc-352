package org.example.clases;

import org.example.services.UsuarioServices;

import java.util.List;

public class Encuesta {
    private static Encuesta instancia;

    private Usuario usuario;


    public static Encuesta getInstance(){
        if (instancia == null){
            instancia = new Encuesta();
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
        List<Usuario> usuarios = UsuarioServices.getInstancia().findAllByUsername(usuario.getUsername());

        for (Usuario user: usuarios){
            if(user.getUsername().equals(usuario.getUsername()) || user.getNombre().equals(usuario.getNombre())){
                return;
            }
        }
        UsuarioServices.getInstancia().crear(new Usuario(usuario.getUsername(),usuario.getNombre(),usuario.getPassword()
                ,usuario.getListaRoles()));
        //usuarioList.add(usuario);
    }




}
