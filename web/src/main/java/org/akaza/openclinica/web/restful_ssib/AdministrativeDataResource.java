package org.akaza.openclinica.web.restful_ssib;

import org.akaza.openclinica.control.core.SecureController;
import org.akaza.openclinica.web.InsufficientPermissionException;
import org.akaza.openclinica.web.restful_ssib.dto.StudySubjectDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.MalformedURLException;
import java.net.URL;

import es.ssib.otic.empi.proxy.CargaDatosProxy;
import es.ssib.otic.empi.proxy.DatosProxy;

/**
 * Created by S004256 on 09/12/2016.
 */
@Controller
@RequestMapping("/ssibAdministrativeData")
@Scope("prototype")
public class AdministrativeDataResource {

	private static final Logger LOGGER =
		LoggerFactory
			.getLogger(
				AdministrativeDataResource.class);

	private URL url;

	public AdministrativeDataResource()
		throws BeanInitializationException{
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
	public ResponseEntity<DatosProxy> getDataByCipAut(
		@PathVariable("personCipAut") String personCipAut)
			throws Exception {
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

		return
			new ResponseEntity<DatosProxy>(
				datosProxy,
				HttpStatus.OK);
	}

	@RequestMapping(
		value = "/json/view/nif/{personNif}",
		method = RequestMethod.GET)
	public ResponseEntity<DatosProxy> getDataByNif(
		@PathVariable("personNif") String personNif)
		throws Exception {
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

		return
			new ResponseEntity<DatosProxy>(
				datosProxy,
				HttpStatus.OK);
	}


}
