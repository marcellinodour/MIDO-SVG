package com.github.cocolollipop.mido_svg.database;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.github.cocolollipop.mido_svg.university.components.Formation;
import com.github.cocolollipop.mido_svg.university.components.Subject;

import ebx.ebx_dataservices.StandardException;
/**
 * This class aim to run several tests from {@link RofDatabase}
 * @author marcellinodour, Raphda and TajouriSarra
 *
 */
class RofDatabaseTest {

	/**
	 * Fetch formations and department data from Rof and
	 *  make sure that it corresponds 
	 * for the expected
	 * 
	 * @throws Exception
	 */
	@Test
	void testRof() throws Exception {
		RofDatabase rof = RofDatabase.initialize();
		ArrayList<String> expected = new ArrayList<>();
		ArrayList<String> actual = new ArrayList<>();

		assertEquals("DEP MATHS INFO DECIS DES ORG", rof.getDepartment().getNomDepartement());

		expected.add("M1 Affaires internationales et développement ");	               
		expected.add("M2 parcours Affaires internationales (212)");	 
		expected.add("M2 parcours Développement durable et responsabilités des organisations (239)");	 
		expected.add("M2 parcours Développement durable et responsabilités des organisations (293) Professionnel et EMP");	 
		expected.add("M2 parcours Diagnostic économique international (211)");
		expected.add("M2 parcours Economie internationale et développement (111)");
		expected.add("M2 parcours Peace studies (127)");
		expected.add("M2 parcours Développement durable et organisations (DEP)");
		expected.add("M2 parcours Supply-chain internationale (217)");

		for (Formation f : rof.getFormations()) {
			actual.add(f.getFullName());
		}

		assertEquals(actual,expected);
	}

	/**
	 * Fetch the M1 Affaires internationales'subjects from Rof and
	 * make sure that it corresponds 
	 * for the expected
	 * 
	 * @throws Exception
	 */
	@Test
	void testSubjectsM1Affaires() throws Exception{
		RofDatabase rof = RofDatabase.initialize();
		ArrayList<String> actual = new ArrayList<>();
		boolean containAnglais = false, containStage = false, containDev = false;

		for (Subject s : rof.getSubjects()) {
			if (s.getLevel().getFullName().equals("M1 Affaires internationales et développement ")) {
				actual.add(s.getTitle());
			}
		}

		assertTrue(actual.size() < 100);// actual.size() > 30);

		for (String c_test : actual) {
			if(c_test.contains("Anglais")) {
				containAnglais = true;
			}

			if(c_test.contains("Stage")) {
				containStage = true;
			}

			if(c_test.contains("développement")) {
				containDev = true;
			}
		}

		assertTrue(containAnglais);
		assertTrue(containStage);
		assertTrue(containDev);
	}

}
