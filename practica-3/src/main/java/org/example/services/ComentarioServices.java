package org.example.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.example.clases.Articulo;
import org.example.clases.Comentario;
import org.example.clases.Usuario;

import java.util.List;

public class ComentarioServices extends GestionDb<Comentario>{

    private static ComentarioServices instancia;


    private ComentarioServices() {
        super(Comentario.class);
    }

    public static ComentarioServices getInstancia(){
        if(instancia==null){
            instancia = new ComentarioServices();
        }
        return instancia;
    }

    public List<Comentario> findAllByNombre(String idComentario){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select c from Comentario c where c.id like :idComentario");
        query.setParameter("idComentario", idComentario+"%");
        List<Comentario> lista = query.getResultList();
        return lista;
    }

    public List<Comentario> obtenerTodosLosComentarios() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Comentario c", Comentario.class).getResultList();
        } finally {
            em.close();
        }
    }


}
