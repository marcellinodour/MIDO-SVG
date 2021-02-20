package com.github.cocolollipop.mido_svg.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.xml.bind.JAXBElement;

import com.github.cocolollipop.mido_svg.svg_generator.Settings;
import com.github.cocolollipop.mido_svg.university.components.Department;
import com.github.cocolollipop.mido_svg.university.components.Formation;
import com.github.cocolollipop.mido_svg.university.components.Licence;
import com.github.cocolollipop.mido_svg.university.components.Master;
import com.github.cocolollipop.mido_svg.university.components.Subject;
import com.github.cocolollipop.mido_svg.university.components.Teacher;
import com.github.cocolollipop.mido_svg.xml.jaxb.model.Tag;
import com.google.common.base.Verify;
import com.google.common.base.VerifyException;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import ebx.ebx_dataservices.StandardException;
import schemas.ebx.dataservices_1.CourseType.Root.Course;
import schemas.ebx.dataservices_1.MentionType.Root.Mention;
import schemas.ebx.dataservices_1.OrgUnitType.Root.OrgUnit;
import schemas.ebx.dataservices_1.PersonType.Root.Person;
import schemas.ebx.dataservices_1.ProgramType.Root.Program;

/**
 * This class fetch informations from Dauphine's DataBase 
 * @author Sarra and Zhenyi
 * @date 10/06/2020
 */
public class RofDatabase {

	private Department department;
	private ImmutableSet<Formation> formations;
	private ImmutableSet<Subject> subjects;
	private ImmutableMap<Subject,String> tags;
	private ImmutableMap<String, Teacher> teachers;

	static public final String MENTION_MIDO_IDENT = "?";

	/**
	 * Initialize an immutable Database with ROF informations.
	 * @return RofDatabase
	 * @throws Exception 
	 */
	public static RofDatabase initialize() throws Exception {
		return new RofDatabase();
	}

	private RofDatabase() throws Exception{

		QueriesHelper.setDefaultAuthenticator();
		this.department = fetchDepartment();
		this.formations = fetchFormations();
		this.teachers = fetchTeachers();

	}

	/**
	 * This method use subject List (previously fetch) to fetch all teachers.
	 * Set an ImmutableList of teachers
	 */
	private ImmutableMap<String, Teacher> fetchTeachers() {
		Map<String, Teacher> teacherList = new HashMap<>();

		for (Subject subject : this.subjects) {
			if (!teacherList.containsKey(subject.getResponsible().getLastName())) {
				teacherList.put(subject.getResponsible().getLastName(),subject.getResponsible());
			}
		}
		return ImmutableMap.copyOf(teacherList);
	}

	/**
	 * This method enables to retrieve the final
	 * levels of the programs which contain the courses'list in the refCourse field
	 * The final levels of Program are the only programs that have the refProgram field empty
	 * and the refCourse field containing all courses
	 * @param program
	 * @return a list of final levels programs (containing the courses)
	 * @throws StandardException
	 * @author TajouriSarra
	 */
	private List<Program> childProgram(Program program) throws StandardException{
		List<Program> child = new ArrayList<>();
		Querier querier = new Querier();

		if (!program.getProgramStructure().getValue().getRefCourse().isEmpty()) {
			child.add(program);
		}else {
			List<String> refProg = program.getProgramStructure().getValue().getRefProgram();
			for (String ref : refProg) {
				Program p = querier.getProgram(ref);
				child.addAll(childProgram(p));
			}
		}
		return child;
	}

