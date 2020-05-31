package com.github.cocolollipop.mido_svg.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;

import com.github.cocolollipop.mido_svg.svg_generator.Settings;
import com.github.cocolollipop.mido_svg.university.components.Department;
import com.github.cocolollipop.mido_svg.university.components.Formation;
import com.github.cocolollipop.mido_svg.university.components.Master;
import com.github.cocolollipop.mido_svg.university.components.Subject;
import com.github.cocolollipop.mido_svg.university.components.Teacher;
import com.github.cocolollipop.mido_svg.university.components.Licence;

import com.google.common.base.Verify;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import ebx.ebx_dataservices.StandardException;
import schemas.ebx.dataservices_1.CourseType.Root.Course;
import schemas.ebx.dataservices_1.PersonType.Root.Person;
import schemas.ebx.dataservices_1.ProgramType.Root.Program;


/**
 * This class fetch informations from Dauphine's DataBase 
 * @author marcellinodour and Raphda
 * @date 02/05/2020
 */
public class RofDatabase {

	private Department department;
	private ImmutableList<Formation> formations;
	private ImmutableList<Subject> subjects;
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
		this.subjects = fetchSubjects();
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
	 * This method enables to get all the subjects in each formation from ROF.
	 * Now, we only fetch subjects of M1 MIAGE APP by a hard code.
	 * But, when we will be able to fetch all Programm ID,
	 * we automatically fetch it.
	 * @author marcellinodour and Raphda
	 * Set a ImmutableList of Subject
	 * @return 
	 * @throws Exception 
	 **/
	private ImmutableList<Subject> fetchSubjects() throws IllegalStateException {
		List<String> keys = new ArrayList<>();
		List<Subject> rofSubjectList = new ArrayList<>();
		keys.add("FRUAI0750736TPRCPA4AMIA-100-S1L1");
		keys.add("FRUAI0750736TPRCPA4AMIA-100-S2L1");
		keys.add("FRUAI0750736TPRCPA4AMIA-100-S2L2");
		keys.add("FRUAI0750736TPRCPA4AMIAS1L2");

		for(String key : keys) {
			Querier querier = new Querier();

			Program program;

			try {
				program = querier.getProgram(key);
			} catch (StandardException e) {
				throw new IllegalStateException(e);
			}

			List<String> courseRefs = program.getProgramStructure().getValue().getRefCourse();

			for(String courseRef : courseRefs) {

				Course course;

				try {
					course = querier.getCourse(courseRef);
				} catch (StandardException e) {
					throw new IllegalStateException(e);
				}

				Subject s;

				try {
					s = createSubject(course, this.formations.get(0));
				} catch (Exception e) {
					throw new IllegalStateException(e);
				}

				rofSubjectList.add(s);
			}	
		}
		return ImmutableList.copyOf(rofSubjectList);
	}
	
	

	/**
	 * Use to fetch formations' object from ROF.
	 * Now, we hard coding this part due to the non knowledge of the method.
	 * But, we project to do it in the next iteration.
	 * Set a ImmutableList of Formation
	 * @return 
	 */
	private ImmutableList<Formation> fetchFormations() {
		List<String> keys = new ArrayList<>();
		List<Formation> rofFormationList = new ArrayList<>();

		rofFormationList.add(new Master("M1 MIAGE App", 4));
		
		keys.add(" ");
		keys.add(" ");
		

		return ImmutableList.copyOf(rofFormationList);
	}



	/**
	 * Use to get department attribute
	 * @return department
	 */
	public Department getDepartment() {
		return department;
	}

	/**
	 * Use to fetch departments' object from ROF.
	 * Now, we hard coding this part due to the non knowledge of the method.
	 * But, we project to do it in the next iteration.
	 * Set a Department
	 * @return 
	 */
	private Department fetchDepartment() {

		Department MIDO = new Department("MIDO");

		return MIDO;
	}

	/**
	 * Use to get subjects attribute
	 * Set subjects
	 */
	public ImmutableList<Subject> getSubjects() {
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
	 * @param course of type Course
	 * @param A Formation
	 * @return an object of type Subject
	 * @author marcellinodour and Raphda
	 * @throws Exception 
	 */
	private static Subject createSubject(Course course, Formation formation) throws IllegalStateException {
		Subject subject = new Subject("", 0);

		subject.setCredit(Double.parseDouble(course.getEcts().getValue()));
		subject.setLevel(formation);

		Querier querier = new Querier();
		Teacher t = new Teacher();

		Person person;

		if (!course.getManagingTeacher().getValue().isEmpty()){
			try {
				person = querier.getPerson(course.getManagingTeacher().getValue());
				t = createTeacher(person);
			} catch (StandardException e) {
				throw new IllegalStateException(e);
			}
		}

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

}


