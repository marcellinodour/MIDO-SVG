package com.github.cocolollipop.mido_svg.svg_generator;

import com.github.cocolollipop.mido_svg.university.components.Formation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;

/**
 * This class adapts the position of objects in order to draw a
 * responsive SVG according to the user's settings.
 *
 */
public class ResponsiveSVG {
	
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(ResponsiveSVG.class);
	
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
		
		/*We have to define the margins:
		 * This margins will be use in the future to calculate the offsets
		 * and to control if an formation is written outside of the authorized area
		 * */
		int TOPMARGIN = (int)((double)canvasY *0.10);
		int BOTMARGIN = (int)((double)canvasY *0.95);
		int LEFTMARGIN = (int)((double)canvasX *0.05);
		int RIGHTMARGIN = (int)((double)canvasX *0.95);
		
		/*
		 * We have to define the initial shift. So we must count the total
		 * number formations in "list" then calculate Y offset depending on which
		 * formation are chosen (Only L1 or all ?). Finally we calculate X offset
		 */
		int offsetX = 0;
		int offsetY = 0;
	
		/* O<=totalCptY<=5 corresponds to the DOM Tree height 
		 * totalCptY = 5 means we have to draw the formations from grade 1 to 5 ("L1" to "M2") 
		 */
		int totalCptY = 0; 

		/* We assume that the indexes correspond to the grade ordered from 1 to 5 ("L1" to "M2")
		 * For the moment we just initialize the tab with zeros 
		 * Afterwards, depending on which levels the user choose to
		 * draw, each level will have a certain height in the tree
		 */
		int cptY[] = {0,0,0,0,0};   
		
		/* 
		 * The following instruction loop on the list of formation which is in the parameters of this method 
		 * and calculate the number of formation per grade
		 * each result is store on the ArrayList "nbFormaPerGrade"
		 */
		ArrayList<Integer> nbFormaPerGrade = new ArrayList<>(); 
		for (int i=0; i<=4; i++) {
			nbFormaPerGrade.add(0);
		}
		
		for (Formation f: list) {
			int grade=f.getGrade();
			nbFormaPerGrade.set(grade-1,nbFormaPerGrade.get(grade-1)+1) ; 
		}
			
		/* 
		 * The following instruction loop on the list of formation which is in the parameters of this method 
		 * and calculate the maximum number of subjects per grade
		 * each result is store on the ArrayList "maxSubjPerGrade"
		 */
		
		ArrayList<Integer> maxSubjPerGrade = new ArrayList<>(); 
		for (int i=0; i<=4; i++) {
			maxSubjPerGrade.add(0);
		}
		
		for (Formation f: list) {
			int grade=f.getGrade();
			int nbsujet =f.getSubjects().size();
			if (nbsujet >= maxSubjPerGrade.get(grade-1)) {
				maxSubjPerGrade.set(grade-1, nbsujet);
			}
		}
		
		/* The following instruction loop on the number of formation per grade to fill cptY and calculate totalCptY
		 * For example grade 1 (nbFormaPerGrade.get(0)) and grade 2 (nbFormaPerGrade.get(1))
		 * then cpt[2] == 1 which means that the level 
		 * grade 3 has the height 1 in the tree which means
		 * grade 3 is the root of the tree.
		 * 
		 */
	     
		int compteur = 0;
		for (Integer grade : nbFormaPerGrade) {
			if (grade!=0) {
				totalCptY++;
				cptY[compteur] = totalCptY;
			}	
			compteur++;
		}
		totalCptY+= 1; 
		
		/* calculate the offset for an subject (it is temporary, we should use Graphics2D in the futur) */
		int offset_subject= 16 + 14;
		
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
		

		offsetY = TOPMARGIN;
		int margesubject;
		int HeigtFormTittle= 50;
		
		for (int i=0; i<=nbFormaPerGrade.size()-1; i++) {
			LOGGER.info("-------------------loop{}",i+1);
			LOGGER.info("The grade {} have {} formation and {} csubjects max",i+1,nbFormaPerGrade.get(i),maxSubjPerGrade.get(i));
			margesubject=0;
			if (nbFormaPerGrade.get(i) !=0) {
				if (maxSubjPerGrade.get(i)!=0) {
					margesubject=28;
				}
				offsetX = canvasX / (nbFormaPerGrade.get(i) + 1);
				//offsetY = (int) ((canvasY / (totalCptY) ) * (cptY[i] - 1) + (canvasY * 0.1));
				LOGGER.info("his position in the three is {} and the vertical offset is {}",cptY[i],offsetY);
				associatePositionX(list, i+1, offsetX, offsetY);
				offsetY = offsetY + (margesubject+maxSubjPerGrade.get(i)*offset_subject);	
			}
		}	
	}
	
	
	/**
	 * Associate each formation of the specified level to a certain 
	 * position according to the coordinates passed in parameter 
	 *
	 * @param list
	 *            is a LinkedList of Formation
	 * @param myYear
	 *            is the grade 1="l1", 2=" l2",....
	 * @param offsetX
	 * 			  the abscissa calculated after shifting and adjusting 
	 * @param offsetY
	 * 			  the ordinate calculated after shifting and adjusting
	 */

	private void associatePositionX(List<Formation> list, int myYear, int offsetX, int offsetY) {
		int i = 0;
		for (Formation aFormation : list) {
			if (aFormation.getGrade() == myYear) {
				aFormation.setPosX((int) (offsetX * i + offsetX * 0.5));
				aFormation.setPosY(offsetY);
				i++;
				//LOGGER.info("associerOK : {}",aFormation.getFullName());
			}
		}
	}
}
