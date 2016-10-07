package org.akaza.openclinica.control.ssib;

/**
 * Created by S004256 on 05/10/2016.
 */
public class Persona {

	private int id;
	private String uid;

	public Persona(
		int id,
		String uid) {
		this.id =
			id;
		this.uid =
			uid;
	}

	public int getId() {
		return
			this.id;
	}

	public String getUid() {
		return
			this.uid;
	}
}

