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
}
