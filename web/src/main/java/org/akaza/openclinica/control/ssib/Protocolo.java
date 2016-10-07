package org.akaza.openclinica.control.ssib;

/**
 *  * Created by S004256 on 04/10/2016.
 *   */
public class Protocolo {
	private int id;
	private String descripcion;

	public Protocolo(){
	}

	public Protocolo(
		int id,
		String descripcion) {

		this.id =
			id;

		this.descripcion =
			descripcion;
	}

	public int getId() {
		return
			this.id;
	}

	public String getDescripcion() {
		return
			this.descripcion;
	}
}

