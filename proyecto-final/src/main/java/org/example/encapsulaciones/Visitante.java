package org.example.encapsulaciones;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Visitante {
	@Id
	private UUID id;

	@Reference
	private List<ShortURL> urlList;

	public Visitante() {
		this.id = UUID.randomUUID();
		this.urlList = new ArrayList<>();
	}

	public UUID getId() {
		return id;
	}

	public List<ShortURL> getUrlList() {
		return urlList;
	}
}