	/** 
	 * This method enables to get all the subjects in each formation from ROF.
	 * Now, we only fetch subjects of M1 MIAGE APP by a hard code.
	 * But, when we will be able to fetch all Programm ID,
	 * we automatically fetch it.
	 * @author marcellinodour and Raphda
	 * Set a ImmutableList of Subject
	 * @return 
	 * @throws StandardException 
	 * @throws Exception 
	 **/
	private ImmutableSet<Subject> fetchSubjects(Program program, Formation formation) throws IllegalStateException {

		Set<Subject> rofSubjectList = new HashSet<>();
		Querier querier = new Querier();
		List<String> courseRefs = new ArrayList<>();
		List<Program> progs  = new ArrayList<>();
		List<String> refProgram = new ArrayList<>();
		Map<Subject,String> tagsList = new HashMap<>();

		if (program.getProgramStructure() !=  null) {
			refProgram = program.getProgramStructure().getValue().getRefProgram();
		}
		if (!refProgram.isEmpty()) {
			try {
				progs = childProgram(program);
			} catch (StandardException e) {
				throw new IllegalStateException(e);
			}
		}
		for (Program p: progs) {
			courseRefs = p.getProgramStructure().getValue().getRefCourse();
			for(String courseRef : courseRefs) {
				Course course;

				try {
					course = querier.getCourse(courseRef);
				} catch (StandardException e) {
					throw new IllegalStateException(e);
				}

				Subject subject;

				subject = createSubject(course, formation);
				
				if(!(course.getFormalPrerequisites() == null)) {
					subject.addListOfPrerequisites(new Subject(course.getFormalPrerequisites().getValue().getFr().getValue(), 0));
				}

				if (course.getSearchword() != null) {
					for (String tag : course.getSearchword()) {
						tagsList.put(subject, tag);
					}
				}
				rofSubjectList.add(subject);
			}
		}
		tags = ImmutableMap.copyOf(tagsList);
		return ImmutableSet.copyOf(rofSubjectList);
	}

	/**
	 * Use to fetch formations' object from ROF.
	 * Now, we hard coding this part due to the non knowledge of the method.
	 * But, we project to do it in the next iteration.
	 * Set a ImmutableList of Formation
	 * @return 
	 */
	private ImmutableSet<Formation> fetchFormations() {
		List<Mention> mentionList = new ArrayList<>();
		List<String> keysFormationList = new ArrayList<>();
		List<Formation> formationList = new ArrayList<>();
		List <String> refProgram = new ArrayList<>();
		List<Subject> subjectsList= new ArrayList<>();
		Querier querier = new Querier();
		/*
		String predicate = "../../root/Mention";

		try {
			mentionList = querier.getMentions(predicate);
		} catch (StandardException e) {
			throw new IllegalStateException(e);
		}
		for (Mention m : mentionList) {
			if (m.getName().getValue().getFr().getValue().contains("Informatique")) {
				keysFormationList.add(m.getMentionID());
			}
			if (m.getName().getValue().getFr().getValue().contains("Mathématiques")) {
				keysFormationList.add(m.getMentionID());
			}
		}
		 */
		if (keysFormationList.isEmpty()) {
			/*keysFormationList.add("FRUAI0750736TPRMEA2MIE");
			keysFormationList.add("FRUAI0750736TPRMEA3IDO");
			keysFormationList.add("FRUAI0750736TPRMEA3INF");
			keysFormationList.add("FRUAI0750736TPRMEA3MATH");
			keysFormationList.add("FRUAI0750736TPRMEA5MAP");
			keysFormationList.add("FRUAI0750736TPRMEA5STI");
			keysFormationList.add("FRUAI0750736TPRMEA5STM");*/
			keysFormationList.add("FRUAI0750736TPRMEAID");
		}

		for (String key : keysFormationList) {
			Mention mention;

			try {
				mention = querier.getMention(key);
			} catch (StandardException e) {
				throw new IllegalStateException(e);
			}
			refProgram = mention.getStructure().getValue().getRefProgram();

			for(String refProg : refProgram) {
				Program program;
				try {
					program = querier.getProgram(refProg);

				} catch (StandardException e) {
					throw new IllegalStateException(e);
				}

				Formation formation;
				try {
					formation = createFormation(program);

				} catch (Exception e) {
					throw new IllegalStateException(e);
				}
				subjectsList.addAll(fetchSubjects(program, formation));
				formationList.add(formation);
			}	
		}
		subjects = ImmutableSet.copyOf(subjectsList);
		return ImmutableSet.copyOf(formationList);
	}


	/**
	 * Use to get department attribute
	 * @return department
	 */
	public Department getDepartment() {
		return department;
	}

