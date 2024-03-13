package org.example.services;


import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import org.example.util.RolesApp;

import java.util.List;

public class RolesServices extends GestionDb<RolesApp>{

    private static RolesServices instancia;


    private RolesServices() {
        super(RolesApp.class);
    }

    public static RolesServices getInstancia(){
        if(instancia==null){
            instancia = new RolesServices();
        }
        return instancia;
    }


}
