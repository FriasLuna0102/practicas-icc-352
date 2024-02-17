package org.example.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import org.example.clases.Usuario;

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

    public List<Usuario> findAllByUsername(String username){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select u from Usuario u where u.username like :username");
        query.setParameter("username", username+"%");
        List<Usuario> lista = query.getResultList();
        return lista;
    }

    public Usuario findByUsername(String username) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createQuery("select u from Usuario u where u.username = :username", Usuario.class);
            query.setParameter("username", username);
            return (Usuario) query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        } finally {
            em.close();
        }
    }

    public List<Usuario> obtenerTodosLosUsuarios() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
        } finally {
            em.close();
        }
    }

}
