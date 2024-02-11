package org.example.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.example.clases.Usuario;

import java.awt.dnd.DragGestureEvent;
import java.util.List;

public class UsuarioServices extends GestionDb<Usuario>{

        private static UsuarioServices instancia;


    private UsuarioServices() {
        super(Usuario.class);
    }

    public static UsuarioServices getInstancia(){
        if(instancia==null){
            instancia = new UsuarioServices();
        }
        return instancia;
    }

    public List<Usuario> findAllByNombre(String nombre){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select e from Usuario e where e.nombre like :nombre");
        query.setParameter("nombre", nombre+"%");
        List<Usuario> lista = query.getResultList();
        return lista;
    }

    public List<Usuario> consultaNativa(){
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery("select * from estudiante ", Usuario.class);
        //query.setParameter("nombre", apellido+"%");
        List<Usuario> lista = query.getResultList();
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
