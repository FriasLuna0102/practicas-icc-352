package org.example.utils;

public enum TablasMongo {
    USUARIOS("usuarios");

    private String valor;

    TablasMongo(String valor){
        this.valor =  valor;
    }

    public String getValor() {
        return valor;
    }
}
