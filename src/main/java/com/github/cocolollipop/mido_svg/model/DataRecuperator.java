package com.github.cocolollipop.mido_svg.model;

import com.github.cocolollipop.mido_svg.BddQuerries.Querier;
import com.github.cocolollipop.mido_svg.university.components.Subject;

import ebx.ebx_dataservices.StandardException;
import schemas.ebx.dataservices_1.CourseType.Root.Course;
import schemas.ebx.dataservices_1.ProgramType.Root.Program;

import java.util.ArrayList;
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
	 * @throws StandardException 
	 **/
	public static List<Subject> getSubjects(String programID) throws StandardException {
		List<Subject> list = new ArrayList<Subject>();
		Querier querier = new Querier();
		Program program = querier.getProgram(programID);
		List<String> courseRefs = program.getProgramStructure().getValue().getRefCourse();
		for(String courseRef : courseRefs) {
			final Course course = querier.getCourse(courseRef);
			Subject s = ObjectTransformer.createSubject(course);
			list.add(s);
		}
		return list;
	}

}
