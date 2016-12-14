package org.akaza.openclinica.web.restful_ssib;

import org.akaza.openclinica.web.restful_ssib.dto.AdministrativeDataDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;

import es.ssib.otic.empi.proxy.CargaDatosProxy;
import es.ssib.otic.empi.proxy.DatosProxy;
/**
 * Created by S004256 on 09/12/2016.
 */
@Controller
@RequestMapping("/ssibData")
@Scope("prototype")
public class AdministrativeDataResource {

	private static final Logger LOGGER =
		LoggerFactory
			.getLogger(
				AdministrativeDataResource.class);

	private static final SimpleDateFormat SDF_ANYO =
		new SimpleDateFormat("yyyy");
	private URL url;

	public AdministrativeDataResource()
		throws BeanInitializationException{

		LOGGER.warn(
			"Inicializando AdministrativeDataResource");
		try {
			this.url =
				new URL(
					"http://proxy-pre.ssib.es:9080/porpacCorp/services/ProxyService?wsdl");
		} catch (MalformedURLException mue) {
			LOGGER.warn(
				"Excepción instanciando URL a Proxy Empi",
				mue);
		}
	}

	@RequestMapping(
		value = "/json/view/cipAut/{personCipAut}",
		method = RequestMethod.GET)
	public ResponseEntity<AdministrativeDataDto> getDataByCipAut(
		@PathVariable ("personCipAut") String personCipAut) {

		try {
			// TODO configurar/parametrizar.
			CargaDatosProxy cargaDatosProxy =
				new CargaDatosProxy(
					this.url,
					"HSAL",
					"S");
			// TODO numero petición.
			DatosProxy datosProxy =
				cargaDatosProxy.
					obtenerDatos(
						null,
						personCipAut,
						"19");

			AdministrativeDataDto administrativeDataDto =
				this.convertir(
					datosProxy);

			return
				new ResponseEntity<AdministrativeDataDto>(
					administrativeDataDto,
					HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.warn(
				"Error obteniendo datos administrativos para CIP Aut "
					+ personCipAut,
				e);
			AdministrativeDataDto administrativeDataDto =
				new AdministrativeDataDto();
			administrativeDataDto.setMensajeError(
				"Error obtenint dades administratives");
			return
				new ResponseEntity<AdministrativeDataDto>(
					administrativeDataDto,
					HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}
	@RequestMapping(
		value = "/json/view/nif/{personNif}",
		method = RequestMethod.GET)
	public ResponseEntity<AdministrativeDataDto> getDataByNif(
		@PathVariable("personNif") String personNif) {

		try {
			// TODO configurar/parametrizar.
			CargaDatosProxy cargaDatosProxy =
				new CargaDatosProxy(
					this.url,
					"HSAL",
					"S");
			// TODO numero petición.
			DatosProxy datosProxy =
				cargaDatosProxy.
					obtenerDatos(
						personNif,
						null,
						"19");

			AdministrativeDataDto administrativeDataDto =
				this.convertir(
					datosProxy);
			return
				new ResponseEntity<AdministrativeDataDto>(
					administrativeDataDto,
					HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.warn(
				"Error obteniendo datos administrativos para NIF "
					+ personNif,
				e);
			AdministrativeDataDto administrativeDataDto =
				new AdministrativeDataDto();
			administrativeDataDto.setMensajeError(
				"Error obtenint dades administratives");
			return
				new ResponseEntity<AdministrativeDataDto>(
					administrativeDataDto,
					HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	private AdministrativeDataDto convertir(
		DatosProxy datosProxy) {

		AdministrativeDataDto administrativeDataDto =
			new AdministrativeDataDto();
		if (datosProxy == null) {
			administrativeDataDto.setMensajeError(
				"No s'han trobat dades");
			return
				administrativeDataDto;
		}

		if (datosProxy.getFechaNacimiento() != null) {
			administrativeDataDto.setAnyoNacimiento(
				SDF_ANYO.format(
					datosProxy.getFechaNacimiento()));
		}

		administrativeDataDto.setApellido1(
			datosProxy.getApellido1());

		administrativeDataDto.setApellido2(
			datosProxy.getApellido2());

		administrativeDataDto.setNombre(
			datosProxy.getNombre());

		administrativeDataDto.setCipAut(
			datosProxy.getCipAutonomico());

		administrativeDataDto.setDni(
			datosProxy.getDni());

		DatosProxy.Sexo sexo =
			datosProxy.getSexo();
		if (sexo != null) {
			switch (sexo) {
				case MUJER:
					administrativeDataDto.setSexo("f"); //female
					break;
				case HOMBRE:
					administrativeDataDto.setSexo("m"); // male
					break;
			}
		}
		return administrativeDataDto;
	}
}
