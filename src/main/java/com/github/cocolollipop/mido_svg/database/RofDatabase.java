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
	private Settings settings;
	private ImmutableList<Subject> subjects;
	private ImmutableList<String> tags;
	private ImmutableMap<String, Teacher> teachers;
	private ImmutableList<String> users;

	/**
	 * Initialize an immutable Database with ROF informations.
	 * @return RofDatabase
	 * @throws StandardException
	 */
	public static RofDatabase initialize() throws StandardException {
		return new RofDatabase();
	}

	private RofDatabase() throws StandardException {

		this.settings = new Settings(true, true, true, true, true, true, true, "A4");

		QueriesHelper.setDefaultAuthenticator();
		setDepartment(fetchDepartment());
		setFormations(fetchFormations());
		setSubjects(fetchSubjects());
		setTeachers(fetchTeachers());
		setUsers(fetchUsers());
	}

	/**
	 * Set teachers attribute
	 * @param teachersList
	 */
	private void setTeachers(ImmutableMap<String, Teacher> teachersList) {
		this.teachers = teachersList;
	}

	/**
	 * Set subjects attribute
	 * @param subjectsList
	 */
	private void setSubjects(ImmutableList<Subject> subjectsList) {
		this.subjects = subjectsList;
	}

	/**
	 * Set formations attribute
	 * @param formationsList
	 */
	private void setFormations(ImmutableList<Formation> formationsList) {
		this.formations = formationsList;
	}

	/**
	 * Set users attribute
	 * @param usersList
	 */
	private void setUsers(ImmutableList<String> usersList) {
		this.users = usersList;
	}

	/**
	 * Use to get department attribute
	 * @return department
	 */
	public Department getDepartment() {
		return department;
	}

	/**
	 * Set department attribute
	 * @param department
	 */
	private void setDepartment(Department department) {
		this.department = department;
	}

	/**
	 * Use to get subjects attribute
	 * @return subjects
	 */
	public List<Subject> getSubjects() {
		return subjects;
	}

	/**
	 * Use to get settings attribute
	 * @return settings
	 */
	public Settings getSettings() {
		return settings;
	}

	/**
	 * Use to get tags attribute
	 * @return tags
	 */
	public List<String> getTags() {
		return tags;
	}

	/**
	 * Use to get formations attribute
	 * @return formations
	 */
	public List<Formation> getFormations() {
		return formations;
	}

	/**
	 * Use to get teachers attribute
	 * @return teachers
	 */
	public Map<String, Teacher> getTeachers() {
		return teachers;
	}

	/**
	 * Use to get users attribute
	 * @return users
	 */
	public ImmutableList<String> getUsers() {
		return users;
	}

	/**
	 * Use to fetch departments' object from ROF.
	 * Now, we hard coding this part due to the non knowledge of the method.
	 * But, we project to do it in the next iteration.
	 * @return a Department
	 */
	private Department fetchDepartment() {
		Department MIDO = new Department("MIDO");
		return MIDO;
	}

	/**
	 * Use to fetch formations' object from ROF.
	 * Now, we hard coding this part due to the non knowledge of the method.
	 * But, we project to do it in the next iteration.
	 * @return a ImmutableList of Formation
	 */
	private ImmutableList<Formation> fetchFormations() {
		List<Formation> rofFormationList = new ArrayList<>();
		rofFormationList.add(new Master("M1 MIAGE App", 4));

		return ImmutableList.copyOf(rofFormationList);
	}

	/** 
	 * This method enables to get all the subjects in each formation from ROF.
	 * Now, we only fetch subjects of M1 MIAGE APP by a hard code.
	 * But, when we will be able to fetch all Programm ID,
	 * we automatically fetch it.
	 * @author marcellinodour and Raphda
	 * @throws StandardException 
	 * @return a ImmutableList of Subject
	 **/
	private ImmutableList<Subject> fetchSubjects() throws StandardException {
		List<String> keys = new ArrayList<>();
		List<Subject> rofSubjectList = new ArrayList<>();
		keys.add("FRUAI0750736TPRCPA4AMIA-100-S1L1");
		keys.add("FRUAI0750736TPRCPA4AMIA-100-S2L1");
		keys.add("FRUAI0750736TPRCPA4AMIA-100-S2L2");
		keys.add("FRUAI0750736TPRCPA4AMIAS1L2");

		for(String key : keys) {
			Querier querier = new Querier();

			Program program = querier.getProgram(key);
			List<String> courseRefs = program.getProgramStructure().getValue().getRefCourse();

			for(String courseRef : courseRefs) {
				final Course course = querier.getCourse(courseRef);
				Subject s = createSubject(course, this.formations.get(0));
				rofSubjectList.add(s);
			}	
		}
		return ImmutableList.copyOf(rofSubjectList);
	}

	/**
	 * This method use subject List (previously fetch) to fetch all teachers.
	 * @return ImmutableMap of teachers
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
	 * This method return a ImmutableList of users.
	 * We plan to delete it in a next iteration (in parallel with the call of this method in the GUI). 
	 * @return a ImmutableList of users
	 */
	private ImmutableList<String> fetchUsers() {
		List<String> usersList = new ArrayList<>();

		usersList.add("ikram");
		usersList.add("romain");
		usersList.add("jules");
		usersList.add("cocolollipop");
		usersList.add("ocailloux");
		return ImmutableList.copyOf(usersList);
	}

	/**
	 * This method enables to create an object of type Subject starting from an object of type Course.
	 * @param course of type Course
	 * @param A Formation
	 * @return an object of type Subject
	 * @author marcellinodour and Raphda
	 * @throws StandardException 
	 */
	private static Subject createSubject(Course course, Formation formation) throws StandardException {
		Subject subject = new Subject("", 0);

		subject.setCredit(Double.parseDouble(course.getEcts().getValue()));
		subject.setLevel(formation);

		Querier querier = new Querier();
		Teacher t = new Teacher();

		if (course.getManagingTeacher() != null) {
			Person person = querier.getPerson(course.getManagingTeacher().getValue());
			t = createTeacher(person);
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


