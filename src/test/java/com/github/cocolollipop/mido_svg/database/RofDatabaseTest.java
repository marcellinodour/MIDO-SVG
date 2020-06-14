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
 * @author marcellinodour and Raphda
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
		
		expected.add("L1 : Parcours Initial");
		expected.add("Parcours Talents");
		expected.add("L2 : Parcours Mathématiques-Informatique");
		expected.add("L2 : Parcours Mathématiques-Economie");	               
		expected.add("L3 : Parcours MIAGE Apprentissage");	 
		expected.add("L3 : Parcours MIAGE Apprentissage");	 
		expected.add("L1 : Parcours Initial");	 
		expected.add("Parcours Talents");
		expected.add("L2 : Parcours Mathématiques-Economie");
		expected.add("L2 : Parcours Mathématiques-Informatique");
		expected.add("L3 : Parcours Mathématiques-Economie-Finance-Actuariat");
		expected.add("L3 : Parcours Mathématiques-Informatique");
		for (Formation f : rof.getFormations()) {
			actual.add(f.getFullName());
		}
		expected.removeAll(actual);
		assertTrue(expected.isEmpty());
	}
	/**
	 * Fetch MIAGE App's subjects from Rof and
	 * make sure that it corresponds 
	 * for the expected
	 * 
	 * @throws Exception
	 */
	@Test
	void testSubjectsMIAGEApp() throws Exception {
		RofDatabase rof = RofDatabase.initialize();
		ArrayList<String> expected = new ArrayList<>();
		ArrayList<String> actual = new ArrayList<>();
		
		expected.add("Graphes : modélisation et algorithmes");
		expected.add("Finance d'entreprise");
		expected.add("Réseaux : infrastructures");
		expected.add("Probabilités et processus stochastiques");
		expected.add("Comptabilité analytique");
		expected.add("Anglais 6");
		expected.add("Expression écrite - communication");
		expected.add("Sociologie des organisations");
		expected.add("Bloc entreprise");
		expected.add("Ingénierie des systèmes d'information 2");
		expected.add("Anglais 5");
		expected.add("Logique");
		expected.add("Bases de données relationnelles");
		expected.add("Ingénierie des systèmes d'information 1");
		expected.add("Programmation linéaire");
		expected.add("Utilisation et programmation UNIX");
		expected.add("Analyse de données");
		expected.add("Java objet");
		
		for (Subject s : rof.getSubjects()) {
			if (s.getLevel().getFullName().equals("L3 : Parcours MIAGE Apprentissage")) {
				actual.add(s.getTitle());
			}
		}
		expected.removeAll(actual);
		assertTrue(expected.isEmpty());
	}
	
}
