package org.example.clases;

import org.example.services.BootStrapServices;
import org.example.services.EtiquetaServices;
import org.example.services.UsuarioServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Blog {

	private static Blog instancia;
	private List<Articulo> articuloList;
	private List<Usuario> usuarioList;
	private List<Etiqueta> etiquetaList;
	private long contadorEtiqueta;
	private Usuario usuario;

	//Metodo para convertir un string de etiquetas en una lista.
	public List<Etiqueta> stringToEtiqueta(String etiquetasString) {

		List<Etiqueta> etiquetaList = new ArrayList<>();
		String[] etiquetaArray = etiquetasString.split(",", 0);
		boolean existe = false;

		for (String s : etiquetaArray) {
			for (Etiqueta etiqueta : this.etiquetaList) {
				if (etiqueta.getEtiqueta().equalsIgnoreCase(s)) {
					etiquetaList.add(etiqueta);
					existe = true;
				}
			}
			if (!existe) {
				crearEtiqueta(s.trim());
				etiquetaList.add(this.etiquetaList.getLast());
			}
			existe = false;
		}

		return etiquetaList;
	}

	//Metodo auxiliar para crear etiqueta
	private void crearEtiqueta(String nombre){

		Etiqueta tmp = new Etiqueta(contadorEtiqueta,nombre);
		this.etiquetaList.add(tmp);

		//Solucionar lo del id en la base de datos, ya que el contador se reinicia
		//y vuelve a 1 y en la base de datos ya hay informacion con id 1.
		EtiquetaServices.getInstancia().crear(new Etiqueta(contadorEtiqueta,nombre));
		contadorEtiqueta++;
		System.out.println(tmp.getEtiqueta());
		System.out.println(contadorEtiqueta);
	}

	//Metodo para obtener un articulo por el id.
	public Articulo obtenerArticuloPorId(String id){
		for(Articulo articulo: articuloList){
			if(articulo.getId().equals(id)){
				return articulo;
			}
		}
		return null; // Devuelve null si no se encuentra ningún artículo con ese ID
	}

	public boolean eliminarArticuloById(String id) {
		for (Articulo articulo: this.articuloList){
			if (articulo.getId().equals(id)){
				this.articuloList.remove(articulo);
				return true;
			}
		}
		return false;
	}

	//Add usuarios si no estan repetidos.
	public void addUsuario(Usuario usuario){

		// Buscar el usuario en la base de datos utilizando Hibernate
		List<Usuario> usuarios = UsuarioServices.getInstancia().findAllByNombre(usuario.getUsername());

		for (Usuario user: usuarios){
			if(user.getUsername().equals(usuario.getUsername()) || user.getNombre().equals(usuario.getNombre())){
				return;
			}
		}
		UsuarioServices.getInstancia().crear(new Usuario(usuario.getUsername(),usuario.getNombre(),usuario.getPassword()
		,usuario.isAdministrator(),usuario.isAutor()));
		//usuarioList.add(usuario);
	}

	//Buscar usuario por username
	public Usuario buscarUsuario (String username){
		for(Usuario usuario : usuarioList){
			if(usuario.getUsername().equals(username)){
				return usuario;
			}
		}
		return null;
	}

	public static Blog getInstance(){
		if (instancia == null){
			instancia = new Blog();
		}
		return instancia;
	}

	private Blog(){
		articuloList = new ArrayList<>();
		usuarioList = new ArrayList<>();
		etiquetaList = new ArrayList<>();
		contadorEtiqueta = 1;
	}

	public List<Articulo> getArticuloList() {
		return articuloList;
	}

	public List<Usuario> getUsuarioList() {
		return usuarioList;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
