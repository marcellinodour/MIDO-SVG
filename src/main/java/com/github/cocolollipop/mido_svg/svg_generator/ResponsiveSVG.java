package com.github.cocolollipop.mido_svg.svg_generator;

import java.util.List;

import com.github.cocolollipop.mido_svg.university.components.Formation;
import com.github.cocolollipop.mido_svg.university.components.Subject;

/**
 * This class adapts the position of objects in order to draw a
 * responsive SVG according to the user's settings.
 *
 */
public class ResponsiveSVG {

	/**
	 * Count the number of formations of a specific level in a list.
	 *
	 * @param list
	 *            is a LinkedList of formations
	 * @param myYear
	 *            is a year of study such as "L3" or "M1"
	 *            
	 * @return an integer corresponding to the number of formations in "list" of level "myYear"
	 */
	public int countFormations(List<Formation> list, String myYear) {
		int nb = 0;
		for (Formation aFormation : list) {
			if (aFormation.getFullName().indexOf(myYear) != -1) {
				nb++;
			}
		}
		return nb;
	}

	/**
	 * Determine the position of each Formation in the 
	 * Canvas according to the number of formations and their level.
	 *
	 * @param list
	 *            : LinkedList of all the formations available in the University
	 * @param canvasX
	 *            : width of the canvas
	 * @param canvasY
	 * 			  : height of the canvas
	 */
	public void defineObjectsPosition(List<Formation> list, int canvasX, int canvasY) {

		/*
		 * We have to define the initial shift. So we must count the total
		 * number formations in "list" then calculate Y offset depending on which
		 * formation are chosen (Only L1 or all ?). Finally we calculate X offset
		 */
		int offsetX = 0;
		int offsetY = 0;
		int nbL1 = 0;
		int nbL2 = 0;
		int nbL3 = 0;
		int nbM1 = 0;
		int nbM2 = 0;
		
		int totalCptY = 0; /* O<=totalCptY<=5 corresponds to the DOM Tree height 
						    * totalCptY = 5 means we have to draw the formations from 
					        * level "L1" to "M2" 
					        */

		int cptY[] = {0,0,0,0,0};   /* We assume that the indexes correspond to the levels 
									 * ordered from "L1" to "M2"
									 * For the moment we just initialize the tab with zeros 
									 * Afterwards, depending on which levels the user choose to
									 * draw, each level will have a certain height in the tree
									 */
								
		
		// First we count the number of formations of every level
		nbL1 = countFormations(list, "L1");
		nbL2 = countFormations(list, "L2");
		nbL3 = countFormations(list, "L3");
		nbM1 = countFormations(list, "M1");
		nbM2 = countFormations(list, "M2");


		/* We fill cptY
		 * 
		 * For example if nbL1==0 and nbL2 == 0 
		 * then cpt[2] == 1 which means that the level 
		 * "L3" has the height 1 in the tree which means
		 * "L3" is the root of the tree.
		 * 
		 */
		int tempCpt = 0;
		if (nbL1 != 0) {					
			totalCptY += 1;     
			cptY[0] = tempCpt + 1;
			tempCpt++;
		}
		if (nbL2 != 0) {
			totalCptY += 1; 
			cptY[1] = tempCpt + 1;
			tempCpt++;
		}
		if (nbL3 != 0) {
			totalCptY += 1; 
			cptY[2] = tempCpt + 1;
			tempCpt++;
		}
		if (nbM1 != 0) {
			totalCptY += 1; 
			cptY[3] = tempCpt + 1;
			tempCpt++;
		}
		if (nbM2 != 0) {
			totalCptY += 1; 
			cptY[4] = tempCpt + 1;
		}
		
		totalCptY+= 1;
		
		/*
		 * Now we calculate X and Y offset
		 * First we have to check which levels has been seted 
		 * If it has been, then 
		 * 		- the canvas'width should be evenly distributed 
		 * 		over the number of formations of that level
		 * 		- the canvas'height should also be evenly distributed 
		 * 		over the number of formations of that level and should take 
		 * 		into account its height like calculated before in cptY.
		 * 		Plus, we added a margin, which is proportional to the canvas to 
		 * 		make it responsive, so that the SVG would be centered. 
		 */
		if (nbL1 != 0) {
			offsetX = canvasX / (nbL1 + 1);
			offsetY = (int) ((canvasY / (totalCptY) ) * (cptY[0] - 1) + (canvasY * 0.1));
			associatePositionX(list, "L1", offsetX, offsetY);
		}
		
		if (nbL2 != 0) {
			offsetX = canvasX / (nbL2 + 1);
			offsetY = (int) ((canvasY / (totalCptY) ) * (cptY[1] - 1) + (canvasY * 0.1));
			associatePositionX(list, "L2", offsetX, offsetY);
		}
		
		if (nbL3 != 0) {
			offsetX = canvasX / (nbL3 + 1);
			offsetY = (int) ((canvasY / (totalCptY) ) * (cptY[2] - 1) + (canvasY * 0.1));
			associatePositionX(list, "L3", offsetX, offsetY);
		}
		
		if (nbM1 != 0) {
			offsetX = canvasX / (nbM1 + 1);
			offsetY = (int) ((canvasY / (totalCptY)) * (cptY[3] - 1) + (canvasY * 0.1));
			associatePositionX(list, "M1", offsetX, offsetY);
		}
		
		if (nbM2 != 0) {
			offsetX = canvasX / (nbM2 + 2);
			offsetY = (int) ((canvasY / (totalCptY)) * (cptY[4] - 1) + (canvasY * 0.1));
			associatePositionX(list, "M2", offsetX, offsetY);
		}

	}

	/**
	 * Associate each formation of the specified level to a certain 
	 * position according to the coordinates passed in parameter 
	 *
	 * @param list
	 *            is a LinkedList of Formation
	 * @param myYear
	 *            is a year of study such as "L3" or "M1"
	 * @param offsetX
	 * 			  the abscissa calculated after shifting and adjusting 
	 * @param offsetY
	 * 			  the ordinate calculated after shifting and adjusting
	 */
	private void associatePositionX(List<Formation> list, String myYear, int offsetX, int offsetY) {
		int i = 0;
		for (Formation aFormation : list) {
			if (aFormation.getFullName().indexOf(myYear) != -1) {
				aFormation.setPosX((int) (offsetX * i + offsetX * 0.5));
				aFormation.setPosY(offsetY);
				i++;
				System.out.println("associerOK : " + aFormation.getFullName());
			}
		}
	}

	private void controlResponsiveSVG(List<Formation> list, int canvasX, int canvasY) throws Exception { 
	     for(Formation f : list) {
	      if(f.getPosY() > canvasY) {
	       throw new Exception("the ordinate element can't beyond the canvasY");
	      }
	      if(f.getPosX() > canvasX) {
	       throw new Exception("the abscissa element can't beyond the canvasX");
	      }
	     }
	  
	 }
	 
	 private boolean controlSetting(Formation f, int x, int y, int maxNumberOfCourse, int maxLengthOfCourse) throws Exception{
	  boolean control = false;
	  List<Subject> l= f.getSubjects();
	  for(Subject s : l) {
	   if(x < maxNumberOfCourse) {
	    throw new Exception("Overlap control Warning : the position x of the formation according to "
	      + "new_x < maxNumberOfCourse of the previous formation.");
	   }
	   else if(x > maxLengthOfCourse) {
	    throw new Exception("overlap control Warning : the position x of the formation according to \"\n" + 
	      "      + \"new_x > maxLengthOfCourse.");
	   }
	   else {
	    control = true;
	   }
	  }
	  return control;
	 }
	
}
