package com.github.cocolollipop.mido_svg.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import com.github.cocolollipop.mido_svg.paper.FactoryPaper;
import com.github.cocolollipop.mido_svg.paper.Paper;
import com.github.cocolollipop.mido_svg.svg_generator.Settings;
import com.github.cocolollipop.mido_svg.university.components.Department;
import com.github.cocolollipop.mido_svg.university.components.Formation;
import com.github.cocolollipop.mido_svg.university.components.Licence;
import com.github.cocolollipop.mido_svg.university.components.Master;
import com.github.cocolollipop.mido_svg.university.components.Subject;
import com.github.cocolollipop.mido_svg.university.components.Teacher;
import com.github.cocolollipop.mido_svg.xml.jaxb.model.Tag;

/**
 * This class is the current database of the application
 *
 */

public class DataBase {

	private Department department;

	private List<Formation> formations;

	private Map<String, Formation> formationsMap;

	private Map<String, Subject> mapSubjects; // used for tags

	private Paper paper;

	private Settings settings;

	private List<Subject> subjects;

	private List<String> tags;

	private Map<String, Teacher> teachers;

	private List<String> users;

	public DataBase() {
		// default settings
		this.settings = new Settings(true, true, true, true, true, true, true, "A4");
		this.teachers = new HashMap<>();
		initTeachers();
		this.formationsMap = new HashMap<>();
		this.formations = new LinkedList<>();
		this.subjects = new ArrayList<>();
		this.tags = new ArrayList<>();
		try {
			initFormations();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initUsers();
		initSubjects();
		setPaper(FactoryPaper.TypeFormat.A4, 0, 0);
		initDepartment();
		FillSubjectListInFormation();

	}

	public DataBase(Settings settings) {
		this.teachers = new HashMap<>();
		initTeachers();
		this.formationsMap = new HashMap<>();
		this.formations = new LinkedList<>();
		this.subjects = new ArrayList<>();
		this.tags = new ArrayList<>();
		try {
			initFormations();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initUsers();
		initSubjects();
		this.settings = settings;
		setPaper(this.settings.getFormat(), this.settings.getWidth(), this.settings.getHeight());
		initDepartment();
		FillSubjectListInFormation();

	}

	/**
	 * This function fills the subjects list of each formation with subjects
	 * assigned to this formation
	 *
	 */
	public void FillSubjectListInFormation() {

		(formationsMap.get("M1MIAGE")).fillsubjectList(subjects);
		(formationsMap.get("L3MIAGEApp")).fillsubjectList(subjects);
		(formationsMap.get("M1MIAGEApp")).fillsubjectList(subjects);
		(formationsMap.get("L3MIAGE")).fillsubjectList(subjects);

	}

	// LOGGERS
	// public static final Logger LOGGER =
	// LoggerFactory.getLogger(DataBase.class);

	public Department getDepartment() {
		return department;
	}

	public List<Formation> getFormations() {
		return formations;
	}

	public Paper getPaper() {
		return paper;
	}

	public Map<String, Subject> getSubjects() {
		return mapSubjects;
	}

	public List<String> getTags() {
		return this.tags;

	}

	public Map<String, Teacher> getTeachersByName() {
		return teachers;
	}

	//////////////// GETTERS AND SETTERS /////////////

	/**
	 * Initialize Department
	 */
	public void initDepartment() {
		Department MIDO = Department.factory("MIDO");
		MIDO.setTitle("MIDO");
		this.setDepartment(MIDO);
	}

	/**
	 * Initialize formations
	 */
	public void initFormations() {

		// The equivalent of the function but with the data from de XML file
		/*
		 * XMLMain myTestXMLMain = new XMLMain();
		 * myTestXMLMain.fillFormationsXML(this.formationsMap);
		 */

		// L3MIAGE
		Licence L3MIAGE = new Licence("L3 MIAGE", 3, 250, 70);
		L3MIAGE.setTeacher(teachers.get("Mayag"));
		L3MIAGE.setFullNameWithLink(
				"http://formations.dauphine.fr/offre/fr-FR/fiche/A3INF/programme//#FRUAI0750736TPRPRA3INF");
		L3MIAGE.setAdmisssion("selection sur dossier");

		// L3MIAGEApp
		Licence L3MIAGEApp = new Licence("L3 MIAGE App", 3, 750, 70);
		L3MIAGEApp.setTeacher(teachers.get("Pigozzi"));
		L3MIAGEApp.setFullNameWithLink(
				"http://formations.dauphine.fr/offre/fr-FR/fiche/A3INF/programme//#FRUAI0750736TPRPRA3INFAPP");
		L3MIAGEApp.setAdmisssion("selection sur entretien");

		// M1MIAGE
		Master M1MIAGE = new Master("M1 MIAGE", 4, 250, 150);
		M1MIAGE.setTeacher(teachers.get("Cailloux"));
		M1MIAGE.setFullNameWithLink(
				"http://formations.dauphine.fr/offre/fr-FR/fiche/A5STI/programme//#FRUAI0750736TPRPRA4MIAI");

		Master M1MIAGEApp = new Master("M1 MIAGE App", 4, 750, 150);
		Master M2MIAGEIF = new Master("M2 MIAGE IF", 5, 100, 300);
		Master M2MIAGEID = new Master("M2 MIAGE ID", 5, 250, 300);
		Master M2MIAGESTIN = new Master("M2 MIAGE STIN", 5, 400, 300);
		Master M2MIAGEIFApp = new Master("M2 MIAGE IF App", 5, 600, 300);
		Master M2MIAGEIDApp = new Master("M2 MIAGE ID App", 5, 750, 300);
		Master M2MIAGESTINApp = new Master("M2 MIAGE STIN App", 5, 900, 300);

		// List of formations
		this.formations = new LinkedList<>();
		this.formations.add(L3MIAGE);
		this.formations.add(L3MIAGEApp);
		this.formations.add(M1MIAGE);
		this.formations.add(M1MIAGEApp);
		this.formations.add(M2MIAGEIF);
		this.formations.add(M2MIAGEID);
		this.formations.add(M2MIAGESTIN);
		this.formations.add(M2MIAGEIFApp);
		this.formations.add(M2MIAGEIDApp);
		this.formations.add(M2MIAGESTINApp);

		// Available formation
		L3MIAGE.addAvailableFormation(M1MIAGE);
		L3MIAGEApp.addAvailableFormation(M1MIAGEApp);
		M1MIAGE.addAvailableFormation(M2MIAGESTIN);
		M1MIAGE.addAvailableFormation(M2MIAGEIF);
		M1MIAGE.addAvailableFormation(M2MIAGEID);
		M1MIAGEApp.addAvailableFormation(M2MIAGESTINApp);
		M1MIAGEApp.addAvailableFormation(M2MIAGEIDApp);
		M1MIAGEApp.addAvailableFormation(M2MIAGEIFApp);

		this.formationsMap.put("L3MIAGE", L3MIAGE);
		this.formationsMap.put("M1MIAGE", M1MIAGE);
		this.formationsMap.put("L3MIAGEApp", L3MIAGEApp);
		this.formationsMap.put("M1MIAGEApp", M1MIAGEApp);

	}

	/**
	 * Initialize subjects
	 */
	public void initSubjects() {
		this.mapSubjects = new HashMap<>();

		// The equivalent of the function but with the data from de XML file
		// XMLMain myTestXMLMain = new XMLMain();
		// myTestXMLMain.fillSubjectsXML(this.mapSubjects);

		Subject proba = new Subject("Probabilités et Statistiques", teachers.get("Mayag"), 3, 350, 70);
		Subject java = new Subject("POO Java", teachers.get("Cailloux"), 3, 350, 85);
		Subject logique = new Subject("Logique", teachers.get("Pigozzi"), 3, 350, 70);
		Subject math = new Subject("Mathematiques", teachers.get("Pigozzi"), 3, 0, 0);

		mapSubjects.put("proba", proba);
		mapSubjects.put("java", java);
		mapSubjects.put("logique", logique);

		logique.setLevel(formationsMap.get("M1MIAGE"));
		math.setLevel(formationsMap.get("L3MIAGEApp"));
		proba.setLevel(formationsMap.get("L3MIAGE"));
		java.setLevel(formationsMap.get("L3MIAGE"));

		subjects.add(proba);
		subjects.add(java);
		subjects.add(logique);
		subjects.add(math);

		// Add prerequisites of each subject

		logique.addListOfPrerequisites(proba); // It means that logique has
												// proba as prerequisites

	}

	/**
	 * Initialize teachers
	 */
	public void initTeachers() {

		// The equivalent of the function but with the data from de XML file
		//
		// XMLMain myTestXMLMain = new XMLMain();
		// myTestXMLMain.fillTeachersXML(this.teachers);

		Teacher Mayag = new Teacher("Mayag", "Brice ", 150, 70);
		Teacher Pigozzi = new Teacher("Pigozzi", "Gabriella ", 650, 70);
		Teacher Cailloux = new Teacher("Cailloux", "Olivier ", 150, 150);
		this.teachers.put("Cailloux", Cailloux);
		this.teachers.put("Pigozzi", Pigozzi);
		this.teachers.put("Mayag", Mayag);

	}

	public boolean isUser(String username) {
		if (users.contains(username)) {
			return true;
		}
		return false;
	}

	/////////// MANAGE////////////
	/**
	 * To have the number of child of each formation
	 */
	public void nbOfChildOfEachFormation() {

		for (int i = 0; i < this.getFormations().size(); i++) {
			System.out.println(
					"Pour l\'annee" + this.getFormations().get(i).getGrade() + this.getFormations().get(i).getFullName()
							+ " a " + this.getFormations().get(i).getAvailableFormations().size());

			if (this.getFormations().get(i).getAvailableFormations().size() == 0) {
				System.out.println("Pas de formation accessible");
				// Logger.info("Pas de formation accessible");
			}
			for (int j = 0; j < this.getFormations().get(i).getAvailableFormations().size(); j++) {
				System.out.println("Les formations accessibles sont:"
						+ this.getFormations().get(i).getAvailableFormations().get(j).getFullName());
			}
		}
	}

	public void setDepartment(Department department) {
		if (this.department == null) {
			this.department = Department.factory("");
		}
		this.department = department;
	}

	public void setFormations(List<Formation> formations) {
		if (this.formations == null) {
			this.formations = new LinkedList<>();
		}
		this.formations = formations;
	}

	///////////// INITIALIZE///////////////
	/**
	 * Initialize Paper
	 */
	public void setPaper(Enum<?> format, int x, int y) {
		FactoryPaper facp = new FactoryPaper();
		if (format == FactoryPaper.TypeFormat.A3 || format == FactoryPaper.TypeFormat.A4) {
			this.paper = facp.getPaper(format);
		} else {
			this.paper = facp.getPaper(format, x, y);
		}
	}

	///////// END GETTERS AND SETTERS //////

	public void setPaper(Paper paper) {
		this.paper = paper;

	}

	public void setSubjects(Map<String, Subject> subjects) {
		if (this.mapSubjects == null) {
			this.mapSubjects = new HashMap<>();
		}
		this.mapSubjects = subjects;
	}

	/**
	 * Store tags into the mapSubjects
	 *
	 * @param listOfTags
	 */
	public void setTags(List<Tag> listOfTags) {
		for (Tag tag : listOfTags) {
			Set<String> sujets = tag.getSubjects();
			for (String sujet : sujets) {
				this.mapSubjects.get(sujet).addTag(tag);
			}
		}

	}

	public void setTeachers(Map<String, Teacher> teachers) {
		if (this.teachers == null) {
			this.teachers = new HashMap<>();
		}
		this.teachers = teachers;
	}

	private void initUsers() {
		this.users = new ArrayList<>();
		this.users.add("ikram");
		this.users.add("romain");
		this.users.add("jules");
		this.users.add("cocolollipop");
		this.users.add("ocailloux");

	}

}
