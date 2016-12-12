package org.akaza.openclinica.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import org.akaza.openclinica.bean.oid.OidGenerator;
import org.akaza.openclinica.bean.oid.StudySubjectOidGenerator;
import org.akaza.openclinica.domain.datamap.Study;
import org.akaza.openclinica.domain.datamap.StudyEvent;
import org.akaza.openclinica.domain.datamap.StudySubject;

public class StudySubjectDao extends AbstractDomainDao<StudySubject> {

    @Override
    Class<StudySubject> domainClass() {
        // TODO Auto-generated method stub
        return StudySubject.class;
    }

    public StudySubject findByOcOID(String OCOID) {
        getSessionFactory().getStatistics().logSummary();
        String query = "from " + getDomainClassName() + " do  where do.ocOid = :OCOID";
        org.hibernate.Query q = getCurrentSession().createQuery(query);
        q.setString("OCOID", OCOID);
        return (StudySubject) q.uniqueResult();
    }

    public StudySubject findByLabelAndStudy(String embeddedStudySubjectId, Study study) {
        getSessionFactory().getStatistics().logSummary();
        String query = "from " + getDomainClassName() + " do  where do.study.studyId = :studyid and do.label = :label";
        org.hibernate.Query q = getCurrentSession().createQuery(query);
        q.setInteger("studyid", study.getStudyId());
        q.setString("label", embeddedStudySubjectId);
        return (StudySubject) q.uniqueResult();
    }

    public StudySubject findByLabelAndStudyOrParentStudy(String embeddedStudySubjectId, Study study) {
        getSessionFactory().getStatistics().logSummary();
        String query = "from " + getDomainClassName() + " do  where (do.study.studyId = :studyid or do.study.study.studyId = :studyid) and do.label = :label";
        org.hibernate.Query q = getCurrentSession().createQuery(query);
        q.setInteger("studyid", study.getStudyId());
        q.setString("label", embeddedStudySubjectId);
        return (StudySubject) q.uniqueResult();
    }

    public ArrayList<StudySubject> findByLabelAndParentStudy(String embeddedStudySubjectId, Study parentStudy) {
        getSessionFactory().getStatistics().logSummary();
        String query = "from " + getDomainClassName() + " do  where do.study.study.studyId = :studyid and do.label = :label";
        org.hibernate.Query q = getCurrentSession().createQuery(query);
        q.setInteger("studyid", parentStudy.getStudyId());
        q.setString("label", embeddedStudySubjectId);
        return (ArrayList<StudySubject>) q.list();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ArrayList<StudyEvent> fetchListSEs(String id) {
        String query = " from StudyEvent se where se.studySubject.ocOid = :id order by se.studyEventDefinition.ordinal,se.sampleOrdinal";
        org.hibernate.Query q = getCurrentSession().createQuery(query);
        q.setString("id", id.toString());

        return (ArrayList<StudyEvent>) q.list();

    }

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<StudySubject> getBySubjectUID(
		String subjectUID) {

		String hql =
			" from StudySubject ss "
				+ "where ss.subject.uniqueIdentifier= :subjectUID";
		Query query =
			getCurrentSession().
			createQuery(
				hql);
		query.setString(
			"subjectUID",
			subjectUID);

		return (List<StudySubject>) query.list();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<StudySubject> getByStudyID(
		int studyID) {

		String hql =
			" from StudySubject ss "
				+ "where ss.study.studyId= :studyID";
		Query query =
			getCurrentSession().
			createQuery(
				hql);
		query.setInteger(
			"studyID",
			studyID);

		return (List<StudySubject>) query.list();
	}

    public String getValidOid(StudySubject studySubject, ArrayList<String> oidList) {
    OidGenerator oidGenerator = new StudySubjectOidGenerator();
        String oid = getOid(studySubject);
        String oidPreRandomization = oid;
        while (findByOcOID(oid) != null || oidList.contains(oid)) {
            oid = oidGenerator.randomizeOid(oidPreRandomization);
        }
        return oid;
    }

    private String getOid(StudySubject studySubject) {
        OidGenerator oidGenerator = new StudySubjectOidGenerator();
        String oid;
        try {
            oid = studySubject.getOcOid() != null ? studySubject.getOcOid() : oidGenerator.generateOid(studySubject.getLabel());
            return oid;
        } catch (Exception e) {
            throw new RuntimeException("CANNOT GENERATE OID");
        }
    }
    public int findTheGreatestLabel() {
        List<StudySubject> allStudySubjects = super.findAll();
        
        int greatestLabel = 0;
        for (StudySubject subject:allStudySubjects) {
            int labelInt = 0;
            try {
                labelInt = Integer.parseInt(subject.getLabel());
            } catch (NumberFormatException ne) {
                labelInt = 0;
            }
            if (labelInt > greatestLabel) {
                greatestLabel = labelInt;
            }
        }
        return greatestLabel;
    }

}
