package org.akaza.openclinica.web.restful_ssib.dto;

/**
 * Created by S004256 on 13/12/2016.
 */
public class AdministrativeDataDto {

	private String anyoNacimiento;
	private String dni;
	private String cipAut;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private String sexo;
	private String mensajeError;

	public String getAnyoNacimiento() {
		return anyoNacimiento;
	}

	public void setAnyoNacimiento(
		String anyoNacimiento) {

		this.anyoNacimiento = anyoNacimiento;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(
		String dni) {

		this.dni = dni;
	}

	public String getCipAut() {
		return cipAut;
	}

	public void setCipAut(
		String cipAut) {

		this.cipAut = cipAut;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(
		String nombre) {

		this.nombre = nombre;
	}

	public String getApellido1() {
		return apellido1;
	}

	public void setApellido1(
		String apellido1) {

		this.apellido1 = apellido1;
	}

	public String getApellido2() {
		return apellido2;
	}

	public void setApellido2(
		String apellido2) {

		this.apellido2 = apellido2;
	}

	public String getNombreConjunto() {
		if (this.mensajeError != null) {
			return null;
		}
		StringBuilder stringBuilder =
			new StringBuilder(100);
		if (this.nombre != null) {
			stringBuilder.append(
				this.nombre);
			stringBuilder.append(
				' ');
		}
		if (this.apellido1 != null) {
			stringBuilder.append(
				this.apellido1);
			stringBuilder.append(
				' ');
		}
		if (this.apellido2 != null) {
			stringBuilder.append(
				this.apellido2);
		}
		String nombreConjunto =
			stringBuilder.
				toString().
				trim();

		return nombreConjunto;
	}
	public String getSexo() {
		return sexo;
	}

	public void setSexo(
		String sexo) {

		this.sexo = sexo;
	}

	public String getMensajeError() {
		return this.mensajeError;
	}

	public void setMensajeError(
		String mensajeError) {
		this.mensajeError = mensajeError;
	}
}
