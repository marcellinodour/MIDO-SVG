package com.github.cocolollipop.mido_svg.svg_generator;

import java.awt.Canvas;
import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.cocolollipop.mido_svg.university.components.Formation;
import com.github.cocolollipop.mido_svg.university.components.Subject;

/**
 * This class adapts the position of objects in order to draw a
 * responsive SVG according to the user's settings.
 *
 */
public class ResponsiveSVG {

	private static java.awt.Font Basicfont = new java.awt.Font("TimesRoman", 12, 12);
	private static Map<Integer, String> mapOfGrade = new HashMap<>();
	private static Map<Integer, Integer> numberOfGrade = new HashMap<>();

	/**
	 * Count the number of formations of a specific level in a list.
	 *
	 * @param list is a Collection of formations
	 * @param myYear is a key in mapOfGrade. 1 representing the first year (L1) and 5 the last year (M2)        
	 * @return an int corresponding to the number of formations in "list" of level "myYear"
	 */
	public int countFormations(Collection<Formation> list, int myYear) {
		int nb = 0;
		for (Formation aFormation : list) {
			if (aFormation.getFullName().indexOf(mapOfGrade.get(myYear)) != -1) {
				nb++;
			}
		}
		return nb;
	}

	/**
	 * Count the number of Subjects in the Formation of the year myYear which has the most Subjects
	 * 
	 * @param list is a Collection of formations
	 * @param myYear the year for which we want to find the maximum number of subjects.
	 * 1 representing the first year (L1) and 5 the last year (M2)
	 * @return an int that represents the maximum number of Subjects in a Formation of myYear.
	 */
	public int countMaxSubjects(Collection<Formation> list, int myYear) {
		int max = 0;
		for (Formation aFormation : list) {
			if (aFormation.getFullName().indexOf(mapOfGrade.get(myYear)) != -1) {
				if (aFormation.getSubjects().size() > max) {
					max = aFormation.getSubjects().size();
				}
			}
		}
		return max;
	}

	/**
	 * @param nbOfSubjects the number of Subjects of a block 
	 * @return the height in pixels of a Subjects block
	 */
	public int calculateHeightOfSubjects(int nbOfSubjects) {
		Canvas c = new Canvas();
		FontMetrics fm = c.getFontMetrics(Basicfont);
		int height = fm.getHeight();
		int heightOfSubjects = (nbOfSubjects+1) * height + (nbOfSubjects+2) * DrawerSVGGen.SPACE_BETWEEN_LIGNES ;
		return heightOfSubjects;
	}

	/**
	 * @param formation 
	 * @return the size in pixels of the longest Subject's title of the Formation in argument
	 */
	public int giveTheLongestSubjectInPixel(Formation formation) {
		int longestword = 0;
		for(Subject subject : formation.getSubjects()) {
			if (longestword < widthOfTheWordInPixel(subject.getTitle())) {
				longestword = widthOfTheWordInPixel(subject.getTitle());
			}
		}
		return longestword;
	}

	/**
	 * @param word
	 * @return the width in pixels of a word
	 */
	public int widthOfTheWordInPixel(String word) {
		Canvas c = new Canvas();
		FontMetrics fm = c.getFontMetrics(Basicfont);
		return fm.stringWidth(word);
	}
	/**
	 * This method aim to calculate the space that is available to add between the formations of different grade
	 * 
	 * @param canvasY
	 * @param maxSubjects
	 * @param totalCptY
	 * @return an int corresponding to the additional space in pixel
	 */
	public int calculateAdditionalSpaceY(int canvasY, Collection<Integer> maxSubjects, int totalCptY) {
		int totalHeightOfSubjects = 0;
		for (Integer i : maxSubjects) {
			totalHeightOfSubjects += calculateHeightOfSubjects(i.intValue());
		}
		Canvas c = new Canvas();
		FontMetrics fm = c.getFontMetrics(Basicfont);
		int heightOfResponsibleName = fm.getHeight();
		int formationBlock = DrawerSVGGen.HEIGHT_OF_RECTANGLE + heightOfResponsibleName;

		int additionalSpace = (int) (((canvasY - totalHeightOfSubjects - (canvasY * 0.15) - formationBlock * (totalCptY - 1)) / totalCptY) + formationBlock);

		return additionalSpace;
	}

