package com.github.cocolollipop.mido_svg.xml.jaxb.model;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class is corresponding to a tag A tag stores 1 name and a Set of
 * subjects
 *
 */
@XmlRootElement(name = "tag")
@XmlAccessorType(XmlAccessType.FIELD)
// To define the order in which the fields are written
@XmlType(propOrder = { "name", "subjects" })
public class Tag {

	@XmlElement(name = "name")
	private String name; 

	@XmlElementWrapper(name = "subjects")
	@XmlElement(name = "subject")
	private Set<String> subjects; 

	public void addSubject(String math) {
		if (subjects == null) {
			this.subjects = new HashSet<>();
		}
		this.subjects.add(math);

	}

	public String getName() {
		return this.name;

	}

	public Set<String> getSubjects() {
		return this.subjects;
	}

	public void setName(String name) {
		this.name = name;

	}

}
