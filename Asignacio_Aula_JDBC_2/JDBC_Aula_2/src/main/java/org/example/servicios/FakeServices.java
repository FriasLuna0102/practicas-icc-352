package org.example.servicios;

import org.example.encapsulaciones.Estudiante;
import org.example.encapsulaciones.Usuario;
import org.example.util.NoExisteEstudianteException;
import org.example.util.RolesApp;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Ejemplo de servicio patron Singleton
 */
public class FakeServices {

    private static FakeServices instancia;
    private List<Estudiante> listaEstudiante = EstudianteServices.getInstancia().consultaNativa();
    private List<Usuario> listaUsuarios = new ArrayList<>();

    /**
     * Constructor privado.
     */
    private FakeServices(){
        //añadiendo los estudiantes.
        Estudiante cama = new Estudiante(20011136, "Carlos Camacho", "ITT");

        Estudiante est = EstudianteServices.getInstancia().findByMatricula(cama.getMatricula());

        if(est == null){
            EstudianteServices.getInstancia().crear(cama);
        }
        //anadiendo los usuarios.
        listaUsuarios.add(new Usuario("admin", "admin", "1234", Set.of(RolesApp.ROLE_ADMIN, RolesApp.CUALQUIERA, RolesApp.LOGUEADO)));
        listaUsuarios.add(new Usuario("logueado", "logueado", "logueado", Set.of(RolesApp.CUALQUIERA)));
        listaUsuarios.add(new Usuario("usuario", "usuario", "usuario", Set.of(RolesApp.ROLE_USUARIO)));

    }

    public static FakeServices getInstancia(){
        if(instancia==null){
            instancia = new FakeServices();
        }
        return instancia;
    }

    /**
     * Permite autenticar los usuarios. Lo ideal es sacar en
     * @param usuario
     * @param password
     * @return
     */
    public Usuario autheticarUsuario(String usuario, String password){
        //simulando la busqueda en la base de datos.
        return new Usuario(usuario, "Usuario "+usuario, password);
    }

    public List<Usuario> getListaUsuarios(){
        return listaUsuarios;
    }

    public List<Estudiante> listarEstudiante(){
        return listaEstudiante;
    }

    public Estudiante getEstudiantePorMatricula(int matricula){
        return listaEstudiante.stream().filter(e -> e.getMatricula() == matricula).findFirst().orElse(null);
    }

    public Estudiante crearEstudiante(Estudiante estudiante){
        if(getEstudiantePorMatricula(estudiante.getMatricula())!=null){
            System.out.println("Estudiante registrado...");
            return null; //generar una excepcion...
        }
        EstudianteServices.getInstancia().crear(estudiante);
        listaEstudiante.add(estudiante);
        return estudiante;
    }

    public Estudiante actualizarEstudiante(Estudiante estudiante){
        Estudiante tmp = getEstudiantePorMatricula(estudiante.getMatricula());
        if(tmp == null){//no existe, no puede se actualizado
            throw new NoExisteEstudianteException("No Existe el estudiante: "+estudiante.getMatricula());
        }
        tmp.setNombre(estudiante.getNombre());
        tmp.setCarrera(estudiante.getCarrera());
        // No cambies la matrícula aquí si es la clave primaria
        EstudianteServices.getInstancia().editar(tmp);
        return tmp;
    }


    public boolean eliminandoEstudiante(int matricula){
        Estudiante tmp = EstudianteServices.getInstancia().findByMatricula(matricula);
        EstudianteServices.getInstancia().eliminar(matricula);
        return listaEstudiante.remove(tmp);
    }

}
