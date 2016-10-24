package org.akaza.openclinica.control.ssib;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.akaza.openclinica.web.restful_ssib.dto.StudyEventDto;

public class PendingEventsBean
	implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Protocolo> protocolos;
	private List<Persona> personas;
	private List<StudyEventDto> eventos;

	public PendingEventsBean() {
		this.protocolos =
			new ArrayList<>();
		this.personas =
			new ArrayList<>();
		this.eventos =
			new ArrayList<>();
	}

	public PendingEventsBean(
		List<Protocolo> protocolos,
		List<Persona> personas,
		List<StudyEventDto> eventos) {

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

		if (eventos != null) {
			this.
				eventos.
				addAll(
					eventos);
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

	public List<StudyEventDto> getEventos() {
		return
			this.eventos;
	}
} 
