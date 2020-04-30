package com.github.cocolollipop.mido_svg.database;

import com.github.cocolollipop.mido_svg.university.components.Formation;
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
	 * @param A Formation 
	 * @param A list of subjects
	 * @author marcellinodour and Raphda
	 * @throws StandardException 
	 **/
	
	public static void getSubjects(String programID, Formation formation, List<Subject> subjects) throws StandardException {
		Querier querier = new Querier();
		
		Program program = querier.getProgram(programID);
		List<String> courseRefs = program.getProgramStructure().getValue().getRefCourse();
		
		for(String courseRef : courseRefs) {
			final Course course = querier.getCourse(courseRef);
			Subject s = ObjectTransformer.createSubject(course, formation);
			subjects.add(s);
		}

	}

}
