package org.example.servicios;

import jakarta.persistence.NoResultException;
import org.example.encapsulaciones.Estudiante;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;


/**
 *
 * Created by vacax on 03/06/16.
 */
public class EstudianteServices extends GestionDb<Estudiante> {

    private static EstudianteServices instancia;

    private EstudianteServices() {
        super(Estudiante.class);
    }

    public static EstudianteServices getInstancia(){
        if(instancia==null){
            instancia = new EstudianteServices();
        }
        return instancia;
    }

    /**
     *
     * @param nombre
     * @return
     */
    public List<Estudiante> findAllByNombre(String nombre){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select e from Estudiante e where e.nombre like :nombre");
        query.setParameter("nombre", nombre+"%");
        List<Estudiante> lista = query.getResultList();
        return lista;
    }

    public Estudiante findByMatricula(int matricula) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createQuery("SELECT e FROM Estudiante e WHERE e.matricula = :matricula");
            query.setParameter("matricula", matricula);
            return (Estudiante) query.getSingleResult();
        } catch (NoResultException e) {
            return null; // Devuelve null si no se encuentra ningún estudiante con esa matrícula
        } finally {
            em.close();
        }
    }


    public List<Estudiante> consultaNativa(){
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery("select * from estudiante ", Estudiante.class);
        //query.setParameter("nombre", apellido+"%");
        List<Estudiante> lista = query.getResultList();
        return lista;
    }


}
