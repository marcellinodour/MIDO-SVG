package com.github.cocolollipop.mido_svg.svg_generator;

import java.util.List;

import com.github.cocolollipop.mido_svg.university.components.Formation;

/**
 * This class adapts the position of objects to draw according to the user's
 * settings
 *
 */
public class ResponsiveSVG {

	/**
	 * countFormations count the number of "myYear" in lesFormations.getFullName()
	 *
	 * @param list
	 *            is a LinkedList of Formation
	 * @param myYear
	 *            is a year such as "L3" or "M1"
	 * @return an integer or a negative if myYear isn't in the List
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
	 * defineObjectsPosition determine the position of each Formation in the
	 * someFormations List
	 *
	 * @param list
	 *            : LinkedList of all the formations available in the University
	 * @param canvasX
	 *            : abscissa size of the actual Canval
	 */
	public void defineObjectsPosition(List<Formation> list, int canvasX, int canvasY) {

		/*
		 * We define initial offset In order to it, we must count number of each
		 * Formation in someFormations Then calculate Y offset depending on which
		 * formation exists (Only L1 or all ?) Finally we calculate X offset
		 */
		int offsetX = 0;
		int offsetY = 0;
		int nbL1 = 0;
		int nbL2 = 0;
		int nbL3 = 0;
		int nbM1 = 0;
		int nbM2 = 0;
		int totalCptY = 0; // O<=totalCptY<=5 // tal number of potential
							// "stairs" in Y

		int cptY[] = new int[5];// trigger ; cpunt if there is an object in
								// Formation ; cptYL1 =0, else the postion of
								// first "seen" 0<=cptY[i]<=totalcptY
		for (int i = 0; i < 5; i++) {
			cptY[i] = 0;
		}

		// First we count number of each formation
		nbL1 = countFormations(list, "L1");
		nbL2 = countFormations(list, "L2");
		nbL3 = countFormations(list, "L3");
		nbM1 = countFormations(list, "M1");
		nbM2 = countFormations(list, "M2");

		/*
		 * We calculate Y offset
		 */
		int tempCpt = 0;
		if (nbL1 != 0) {
			totalCptY = totalCptY + 1;
			cptY[0] = tempCpt + 1;
			tempCpt++;
		}
		if (nbL2 != 0) {
			totalCptY = totalCptY + 1;
			cptY[1] = tempCpt + 1;
			tempCpt++;
		}
		if (nbL3 != 0) {
			totalCptY = totalCptY + 1;
			cptY[2] = tempCpt + 1;
			tempCpt++;
		}
		if (nbM1 != 0) {
			totalCptY = totalCptY + 1;
			cptY[3] = tempCpt + 1;
			tempCpt++;
		}
		if (nbM2 != 0) {
			totalCptY = totalCptY + 1;
			cptY[4] = tempCpt + 1;
			tempCpt++;
		}

		/*
		 * Now we calculate X and Y offset
		 */
		offsetX = canvasX / (nbL1 + 1);
		offsetY = canvasY / (totalCptY + 1) * cptY[0];
		associatePositionX(list, "L1", offsetX, offsetY);

		offsetX = canvasX / (nbL2 + 1);
		offsetY = canvasY / (totalCptY + 1) * cptY[1];
		associatePositionX(list, "L2", offsetX, offsetY);

		offsetX = canvasX / (nbL3 + 1);
		offsetY = canvasY / (totalCptY + 1) * cptY[2];
		associatePositionX(list, "L3", offsetX, offsetY);

		offsetX = canvasX / (nbM1 + 1);
		offsetY = canvasY / (totalCptY + 1) * cptY[3];
		associatePositionX(list, "M1", offsetX, offsetY);

		offsetX = canvasX / (nbM2 + 1);
		offsetY = canvasY / (totalCptY + 1) * cptY[4];
		associatePositionX(list, "M2", offsetX, offsetY);

	}

	/**
	 * associatePositionX set the posX of each Formation which satisfy
	 * uneFormation.getFullName() == myYear
	 *
	 * @param list
	 *            is a LinkedList of Formation
	 * @param myYear
	 *            is a year such as "L3" or "M1"
	 * @param decalage
	 */
	private void associatePositionX(List<Formation> list, String myYear, int decalageX, int decalageY) {
		int i = 1;
		for (Formation aFormation : list) {
			if (aFormation.getFullName().indexOf(myYear) != -1) {
				aFormation.setPosX(decalageX * i);
				aFormation.setPosY(decalageY);
				i++;
				System.out.println("associerOK : " + aFormation.getFullName());
			}
		}
	}

}
