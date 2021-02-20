package com.github.cocolollipop.mido_svg.university.components;

import java.util.ArrayList;

/**
 * This class is corresponding to the Department of the formation. It contains :
 * title (the name of departement) and listOfFormations (the formations
 * available in this Departement)
 *
 */
public class Department {
	
	private String title;
	protected ArrayList<Formation> listOfFormations;
	protected int posX;
	protected int posY;

	public static Department factory(String name) {
		return new Department(name);
	}
	
	private Department() {
		this.title = "";
		this.posX = 0;
		this.posY = 0;
	}

	private Department(String name, int x, int y) {
		this.title = name;
		this.posX = x;
		this.posY = y;

	}
	
	private Department(String name) {
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
