package com.github.cocolollipop.mido_svg.model;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Iterator;
import java.util.List;


import com.github.cocolollipop.mido_svg.BddQuerries.Querier;
import com.github.cocolollipop.mido_svg.university.components.Subject;
import com.github.cocolollipop.mido_svg.university.components.Teacher;

import ebx.ebx_dataservices.StandardException;
import schemas.ebx.dataservices_1.CourseType.Root.Course;
import schemas.ebx.dataservices_1.PersonType.Root.Person;

/**
 * 
 * This class enables to test the class ObjectTransformer
 * @author brulej
 *
 */

class ObjectTransformerTest {
	
	/**
	 * Tests to see if the method createSubject creates correct objects
	 * @throws StandardException 
	 * 
	 * @result to determinate...
	 */
	@Test
	public void createSubjectTest() throws StandardException {
		// We can't compile this method yet so we wait to run all our classes and to have access to the BDD to put real values
		Querier querier = new Querier();
		Course course = querier.getCourse("courseID");
		Subject subject = ObjectTransformer.createSubject(course);
		
		Assertions.assertEquals("nombre de crédit", subject.getCredit());
		Assertions.assertEquals("niveau", subject.getLevel());
		Assertions.assertEquals("prof", subject.getResponsible());
		Assertions.assertEquals("libelé", subject.getTitle());
	}
	
	
	
	/**
	 * Tests to see if the method createTeacher creates correct objects
	 * @throws StandardException 
	 * 
	 * @result to determinate...
	 */
	@Test
	public void createTeacherTest() throws StandardException {
		// We can't compile this method yet so we wait to run all our classes and to have access to the BDD to put real values
		Querier querier = new Querier();
		Person personne=  querier.getPerson("personID");
		Teacher teacher= ObjectTransformer.createTeacher(personne);
		
		Assertions.assertEquals("prénom de la personne", teacher.getFirstName());
		Assertions.assertEquals("nom de la personne", teacher.getLastName());
	}

}
