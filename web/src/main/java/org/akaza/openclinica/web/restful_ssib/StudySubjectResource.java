package org.akaza.openclinica.web.restful_ssib;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.util.CycleDetectionStrategy;
import org.akaza.openclinica.bean.login.UserAccountBean;
import org.akaza.openclinica.dao.hibernate.StudySubjectDao;
import org.akaza.openclinica.domain.datamap.StudySubject;
import org.akaza.openclinica.web.restful_ssib.dto.StudySubjectDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
/**
 * Rest service for ODM clinical data usage
 * ROOT_CONTEXT/rest/studySubject/{format}/{personUid} format:xml/ json
 * mode:view
 * 
 * @author SJM
 */

@Controller
@RequestMapping("/studySubject")
@Scope("prototype")
public class StudySubjectResource {
	private static final Logger LOGGER =
		LoggerFactory
			.getLogger(
				PersonSubjectResource.class);

	private static final int INDENT_LEVEL = 2;

	@Autowired
	private StudySubjectDao studySubjectDao;

	public StudySubjectDao getStudySubjectDao() {
		return
			this.studySubjectDao;
	}

	public void setStudySubjectDao(
		StudySubjectDao studySubjectDao) {
		this.studySubjectDao =
			studySubjectDao;
	}

	@RequestMapping(
		value = "/json/view/{studyId}",
		method = RequestMethod.GET)
	public ResponseEntity<Collection<StudySubjectDto>> getStudySubjectsJson(
		@PathVariable("studyId") int studyID)
			throws Exception {
	
                LOGGER.warn(
                        "SJM: Requesting studySubjects for studyId " + studyID);
                try {

			Collection<StudySubjectDto> results =
				this.
					getStudySubjects(
						studyID);

			LOGGER.warn(
				"SJM: Saliendo getStudySubjectsJson");

			return
				new ResponseEntity<Collection<StudySubjectDto>>(
					results,
					HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.warn(
				"SJM: Excepcion: "
					+ e.getMessage(),
				e);
			throw e;
		}
	}

	public Collection<StudySubjectDto> getStudySubjects(
		int studyID)
			throws Exception {
	
                try {
			Collection<StudySubject> studySubjects =
				studySubjectDao.
					getByStudyID(
						studyID);

			LOGGER.warn(
				"SJM: Saliendo getStudyID");

			Collection<StudySubjectDto> results =
				new ArrayList<>();
			for (StudySubject studySubject : studySubjects) {
				results.
					add(
						new StudySubjectDto(
							studySubject));
			}

			return
				results;
		} catch (Exception e) {
			LOGGER.warn(
				"SJM: Excepcion: "
					+ e.getMessage(),
				e);
			throw e;
		}
	}
}

