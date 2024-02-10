package org.example.clases;

import java.util.ArrayList;
import java.util.List;

public class Blog {

	private static Blog instancia;
	private List<Articulo> articuloList;
	private List<Usuario> usuarioList;


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
}