	/**
	 * This method aim to calculate the space that is available to add between the formations of a same year when drawing.
	 * 
	 * @param myYear, key in mapOfGrade of the year on which the space is going to be calculate
	 * @param canvasX the width of the paper format
	 * @param list the Collection of all the Formations
	 * @return an int corresponding to the space in pixels to add between the formations of a same year
	 * @throws IllegalStateException
	 */
	public int calculateAdditionalSpaceByGrade(int myYear, int canvasX, Collection<Formation> list) throws IllegalStateException {
		int spaceTaken = 0;
		int cpt = 1;

		for (Formation formation : list) {
			if (formation.getFullName().contains(mapOfGrade.get(myYear))) {
				spaceTaken += giveTheLongestSubjectInPixel(formation) + widthOfTheWordInPixel(formation.getFullName()) + 20;
				cpt++;
			}
		}

		int additionalSpace = (int) ((canvasX - spaceTaken - (0.1 * canvasX)) / cpt);
		if (additionalSpace < 0) {
			throw new IllegalStateException("The format paper isn't enought wide.");
		}
		return additionalSpace;
	}

	/**
	 * Determine the position of each Formation in the 
	 * Canvas according to the number of formations and their level.
	 *
	 * @param list : Collection of all the formations available in the University            
	 * @param canvasX : width of the canvas          
	 * @param canvasY : height of the canvas	  
	 */
	public void defineObjectsPosition(Collection<Formation> initialList, int canvasX, int canvasY, boolean hiddenSubjects, boolean hiddenLicence, boolean hiddenMaster) {

		/*
		 * We create a list with all the formation that will be drawn on the svg
		 */
		List<Formation> list = new ArrayList<>();
		if(!hiddenLicence) {
			for(Formation f : initialList) {
				if( (f.getFullName().indexOf("L1") != -1) || (f.getFullName().indexOf("L2") != -1) || (f.getFullName().indexOf("L3") != -1)) {
					list.add(f);
				}
			}
		}
		if(!hiddenMaster) {
			for(Formation f : initialList) {
				if( (f.getFullName().indexOf("M1") != -1) || (f.getFullName().indexOf("M2") != -1)) {
					list.add(f);
				}
			}
		}


		mapOfGrade.put(1, "L1");
		mapOfGrade.put(2, "L2");
		mapOfGrade.put(3, "L3");
		mapOfGrade.put(4, "M1");
		mapOfGrade.put(5, "M2");

		/*
		 * We have to define the initial shift. So we must count the total
		 * number formations in "list" then calculate Y offset depending on which
		 * formation are chosen (Only L1 or all ?). Finally we calculate X offset
		 */
		int offsetY = 0;

		int additionalSpace=0;

		List<Integer> maxSubjects = new ArrayList<>();

		int spaceTaken = 0;
		int levelActual = 1;

		/* O<=totalCptY<=5 corresponds to the DOM Tree height 
		 * totalCptY = 5 means we have to draw the formations from 
		 * level "L1" to "M2" 
		 */
		int totalCptY = 0; 

		/* We assume that the indexes correspond to the levels 
		 * ordered from "L1" to "M2"
		 * For the moment we just initialize the tab with zeros 
		 * Afterwards, depending on which levels the user choose to
		 * draw, each level will have a certain height in the tree
		 */
		int cptY[] = {0,0,0,0,0};   


		/*
		 *  First we count the number of formations of every level
		 */
		numberOfGrade.put(1,countFormations(list, 1));
		numberOfGrade.put(2,countFormations(list, 2));
		numberOfGrade.put(3,countFormations(list, 3));
		numberOfGrade.put(4,countFormations(list, 4));
		numberOfGrade.put(5,countFormations(list, 5));


		/* We fill cptY
		 * 
		 * For example if nbL1==0 and nbL2 == 0 
		 * then cpt[2] == 1 which means that the level 
		 * "L3" has the height 1 in the tree which means
		 * "L3" is the root of the tree.
		 * 
		 */
		int tempCpt = 0;

		for (int i = 1; i < 6; i++) {
			if (numberOfGrade.get(i) != 0) {
				totalCptY += 1;
				cptY[i-1] = tempCpt + 1;
				tempCpt++;
				maxSubjects.add(countMaxSubjects(list, i));
			}
		}

		totalCptY += 1;

		if ((!hiddenSubjects)) {
			additionalSpace = calculateAdditionalSpaceY(canvasY, maxSubjects, totalCptY);
			if(additionalSpace < 0) {
				throw new IllegalStateException("You cannot show all the subject in this format of paper");
			}
		}

		/*
		 * Now we calculate Y offset.
		 * 
		 * For each level:
		 * 		- if the subjects are going to be drawn, the Y offset is calculated depending on 
		 * 		  the additionalSpace calculated before.
		 * 		- if the subjects are not going to be drawn, we divide the space depending on the
		 * 		  number of level drawn
		 */
		for (int i = 1; i < 6; i++) {
			if (numberOfGrade.get(i) != 0) {

				if (hiddenSubjects) {
					offsetY = (int) ((canvasY / (totalCptY) ) * (cptY[i - 1] - 1) + (canvasY * 0.1));
				}
				else {
					if (levelActual == 1) {
						offsetY = (int) (canvasY * 0.1);
						levelActual++;
						spaceTaken += offsetY + calculateHeightOfSubjects(countMaxSubjects(list, i)) + additionalSpace;
					}
					else {
						offsetY = spaceTaken;
						levelActual++;
						spaceTaken += calculateHeightOfSubjects(countMaxSubjects(list, i)) + additionalSpace;
					}
				}
				associatePositions(list, i, offsetY, canvasX, hiddenSubjects);
			}
		}
	}


