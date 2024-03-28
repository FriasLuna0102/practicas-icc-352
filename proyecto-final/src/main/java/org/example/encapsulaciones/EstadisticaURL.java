package org.example.encapsulaciones;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity("Estadistica")
public class EstadisticaURL {
	@Id
	private ObjectId id;
	private int cantidadAccesos;
	private Map<String, Integer> navegadores;
	private Map<String, Integer> direccionesIP;
	private Map<String, Integer> dominiosClientes;
	private Map<String, Integer> plataformasSO;
	private Map<Date, Integer> horasAcceso;

	public EstadisticaURL() {
		this.cantidadAccesos = 1;
		this.navegadores = new HashMap<>();
		this.direccionesIP = new HashMap<>();
		this.dominiosClientes = new HashMap<>();
		this.plataformasSO = new HashMap<>();
		this.horasAcceso = new HashMap<>();
	}

	public void aumentarCantAcceso(){
		this.cantidadAccesos++;
	}
	public int getCantidadAccesos() {
		return cantidadAccesos;
	}

	public Map<String, Integer> getNavegadores() {
		return navegadores;
	}

	public Map<String, Integer> getDireccionesIP() {
		return direccionesIP;
	}

	public Map<String, Integer> getDominiosClientes() {
		return dominiosClientes;
	}

	public Map<String, Integer> getPlataformasSO() {
		return plataformasSO;
	}

	public Map<Date, Integer> getHorasAcceso() {
		return horasAcceso;
	}

    public void setCantidadAccesos(int cantidadAccesos) {
        this.cantidadAccesos = cantidadAccesos;
    }
}
