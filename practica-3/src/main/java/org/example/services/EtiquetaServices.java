package org.example.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.example.clases.Articulo;
import org.example.clases.Comentario;
import org.example.clases.Etiqueta;
import org.example.clases.Usuario;

import java.util.List;

public class EtiquetaServices extends GestionDb<Etiqueta>{

    private static EtiquetaServices instancia;


    private EtiquetaServices() {
        super(Etiqueta.class);
    }

    public static EtiquetaServices getInstancia(){
        if(instancia==null){
            instancia = new EtiquetaServices();
        }
        return instancia;
    }

    public List<Etiqueta> findAllByNombre(String etiqueta){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select e from Etiqueta e where e.etiqueta like :etiqueta");
        query.setParameter("etiqueta", etiqueta+"%");
        List<Etiqueta> lista = query.getResultList();
        return lista;
    }



    public List<Etiqueta> obtenerTodasLasEtiquetas() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT e FROM Etiqueta e", Etiqueta.class).getResultList();
        } finally {
            em.close();
        }
    }

    /*
    public void pruebaActualizacion(){
        EntityManager em = getEntityManager();
        Usuario usuario1 = new Usuario("star","Starlin","123",true,false);
        em.getTransaction().begin();
        em.persist(usuario1); //creando el registro.
        em.flush();
        em.getTransaction().commit(); //persistiendo el registro.
        em.getTransaction().begin();
        usuario1.setNombre("Otro Nombre");
        em.flush();
        em.getTransaction().commit();
        em.getTransaction().begin();
        usuario1.setNombre("Nuevamente otro nombre...");
        em.flush();
        em.getTransaction().commit();
        em.close(); //todos los objetos abiertos y manejados fueron cerrados.
        //
        usuario1.setNombre("Cambiando el objeto..."); //en memoria, no en la base datos.
        em = getEntityManager();
        em.getTransaction().begin();
        em.merge(usuario1);
        em.getTransaction().commit();
        em.close();
    }
    */

}
