package com.github.cocolollipop.mido_svg.university.components;

import java.util.ArrayList;

/**
 * This class is corresponding to the Department of the formation. It contains :
 * title (the name of departement) and listOfFormations (the formations
 * available in this Departement)
 *
 */
public class Department {
	// name of the department (for example: "MIDO", "LSO" etc.)
	private String title;

	// list of the formations
	protected ArrayList<Formation> listOfFormations;

	// coordinate X
	protected int posX;

	// coordinate Y
	protected int posY;

	public Department() {
		this.title = "";
		this.posX = 0;
		this.posY = 0;
	}

	// Constructors
	public Department(String name, int x, int y) {
		this.title = name;
		this.posX = x;
		this.posY = y;

	}
	
	// Constructors
	public Department(String name) {
		this.title = name;

	}

	public String getNomDepartement() {
		return title;
	}

	public int getX() {
		return posX;
	}

	public int getY() {
		return posY;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setX(int x) {
		this.posX = x;
	}

	public void setY(int y) {
		this.posY = y;
	}
}
