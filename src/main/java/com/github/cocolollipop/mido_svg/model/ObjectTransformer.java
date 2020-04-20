package com.github.cocolollipop.mido_svg.model;

import com.github.cocolollipop.mido_svg.BddQuerries.Querier;
import com.github.cocolollipop.mido_svg.university.components.Subject;
import com.github.cocolollipop.mido_svg.university.components.Teacher;

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
	 * @return an object of type Subject
	 * @author camillelanglois3
	 */
	public static Subject createSubject(Course course) {
		Subject subject;
		Querier querier = new Querier();
		if(course.getEcts()!=null) {
			subject.setCredit(course.getEcts().getValue());
		}
		if(course.getLevel()!=null) {
			subject.setLevel(course.getLevel().getValue());
		}
		if(course.getManagingTeacher()!=null) {
			Person person = querier.getPerson(course.getManagingTeacher().getValue());
			Teacher t = createTeacher(person);
			subject.setResponsible(t);
		}
		if(course.getCourseName()!=null) {
			subject.setTitle(course.getCourseName().getValue());
		}
		return subject;
	}
	
	/**
	 * This method enables to create an object of type Teacher starting from an object of type Person
	 * @param person of type Person
	 * @return an object of type Teacher
	 * @author camillelanglois3
	 */
	public static Teacher createTeacher(Person person) {
		Teacher teacher;
		if(person.getGivenName()!=null) {
			teacher.setFirstName(person.getGivenName().getValue());
		}
		if(person.getFamilyName()!=null) {
			teacher.setLastName(person.getFamilyName().getValue());
		}
		return teacher;
	}

}
