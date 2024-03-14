package org.example.services;

import org.example.clases.Registro;
import org.example.util.RolesApp;

public class RegistroServices extends GestionDb<Registro>{
    private static RegistroServices instancia;


    private RegistroServices() {
        super(Registro.class);
    }

    public static RegistroServices getInstancia(){
        if(instancia==null){
            instancia = new RegistroServices();
        }
        return instancia;
    }

}
