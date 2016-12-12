package org.akaza.openclinica.control.ssib;

import org.akaza.openclinica.bean.managestudy.StudySubjectBean;
import org.akaza.openclinica.control.core.SecureController;
import org.akaza.openclinica.control.form.FormProcessor;
import org.akaza.openclinica.control.submit.AddNewSubjectServlet;
import org.akaza.openclinica.control.submit.SubmitDataServlet;
import org.akaza.openclinica.dao.hibernate.SubjectDao;
import org.akaza.openclinica.domain.datamap.Subject;
import org.akaza.openclinica.i18n.core.LocaleResolver;
import org.akaza.openclinica.view.Page;
import org.akaza.openclinica.web.InsufficientPermissionException;
import org.akaza.openclinica.web.restful_ssib.PersonSubjectResource;
import org.akaza.openclinica.web.restful_ssib.StudySubjectEventResource;
import org.akaza.openclinica.web.restful_ssib.StudySubjectResource;
import org.akaza.openclinica.web.restful_ssib.dto.StudyEventDto;
import org.akaza.openclinica.web.restful_ssib.dto.StudySubjectDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException; 
/**
 * Created by S004256 on 04/10/2016.
 */
public class FindPendingEvents extends SecureController {

	@Autowired
	private PersonSubjectResource personSubjectResource;

	@Autowired
	private StudySubjectEventResource studySubjectEventResource;

	@Autowired
	private StudySubjectResource studySubjectResource;

	@Autowired
	private SubjectDao subjectDao;

	private static final SimpleDateFormat SDF_INPUT =
		new SimpleDateFormat(
			"dd/MM/yyyy");

	private static final SimpleDateFormat SDF_OUTPUT =
		new SimpleDateFormat(
			"yyyyMMddHHmmss");

	private WebApplicationContext springContext;

	@Override
	public void init(
		final ServletConfig config)
			throws ServletException {
		
		super.
			init(
				config);

		WebApplicationContext springContext =
			WebApplicationContextUtils.
				getRequiredWebApplicationContext(
					config.getServletContext());
		
		final AutowireCapableBeanFactory beanFactory =
			springContext.
				getAutowireCapableBeanFactory();
		beanFactory.
			autowireBean(
				this);
	}

	/**
	 * @see org.akaza.openclinica.control.core.SecureController#mayProceed()
 	 */
	@Override
	protected void mayProceed() 
		throws InsufficientPermissionException {

		this.
			logger.
			warn(
				"SJM: mayProceed()");

		Locale locale =
			LocaleResolver.
				getLocale(
					request);

		if (ub.isSysAdmin()) {
			this.
				logger.
				warn(
					"SJM: mayProceed sale porque es admin");
			return;
		}

		if (SubmitDataServlet.mayViewData(ub, currentRole)) {
			this.
				logger.
				warn(
					"SJM: mayProceed sale porque mayViewData");
			return;
		}

		this.
			logger.
			warn(
				"SJM: mayProceed lanzara excepcion");

		addPageMessage(
			respage.
				getString(
					"no_have_correct_privilege_current_study")
				+ respage.
					getString(
						"change_study_contact_sysadmin"));

		throw
			new InsufficientPermissionException(
				Page.MENU_SERVLET,
				resexception.getString(
					"may_not_submit_data"),
				"1");
	}

	@Override
	protected void processRequest() 
		throws Exception {

		try {
			getCrfLocker().unlockAllForUser(ub.getId());

			String idSetting =
				currentStudy.
					getStudyParameterConfig().
					getSubjectIdGeneration();

			List<Protocolo> protocolos =
				new ArrayList<>();
			protocolos.add(
				new Protocolo(
					1,
					"Protocolo 1"));
			protocolos.add(
				new Protocolo(
					2,
					"Hepatitis C"));
//			request.
//				setAttribute(
//					"protocolos",
//					protocolos);

			List<Persona> personas =
				new ArrayList<>();
			List<Subject> subjects =
				this.
					subjectDao.
					findAll();
			for(Subject subject : subjects) {
				String subjectUid =
					subject.
						getUniqueIdentifier();
				if ((subjectUid != null) && !subjectUid.trim().isEmpty()) {
					Persona persona =
						new Persona(
							subject.
								getSubjectId(),
							subjectUid);
					personas.
						add(
							persona);
				}
			}

			String idPersona =
				request.
					getParameter(
						"idPersona");

			String idProtocolo =
				request.
					getParameter(
						"idProtocolo");

			String fecha =
				request.
					getParameter(
						"fecha");

			this.
				logger.
				warn(
					"SJM: personSubjectResource es "
						+ personSubjectResource);


			List<StudyEventDto> eventos =
				this.
					getEventos(
						fecha,
						idPersona,
						idProtocolo);

			PendingEventsBean pendingEventsBean =
				new PendingEventsBean(
					protocolos,
					personas,
					eventos);
			request.
				setAttribute(
					"pendingEvents",
					pendingEventsBean);

			forwardPage(
				Page.SSIB_EVENTOS_ABIERTOS);
		} catch (Exception e) {
			this.
				logger.
				warn(
					"SJM: Excepción en Servlet",
					e);
			throw e;
		}
	}

	private List<StudyEventDto> getEventos(
		String fecha,
		String idPersona,
		String idProtocolo) 
			throws Exception {

		List<StudyEventDto> eventos =
			new ArrayList<>();

		if ((idProtocolo == null) || idProtocolo.trim().isEmpty()) {
			return null;
		}

		List<StudySubjectDto> sujetosBuscados =
			new ArrayList<>();

		int protocolIntId =
			Integer.
				parseInt(
					idProtocolo);
		if (idPersona != null) {
			this.
				logger.
				warn(
					"SJM: Buscando sujetos con UID "
						+ idPersona);
			List<StudySubjectDto> sujetos =
				new ArrayList<>(
					this.
						personSubjectResource.
						getPersonByUID(
							idPersona).getBody());


			this.
				logger.
				warn(
					"SJM: El tamaño de StudySubjectDto es "
						+ sujetos.size());

			StudySubjectDto sujetoBuscado =
				null;
			for (StudySubjectDto sujeto : sujetos) {
				if (sujeto.getStudyId() == protocolIntId) {
					sujetoBuscado =
						sujeto;
				}
			}

			// TODO: Error si no se encuentra el sujeto???
			this.
				logger.
				warn(
					"SJM: El sujetoBuscado es "
						+ (sujetoBuscado == null ? "null" : Integer.toString(sujetoBuscado.getStudySubjectId())));
		} else {
			this.
				logger.
				warn(
					"SJM: Buscando sujetos de estudio "
						+ idProtocolo);
			sujetosBuscados.
				addAll(
					this.
						studySubjectResource.
						getStudySubjects(
							protocolIntId));
		}

		Date date;
		if ((fecha != null) && !fecha.trim().isEmpty()) {
			date =
				SDF_INPUT.
					parse(
						fecha);
		} else {
			date =
				new Date();
		}

		for (StudySubjectDto sujetoBuscado : sujetosBuscados) {
			eventos.
				addAll(
					this.
						studySubjectEventResource.
						getOpenEventsBySubjectAndDate(
							sujetoBuscado.
								getStudySubjectId(),
							SDF_OUTPUT.
								format(date)
								+ "000000"
						));
		}

		this.
			logger.
			warn(
				"SJM: Número de eventos "
					+ eventos.size());

		return
			eventos;
	}
}
