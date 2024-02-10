package org.example.clases;

import java.util.ArrayList;
import java.util.List;

public class Blog {

	private static Blog instancia;
	private List<Articulo> articuloList;
	private List<Usuario> usuarioList;


	//Obtener lista de usuarios y validando que no hayas usuarios repetidos.
	public List<Usuario> setUsuario (Usuario usuario){
		for (Usuario user: usuarioList){
			if(user.getUsername().equals(usuario.getUsername())){
				return usuarioList;
			}else if(user.getNombre().equals(usuario.getNombre())){
				return usuarioList;
			}
		}
		usuarioList.add(usuario);
		return usuarioList;
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
}
