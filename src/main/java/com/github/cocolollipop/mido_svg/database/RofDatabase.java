package com.github.cocolollipop.mido_svg.database;

import static com.google.common.base.Verify.verify;

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
	private ImmutableList<Formation> formations;

	/* We chose to use a Set for the subjects'list because
	 * we noticed that there are many duplicates
	 * in the Rof database
	 */
	private ImmutableSet<Subject> subjects;
	private ImmutableList<String> tags;
	private ImmutableMap<String, Teacher> teachers;

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
	private List<Program> childProgram (Program program) throws StandardException{
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
		List<String> refProgram = program.getProgramStructure().getValue().getRefProgram();

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
				Subject s;
				try {
					s = createSubject(course, formation);
				} catch (Exception e) {
					throw new IllegalStateException(e);
				}

				rofSubjectList.add(s);
			}
		}
		return ImmutableSet.copyOf(rofSubjectList);
	}

	/**
	 * Use to fetch formations' object from ROF.
	 * Now, we hard coding this part due to the non knowledge of the method.
	 * But, we project to do it in the next iteration.
	 * Set a ImmutableList of Formation
	 * @return 
	 */
	private ImmutableList<Formation> fetchFormations() {
		List<String> keysFormationList = new ArrayList<>();
		List<Formation> formationList = new ArrayList<>();
		List <String> refProgram = new ArrayList<>();
		List<Subject> subjectsList= new ArrayList<>();

		//keysFormationList.add("FRUAI0750736TPRMEA2MIE");
		//keysFormationList.add("FRUAI0750736TPRMEA3IDO");
		keysFormationList.add("FRUAI0750736TPRMEA3INF");
		keysFormationList.add("FRUAI0750736TPRMEA3MATH");
		//keysFormationList.add("FRUAI0750736TPRMEA5MAP");
		//keysFormationList.add("FRUAI0750736TPRMEA5STI");
		//keysFormationList.add("FRUAI0750736TPRMEA5STM");

		for (String key : keysFormationList) {
			Querier querier = new Querier();
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
		return ImmutableList.copyOf(formationList);
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
		Department MIDO = new Department(orgUnit.getOrgUnitName().getValue());

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
	public ImmutableList<String> getTags() {
		return tags;
	}

	/**
	 * Use to get formations attribute
	 * Set formations
	 */
	public ImmutableList<Formation> getFormations() {
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
	private static Subject createSubject(Course course, Formation formation) throws IllegalStateException {
		Subject subject = new Subject("", 0);

		subject.setCredit(Double.parseDouble(course.getEcts().getValue()));
		subject.setLevel(formation);

		Teacher t = new Teacher();

		subject.setResponsible(t);
		subject.setTitle(course.getCourseName().getValue());

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

		teacher.setAddress(person.getContactData().getValue().getEmail().getValue());
		teacher.setPhone(person.getContactData().getValue().getTelephone().getValue());
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
	private static Formation createFormation (Program program) throws VerifyException{
		String level = program.getIdent().getValue().substring(3, 4);
		//Verify.verify(Integer.class.isInstance(level));

		if (Integer.parseInt(level) <= 3) {
			Licence licence = new Licence(program.getProgramName().getValue(), Integer.parseInt(level));
			return licence;
		}
		Master master = new Master(program.getProgramName().getValue(), Integer.parseInt(level));
		return master;

	}

	public static void main (String[] args) throws Exception {
		RofDatabase test = RofDatabase.initialize();
		for (Formation s : test.formations) {
			System.out.println(s.getFullName());
		}
		for (Subject s : test.subjects) {
			System.out.println(s.getTitle());
			//System.out.print(" "+ s.getLevel().getFullName());
			//System.out.println();
		}
	}

}


