package org.example.clases;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Blog {

	private static Blog instancia;
	private List<Articulo> articuloList;
	private List<Usuario> usuarioList;
	private Usuario usuario;

	//Metodo para convertir un string de etiquetas en una lista.
	public List<Etiqueta> stringToEtiqueta(String etiquetasString) {
		List<Etiqueta> listEtiquetas = new ArrayList<>();

		// Dividir el string en etiquetas individuales utilizando una coma como delimitador
		String[] etiquetasArray = etiquetasString.split(",");

		// Iterar sobre el array de etiquetas
		for (int i = 0; i < etiquetasArray.length; i++) {
			// Obtener la etiqueta y eliminar los espacios en blanco alrededor
			String etiqueta = etiquetasArray[i].trim();

			// Crear un nuevo objeto Etiqueta y añadirlo a la lista
			Etiqueta newEtiqueta = new Etiqueta(i + 1, etiqueta);
			listEtiquetas.add(newEtiqueta);
		}

		return listEtiquetas;
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
		int size = articuloList.size();
		for (int i = 0; i < size; i++) {
			Articulo articulo = articuloList.get(i);

			if (Objects.equals(articulo.getId(), id)) {
				articuloList.remove(i);
				return true;
			}
		}
		return false;
	}

	//Add usuarios si no estan repetidos.
	public void addUsuario(Usuario usuario){
		for (Usuario user: usuarioList){
			if(user.getUsername().equals(usuario.getUsername()) || user.getNombre().equals(usuario.getNombre())){
				return;
			}
		}
		usuarioList.add(usuario);
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
	}

	public List<Articulo> getArticuloList() {
		return articuloList;
	}

	public void setArticuloList(List<Articulo> articuloList) {
		this.articuloList = articuloList;
	}

	public List<Usuario> getUsuarioList() {
		return usuarioList;
	}

	public void setUsuarioList(List<Usuario> usuarioList) {
		this.usuarioList = usuarioList;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
