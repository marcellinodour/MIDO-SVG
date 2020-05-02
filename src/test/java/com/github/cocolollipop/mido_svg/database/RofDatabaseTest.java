package com.github.cocolollipop.mido_svg.database;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import ebx.ebx_dataservices.StandardException;
/**
 * This class aim to run several tests from {@link RofDatabase}
 * @author marcellinodour and Raphda
 *
 */
class RofDatabaseTest {

	/**
	 * Fetch data test (only M1 MIAGE App)
	 * @result Second subject = "Intelligence artificielle"
	 * 		   First Departement = "MIDO"
	 * 		   First Formation = "M1 MIAGE App"
	 * 		   First Teacher = "VUILLOD FREDERIC"
	 * @author marcellinodour and Raphda
	 * @throws StandardException
	 */
	@Test
	void test() throws StandardException {
		RofDatabase rof = new RofDatabase();
		assertEquals("Programmation Objet avancée", rof.getSubjects().get(0).getTitle());
		assertEquals("MIDO", rof.getDepartment().getNomDepartement());
		assertEquals("M1 MIAGE App", rof.getFormations().get(0).getFullName());
		assertEquals("VUILLOD FREDERIC", rof.getTeachers().get("VUILLOD").getFullNameTeacher());
	}
	
	

}
