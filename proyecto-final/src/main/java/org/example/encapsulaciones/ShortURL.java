package org.example.encapsulaciones;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import java.time.Instant;
import java.util.Date;

@Entity("shortURL")
public class ShortURL {

	@Id
	private String id;
	private String codigo;
	private String urlBase;
	private String urlCorta;
	private Date fechaCreacion;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setUrlBase(String urlBase) {
        this.urlBase = urlBase;
    }

    public void setUrlCorta(String urlCorta) {
        this.urlCorta = urlCorta;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setEstadisticas(EstadisticaURL estadisticas) {
        this.estadisticas = estadisticas;
    }

    public void setImgBase64(String imgBase64) {
        this.imgBase64 = imgBase64;
    }

    public ShortURL(String urlBase, String imgBase64) {
        this.urlBase = urlBase;
        this.urlCorta = shortener(urlBase);
        this.estadisticas = new EstadisticaURL();
        this.imgBase64 = imgBase64;
        this.fechaCreacion = Date.from(Instant.now());
    }
    public ShortURL(String id, String urlBase, String urlCorta, Date fechaCreacion, EstadisticaURL estadisticas, String imgBase64) {
        this.id = id;
        this.urlBase = urlBase;
        this.urlCorta = urlCorta;
        this.fechaCreacion = fechaCreacion;
        this.estadisticas = estadisticas;
        this.imgBase64 = imgBase64;
    }

    public void setId(String id) {
        this.id = id;
    }

    private EstadisticaURL estadisticas;
	private String imgBase64;



    public ShortURL() {
    }

    // URL corta debe ser nombre de dominio + id
	public String shortener(String url){
		return "";
	}

	public String getId() {
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
