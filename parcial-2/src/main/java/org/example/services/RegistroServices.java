package org.example.services;

import jakarta.persistence.EntityManager;
import org.example.clases.Registro;
import org.example.util.RolesApp;

import java.util.List;

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

    public List<Registro> obtenerTodosLosRegistros() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT r FROM Registro r", Registro.class).getResultList();
        } finally {
            em.close();
        }
    }
}
