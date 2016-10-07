package org.akaza.openclinica.control.ssib;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PendingEventsBean
	implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Protocolo> protocolos;
	private List<Persona> personas;

	public PendingEventsBean() {
		this.protocolos =
			new ArrayList<>();
		this.personas =
			new ArrayList<>();
	}

	public PendingEventsBean(
		List<Protocolo> protocolos,
		List<Persona> personas) {

		this();
		if (protocolos != null) {
			this.
				protocolos.
				addAll(
					protocolos);
		} 

		if (personas != null) {
			this.
				personas.
				addAll(
					personas);
		}
	}

	public List<Protocolo> getProtocolos() {
		return
			this.protocolos;
	}	

	public List<Persona> getPersonas() {
		return
			this.personas;
	}
} 
