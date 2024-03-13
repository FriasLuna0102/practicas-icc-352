package org.example.clases;

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

}
