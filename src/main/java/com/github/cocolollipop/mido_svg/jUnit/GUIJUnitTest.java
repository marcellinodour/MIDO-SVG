package com.github.cocolollipop.mido_svg.jUnit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.cocolollipop.mido_svg.database.DataBase;
import com.github.cocolollipop.mido_svg.svg_generator.ResponsiveSVG;

public class GUIJUnitTest {

	/**
	 * In this function we will test the function that count the number of
	 * formations in Parcours (or Year) "M1"
	 *
	 */

	@Test
	public void GUItest() {

		ResponsiveSVG r = new ResponsiveSVG();
		DataBase data = new DataBase();
		int output = r.countFormations(data.getFormations(), "M1");
		assertEquals(2, output);

	}

}
