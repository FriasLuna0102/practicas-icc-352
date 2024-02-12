package org.example.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.example.clases.Articulo;
import org.example.clases.Comentario;
import org.example.clases.Usuario;

import java.util.List;

public class ArticuloServices extends GestionDb<Articulo>{

    private static ArticuloServices instancia;


    private ArticuloServices() {
        super(Articulo.class);
    }

    public static ArticuloServices getInstancia(){
        if(instancia==null){
            instancia = new ArticuloServices();
        }
        return instancia;
    }

    public List<Articulo> findAllByNombre(String idArticulo){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select a from Articulo a where a.id like :idArticulo");
        query.setParameter("idArticulo", idArticulo+"%");
        List<Articulo> lista = query.getResultList();
        return lista;
    }



    public Articulo actualizar(Articulo articulo) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Articulo articuloActualizado = em.merge(articulo);
            em.getTransaction().commit();
            return articuloActualizado;
        } finally {
            em.close();
        }
    }


}
