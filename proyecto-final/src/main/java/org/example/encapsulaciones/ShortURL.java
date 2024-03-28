package org.example.encapsulaciones;

import java.time.Instant;
import java.util.Date;

public class ShortURL {

	private long id;
	private String urlBase;
	private String urlCorta;
	private Date fechaCreacion;
	private EstadisticaURL estadisticas;
	private String imgBase64;

	public ShortURL(String urlBase, String imgBase64) {
		this.urlBase = urlBase;
		this.urlCorta = shortener(urlBase);
		this.estadisticas = new EstadisticaURL();
		this.imgBase64 = imgBase64;
		this.fechaCreacion = Date.from(Instant.now());
	}

	// URL corta debe ser nombre de dominio + id
	public String shortener(String url){
		return "";
	}

	public long getId() {
		return id;
	}

	public String getUrlBase() {
		return urlBase;
	}

	public String getUrlCorta() {
		return urlCorta;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public EstadisticaURL getEstadisticas() {
		return estadisticas;
	}

	public String getImgBase64() {
		return imgBase64;
	}
}