	/**
	 * This method calculate the position X of the Formations of a year depending on the parameters.
	 * Then, it set the position X and the position Y of the formations
	 * 
	 * @param list
	 * @param myYear 
	 * @param offsetY
	 * @param canvasX
	 * @param isHidden
	 */
	private void associatePositions(Collection<Formation> list, int myYear, int offsetY, int canvasX, boolean isHidden) {

		if (!isHidden) {
			int additionalSpace = calculateAdditionalSpaceByGrade(myYear, canvasX, list);
			int spaceTaken = (int) ((0.05 * canvasX) + additionalSpace);
			for (Formation aFormation : list) {
				if (aFormation.getFullName().indexOf(mapOfGrade.get(myYear)) != -1) {
					aFormation.setPosX(spaceTaken);
					aFormation.setPosY(offsetY);
					spaceTaken += additionalSpace + giveTheLongestSubjectInPixel(aFormation) + widthOfTheWordInPixel(aFormation.getFullName()) + 20;
				}
			}
		}
		else {
			int additionalSpace = 0;
			int j = 0;
			for (Formation aFormation : list) {
				if (aFormation.getFullName().indexOf(mapOfGrade.get(myYear)) != -1) {
					additionalSpace += widthOfTheWordInPixel(aFormation.getFullName()) + 20;
					j++;
				}
			}
			additionalSpace = (int) ((canvasX - additionalSpace - (canvasX * 0.1)) / (j+1)); 
			if (additionalSpace < 0) {
				throw new IllegalStateException("The format paper isn't enough wide");
			}
			int spaceTaken = (int) (0.05 * canvasX + additionalSpace);
			for (Formation aFormation : list) {
				if (aFormation.getFullName().indexOf(mapOfGrade.get(myYear)) != -1) {
					aFormation.setPosX(spaceTaken);
					aFormation.setPosY(offsetY);
					spaceTaken += additionalSpace + widthOfTheWordInPixel(aFormation.getFullName()) + 20;
				}
			}
		}
	}

}
