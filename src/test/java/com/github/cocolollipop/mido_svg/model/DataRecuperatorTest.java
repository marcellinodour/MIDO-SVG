package com.github.cocolollipop.mido_svg.model;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Iterator;
import java.util.List;

import com.github.cocolollipop.mido_svg.university.components.Subject;

import ebx.ebx_dataservices.StandardException;

/**
 * 
 * This class enables to test the class ObjectTransformer
 * @author brulej
 *
 */

public class DataRecuperatorTest {

	/**
	 * Tests to see if the method getSubject create correct objects
	 * @throws StandardException 
	 * 
	 * @result the first Subject collected should be "Introduction à la microéconomie" and the second "Anglais 1"
	 * with their credits and teacher attached
	 */
	@Test
	public void getSubjectsTest() throws StandardException {
		List<Subject> list = DataRecuperator.getSubjects("FRUAI0750736TPRCP1ALUEC1");
		Assertions.assertEquals(list.size(), 2);
		
		Iterator<Subject> l = list.iterator();
		
		Subject s1 = l.next();
		Assertions.assertEquals("Introduction à la microéconomie", s1.getTitle());
		Assertions.assertEquals(4, s1.getCredit());
		Assertions.assertEquals("TAN", s1.getResponsible().getLastName());
		
		Subject s2 = l.next();
		Assertions.assertEquals("Anglais 1", s2.getTitle());
		Assertions.assertEquals(2, s2.getCredit());
		Assertions.assertEquals("TAN", s2.getResponsible().getLastName());
	}


}
