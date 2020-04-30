package com.github.cocolollipop.mido_svg.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.github.cocolollipop.mido_svg.paper.FactoryPaper;
import com.github.cocolollipop.mido_svg.paper.FactoryPaper.TypeFormat;
import com.github.cocolollipop.mido_svg.paper.Paper;
import com.github.cocolollipop.mido_svg.svg_generator.Settings;
import com.github.cocolollipop.mido_svg.university.components.Department;
import com.github.cocolollipop.mido_svg.university.components.Formation;
import com.github.cocolollipop.mido_svg.university.components.Master;
import com.github.cocolollipop.mido_svg.university.components.Subject;
import com.github.cocolollipop.mido_svg.university.components.Teacher;

import ebx.ebx_dataservices.StandardException;

/**
 * This class fetch informations from Dauphine's DataBase 
 * @author marcellinodour and Raphda
 * @date 30/04/2020
 */
public class RofDatabase {

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

	//contructor
	public RofDatabase() throws StandardException {
		
		this.settings=new Settings(true, true, true, true, true, true, true, "A4");
		this.teachers = new HashMap<>();
        this.formationsMap = new HashMap<>();
        this.formations = new LinkedList<>();
        this.subjects = new ArrayList<>();
		this.setTags(new ArrayList<>());
		
		fetchDepartement();
		fetchFormations();
		fetchSubjects();
		fetchTeachers();
		fetchUsers();
		setPaper(FactoryPaper.TypeFormat.A4);
		FillSubjectListInFormation();

	}

	//getter and setter
	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public List<Subject> getSubjects() {
		return subjects;
	}


	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}
	
	public Paper getPaper() {
		return paper;
	}


	public void setPaper(Paper paper) {
		this.paper = paper;
	}


	public Settings getSettings() {
		return settings;
	}


	public void setSettings(Settings settings) {
		this.settings = settings;
	}


	public Map<String, Subject> getMapSubjects() {
		return mapSubjects;
	}


	public void setMapSubjects(Map<String, Subject> mapSubjects) {
		this.mapSubjects = mapSubjects;
	}


	public List<String> getTags() {
		return tags;
	}


	public void setTags(List<String> tags) {
		this.tags = tags;
	}


	public List<Formation> getFormations() {
		return formations;
	}


	public void setFormations(List<Formation> formations) {
		this.formations = formations;
	}


	public Map<String, Teacher> getTeachers() {
		return teachers;
	}


	public void setTeachers(Map<String, Teacher> teachers) {
		this.teachers = teachers;
	}

	//fetch methods
	private void fetchDepartement() {
		Department MIDO = new Department("MIDO");
		setDepartment(MIDO);
	}
	
	private void fetchFormations() {
		Master M1MIAGEApp = new Master("M1 MIAGE App", 4);
		formations.add(M1MIAGEApp);
		formationsMap.put("M1MIAGEApp", M1MIAGEApp);
		
	}
	
	private void fetchSubjects() throws StandardException {
		DataRecuperator.getSubjects("FRUAI0750736TPRCPA4AMIA-100-S1L1", this.formationsMap.get("M1MIAGEApp"), this.subjects);
		DataRecuperator.getSubjects("FRUAI0750736TPRCPA4AMIA-100-S2L1", this.formationsMap.get("M1MIAGEApp"), this.subjects);
		DataRecuperator.getSubjects("FRUAI0750736TPRCPA4AMIAS1L2", this.formationsMap.get("M1MIAGEApp"), this.subjects);
		DataRecuperator.getSubjects("FRUAI0750736TPRCPA4AMIA-100-S2L2", this.formationsMap.get("M1MIAGEApp"), this.subjects);
	}

	private void fetchTeachers() {
		for (Subject subject : this.subjects) {
			if (!this.teachers.containsKey(subject.getResponsible().getLastName())) {
				this.teachers.put(subject.getResponsible().getLastName(),subject.getResponsible());
			}
		}
	}

	private void fetchUsers() {
		this.users = new ArrayList<>();
		this.users.add("ikram");
		this.users.add("romain");
		this.users.add("jules");
		this.users.add("cocolollipop");
		this.users.add("ocailloux");
	}
	
	private void setPaper(TypeFormat a4) {
		FactoryPaper facp = new FactoryPaper();
	    this.paper=facp.getPaper(a4); 
	}
	
	private void FillSubjectListInFormation() {
		(formationsMap.get("M1MIAGEApp")).fillsubjectList(subjects);
	}	

}
