package com.github.cocolollipop.mido_svg.database;

import java.awt.Point;
import java.util.List;
import java.util.Set;

import com.github.cocolollipop.mido_svg.university.components.Formation;
import com.github.cocolollipop.mido_svg.university.components.Subject;
import com.github.cocolollipop.mido_svg.university.components.Teacher;
import com.github.cocolollipop.mido_svg.xml.jaxb.model.Tag;

import ebx.ebx_dataservices.StandardException;
import schemas.ebx.dataservices_1.CourseType.Root.Course;
import schemas.ebx.dataservices_1.PersonType.Root.Person;

/**
 * 
 * This class enables to make a correspondence between different classes and then create objects for the project MIDO-SVG
 * @author camillelanglois3
 * @date 19/04/2020
 *
 */
public class ObjectTransformer {
	
	/**
	 * This method enables to create an object of type Subject starting from an object of type Course
	 * @param course of type Course
	 * @param A Formation
	 * @return an object of type Subject
	 * @author marcellinodour and Raphda
	 * @throws StandardException 
	 */
	public static Subject createSubject(Course course, Formation formation) throws StandardException {
		Subject subject = new Subject("", 0);
		
		subject.setCredit(Double.parseDouble(course.getEcts().getValue()));
		subject.setLevel(formation);
		
		Querier querier=new Querier();
		Person person = querier.getPerson(course.getManagingTeacher().getValue());
		Teacher t = createTeacher(person);
		subject.setResponsible(t);
		
		subject.setTitle(course.getCourseName().getValue());
		
		return subject;
	}
	
	/**
	 * This method enables to create an object of type Teacher starting from an object of type Person
	 * @param person of type Person
	 * @return an object of type Teacher
	 * @author marcellinodour and Raphda
	 */
	public static Teacher createTeacher(Person person) {
		Teacher teacher = new Teacher();
		
		teacher.setAddress(person.getContactData().getValue().getEmail().getValue());
		teacher.setPhone(person.getContactData().getValue().getTelephone().getValue());
		teacher.setFirstName(person.getGivenName().getValue());
		teacher.setLastName(person.getFamilyName().getValue());
		
		return teacher;
	}

}
