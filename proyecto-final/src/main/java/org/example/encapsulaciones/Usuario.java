package org.example.encapsulaciones;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
	private String username;
	private String nombre;
	private String password;
	private boolean user;
	private List<ShortURL> urlList;

	public Usuario(String username, String nombre, String password, boolean user) {
		this.username = username;
		this.nombre = nombre;
		this.password = password;
		this.user = user;
		this.urlList = new ArrayList<>();
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUser(boolean user) {
		this.user = user;
	}

	public String getUsername() {
		return username;
	}

	public String getNombre() {
		return nombre;
	}

	public boolean isUser() {
		return user;
	}

	public List<ShortURL> getUrlList() {
		return urlList;
	}
}
