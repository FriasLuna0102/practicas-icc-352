package org.example.clases;

import org.example.services.ArticuloServices;
import org.example.services.EtiquetaServices;
import org.example.services.UsuarioServices;

import java.util.ArrayList;
import java.util.List;

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

		for (String s : etiquetaArray) {
			Etiqueta etiqueta = this.etiquetaList.stream()
					.filter(e -> e.getEtiqueta().equalsIgnoreCase(s))
					.findFirst()
					.orElse(null);

			if (etiqueta == null) {
				etiqueta = crearEtiqueta(s.trim());
			}

			etiquetaList.add(etiqueta);
		}

		return etiquetaList;
	}





	//Metodo auxiliar para crear etiqueta
	private Etiqueta crearEtiqueta(String nombre){
		// Busca en la base de datos una Etiqueta con el mismo nombre
		Etiqueta etiqueta = Etiqueta.buscarEtiquet(nombre);

		// Si no se encontró la Etiqueta, la crea y la guarda en la base de datos
		if (etiqueta == null) {
			etiqueta = new Etiqueta(nombre);
			EtiquetaServices.getInstancia().crear(etiqueta);
			this.etiquetaList.add(etiqueta);
		}

		return etiqueta;
	}








	//Metodo para obtener un articulo por el id.
	public Articulo obtenerArticuloPorId(long id){
		for(Articulo articulo: articuloList){
			if(articulo.getId() == id){
				return articulo;
			}
		}
		return null; // Devuelve null si no se encuentra ningún artículo con ese ID
	}

	public boolean eliminarArticuloById(long id) {
		for (Articulo articulo: this.articuloList){
			if (articulo.getId() == id){
				this.articuloList.remove(articulo);
				return true;
			}
		}
		return false;
	}

	//Add usuarios si no estan repetidos.
	public void addUsuario(Usuario usuario){

		// Buscar el usuario en la base de datos utilizando Hibernate
		List<Usuario> usuarios = UsuarioServices.getInstancia().findAllByUsername(usuario.getUsername());

		for (Usuario user: usuarios){
			if(user.getUsername().equals(usuario.getUsername()) || user.getNombre().equals(usuario.getNombre())){
				return;
			}
		}
		UsuarioServices.getInstancia().crear(new Usuario(usuario.getUsername(),usuario.getNombre(),usuario.getPassword()
		,usuario.isAdministrator(),usuario.isAutor(), usuario.getFoto()));
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
		articuloList = ArticuloServices.getInstancia().obtenerArticulosConEtiquetasPorPagina(1,5);
		usuarioList = UsuarioServices.getInstancia().obtenerTodosLosUsuarios();
		etiquetaList = EtiquetaServices.getInstancia().obtenerTodasLasEtiquetas();
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
