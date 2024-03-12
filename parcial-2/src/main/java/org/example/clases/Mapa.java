package org.example.clases;

import java.util.List;
import java.util.Objects;

public class Mapa {
    private List<Registro> listRegistro;
    private Objects apiMapa;

    public Mapa(List<Registro> listRegistro, Objects apiMapa) {
        this.listRegistro = listRegistro;
        this.apiMapa = apiMapa;
    }

    public List<Registro> getListRegistro() {
        return listRegistro;
    }

    public void setListRegistro(List<Registro> listRegistro) {
        this.listRegistro = listRegistro;
    }

    public Objects getApiMapa() {
        return apiMapa;
    }

    public void setApiMapa(Objects apiMapa) {
        this.apiMapa = apiMapa;
    }
}
