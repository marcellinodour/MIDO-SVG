package com.github.cocolollipop.mido_svg.svg_generator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import com.github.cocolollipop.mido_svg.university.components.Formation;

import java.awt.Point;
import java.util.List;

import com.github.cocolollipop.mido_svg.model.DataBase;

public class ResponsiveSVGTests {

	/**
	 * Tests that an SVG in A4 format is responsive by calculating
	 * the objects position by hand 
	 *  
	 * @result the position calculated by hand and verified by Google Inspection 
	 * in the browser should be the same as the stored position of the object
	 */
	@Test
	public void testFormatA4(){
		Settings settings = new Settings(false, false, false, false, false, false, true, 1052, 744);
		DataBase datas = new DataBase(settings);
		List<Formation> formations = datas.getFormations();

		ResponsiveSVG responsive = new ResponsiveSVG();
		responsive.defineObjectsPosition(formations, settings.getWidth(), settings.getHeight(), settings.isHiddenSubject());

		assertEquals(formations.get(0).getFullName(),"L3 MIAGE");
		assertEquals(formations.get(2).getFullName(),"M1 MIAGE");
		assertEquals(formations.get(0).getPoint(), new Point(233,74));
		assertEquals(formations.get(2).getPoint(), new Point(294,290));
	}

	/**
	 * Tests that an SVG in A3 format is responsive by calculating
	 * the objects position by hand 
	 *  
	 * @result the position calculated by hand and verified by Google Inspection 
	 * in the browser should be the same as the stored position of the object
	 */
	@Test
	public void testFormatA3(){
		Settings settings = new Settings(false, false, false, false, false, false, true, 3508, 4961);
		DataBase datas = new DataBase(settings);
		List<Formation> formations = datas.getFormations();

		ResponsiveSVG responsive = new ResponsiveSVG();
		responsive.defineObjectsPosition(formations, settings.getWidth(), settings.getHeight(), settings.isHiddenSubject());

		assertEquals(formations.get(0).getFullName(),"L3 MIAGE");
		assertEquals(formations.get(2).getFullName(),"M1 MIAGE");
		assertEquals(formations.get(0).getPoint(), new Point(1093,496));
		assertEquals(formations.get(2).getPoint(), new Point(1154,1608));
	}

	/**
	 * Tests that an SVG in any random format is responsive by calculating
	 * the objects position by hand 
	 *  
	 * @result the position calculated by hand and verified by Google Inspection 
	 * in the browser should be the same as the stored position of the object
	 */
	@Test
	public void testOtherFormat(){
		Settings settings = new Settings(false, false, false, false, false, false, true, 1200, 400);
		DataBase datas = new DataBase(settings);
		List<Formation> formations = datas.getFormations();

		ResponsiveSVG responsive = new ResponsiveSVG();
		responsive.defineObjectsPosition(formations, settings.getWidth(), settings.getHeight(), settings.isHiddenSubject());

		assertEquals(formations.get(0).getFullName(),"L3 MIAGE");
		assertEquals(formations.get(2).getFullName(),"M1 MIAGE");
		assertEquals(formations.get(0).getPoint(), new Point(286,40));
		assertEquals(formations.get(2).getPoint(), new Point(347,183));
	}
}