	/**
	 * This method enables us to fetch the OrgUnit corresponding 
	 * to the MIDO Department (thanks to its key) and create the Department
	 * object 
	 * @return Department 
	 * @throws StandardException 
	 */
	private Department fetchDepartment() {
		Querier querier = new Querier();
		String key = "FRUAI0750736TOU0755233F";
		OrgUnit orgUnit;

		try {
			orgUnit = querier.getOrgUnit(key);
		} catch (StandardException e) {
			throw new IllegalStateException(e);
		}

		Department MIDO = Department.factory(orgUnit.getOrgUnitName().getValue());

		return MIDO;
	}

	/**
	 * Use to get subjects attribute
	 * Set subjects
	 */
	public ImmutableSet<Subject> getSubjects() {
		return subjects;
	}

	/**
	 * Use to get tags attribute
	 * Set tags
	 */
	public ImmutableMap<Subject,String> getTags() {
		return tags;
	}

	/**
	 * Use to get formations attribute
	 * Set formations
	 */
	public ImmutableSet<Formation> getFormations() {
		return formations;
	}

	/**
	 * Use to get teachers attribute
	 * Set teachers
	 */
	public ImmutableMap<String, Teacher> getTeachers() {
		return teachers;
	}


	/**
	 * This method enables to create an object of type Subject starting from an object of type Course.
	 * For the moment, we don't deal with the managing teacher
	 * @param course of type Course
	 * @param A Formation
	 * @return an object of type Subject
	 * @author marcellinodour, Raphda and TajouriSarra 
	 * @throws IllegalStateException
	 */
	private static Subject createSubject(Course course, Formation formation) throws IllegalStateException{
		Querier querier = new Querier();
		Person person;

		Subject subject = new Subject(course.getCourseName().getValue().getFr().getValue(), 0);

		if (course.getEcts() != null) {
			subject.setCredit(Double.parseDouble(course.getEcts().getValue()));
		}

		subject.setLevel(formation);

		try {
			if (course.getContacts() == null) {
				subject.setResponsible(new Teacher());
			}
			else {
				person = querier.getPerson(course.getContacts().getValue().getRefPerson().get(0));
				Teacher t = createTeacher(person);
				subject.setResponsible(t);
			}

		} catch (StandardException e) {
			throw new IllegalStateException(e);
		}

		return subject;
	}

	/**
	 * This method enables to create an object of type Teacher starting from an object of type Person.
	 * @param person of type Person
	 * @return an object of type Teacher
	 * @author marcellinodour and Raphda
	 */
	private static Teacher createTeacher(Person person) {
		Teacher teacher = new Teacher();

		if(person.getContactData().getValue().getEmail() == null) {
			teacher.setAddress("nan");
		}
		else {
			teacher.setAddress(person.getContactData().getValue().getEmail().getValue());
		}

		if(person.getContactData().getValue().getTelephone() == null) {
			teacher.setPhone("nan");
		}
		else {
			teacher.setPhone(person.getContactData().getValue().getTelephone().getValue());
		}

		teacher.setFirstName(person.getGivenName().getValue());
		teacher.setLastName(person.getFamilyName().getValue());

		return teacher;
	}

	/**
	 * This method enables to create an object of type Formation (Licence or Master) starting from 
	 * an object of type Program.
	 * @param program of type Program
	 * @return an object of type Licence or Master depending on the formation's level
	 * @author TajouriSarra
	 */
	private static Formation createFormation (Program program) {
		int level = 0;
		if (program.getProgramName().getValue().getFr().getValue().contains("L1")) {
			level = 1;
		}
		if (program.getProgramName().getValue().getFr().getValue().contains("L2")) {
			level = 2;
		}
		if (program.getProgramName().getValue().getFr().getValue().contains("L3")) {
			level = 3;
		}
		if (program.getProgramName().getValue().getFr().getValue().contains("M1")) {
			level = 4;
		}
		if (program.getProgramName().getValue().getFr().getValue().contains("M2")) {
			level = 5;
		}

		if (level <= 3) {
			Licence licence = new Licence(program.getProgramName().getValue().getFr().getValue(), level);
			return licence;
		}

		Master master = new Master(program.getProgramName().getValue().getFr().getValue(), level);
		return master;
	}

}


