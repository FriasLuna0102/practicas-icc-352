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

    public List<Articulo> findAllByNombre(long idArticulo){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select a from Articulo a where a.id = :idArticulo");
        query.setParameter("idArticulo", idArticulo);
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

    public List<Articulo> obtenerTodosLosArticulos() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT a FROM Articulo a", Articulo.class).getResultList();
        } finally {
            em.close();
        }
    }


    public List<Articulo> obtenerTodosLosArticulosConEtiquetas() {
        EntityManager em = getEntityManager();
        try {
            // Utilizamos LEFT JOIN FETCH para traer todas las etiquetas asociadas a cada artículo en una sola consulta
            return em.createQuery("SELECT DISTINCT a FROM Articulo a LEFT JOIN FETCH a.listaEtiquetas", Articulo.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Articulo> obtenerArticulosConEtiquetasPorPagina(int numeroPagina, int articulosPorPagina) {
        EntityManager em = getEntityManager();
        try {
            // Calculamos el primer resultado de la página
            int primerResultado = (numeroPagina - 1) * articulosPorPagina;

            // Utilizamos LEFT JOIN FETCH para traer todas las etiquetas asociadas a cada artículo en una sola consulta
            return em.createQuery("SELECT DISTINCT a FROM Articulo a LEFT JOIN FETCH a.listaEtiquetas", Articulo.class)
                    .setFirstResult(primerResultado) // Establecemos el primer resultado
                    .setMaxResults(articulosPorPagina) // Establecemos el número máximo de resultados
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public long contarArticulos() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(a) FROM Articulo a", Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }


}
