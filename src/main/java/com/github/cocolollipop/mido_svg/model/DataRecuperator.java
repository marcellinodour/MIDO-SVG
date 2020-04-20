package com.github.cocolollipop.mido_svg.model;

import com.github.cocolollipop.mido_svg.BddQuerries.Querier;
import com.github.cocolollipop.mido_svg.university.components.Subject;
import java.util.List;

/**
 * This class enables to collect informations from Dauphine's DataBase 
 * @author camillelanglois3
 * @date 19/04/2020
 */

public class DataRecuperator {

	/** 
	 * this method enables to get all the subjects attached to a programID
	 * @param a String containing a program ID 
	 * @return a List of all the Subject attached to the programID
	 * @author camillelanglois3
	 **/
	public static List<Subject> getSubjects(String programID){
		List<Subject> list;
		Querier querier = new Querier();
		Program program = querier.getProgram(programID);
		List<String> courseRefs = program.getProgramStructure().getValue().getRefCourse();
		for(String courseRef : courseRefs) {
			final Course course = querier.getCourse(courseRef);
			Subject s = createSubject(course);
			list.add(s);
		}
		return list;
	}

}
