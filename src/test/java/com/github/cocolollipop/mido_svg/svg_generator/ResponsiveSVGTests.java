package com.github.cocolollipop.mido_svg.svg_generator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.github.cocolollipop.mido_svg.database.DataBase;
import com.github.cocolollipop.mido_svg.university.components.Formation;

import java.awt.Point;
import java.util.List;

public class ResponsiveSVGTests {
	 
	/**
	 * Tests that an SVG in A4 format is responsive by calculating
	 * the objects position by hand 
	 * 
	 * The test allow an uncertainty for the position because GitHub seems to make different rounded
	 *  
	 * @result the position calculated by hand and verified by Google Inspection 
	 * in the browser should be the same as the stored position of the object
	 */
	@Test
	public void testFormatA4WithAll(){
		Settings settings = new Settings(false, false, false, false, false, false, true, 1052, 744);
		TestsDatabase datas = new TestsDatabase(settings);
		List<Formation> formations = datas.getFormations();

		ResponsiveSVG responsive = new ResponsiveSVG();
		responsive.defineObjectsPosition(formations, settings.getWidth(), settings.getHeight(), settings.isHiddenSubject(), settings.isHiddenLicence(), settings.isHiddenMaster());

		assertEquals("L3 MIAGE", formations.get(0).getFullName());
		assertEquals("M1 MIAGE", formations.get(2).getFullName());
		assertEquals(243, formations.get(0).getPoint().getX(), 30);
		assertEquals(634, formations.get(1).getPoint().getX(), 30);
		assertEquals(293, formations.get(2).getPoint().getX(), 30);
		assertEquals(653, formations.get(3).getPoint().getX(), 30);
		assertEquals(91, formations.get(4).getPoint().getX(), 30);
		assertEquals(224, formations.get(5).getPoint().getX(), 30);
		assertEquals(359, formations.get(6).getPoint().getX(), 30);
		assertEquals(508, formations.get(7).getPoint().getX(), 30);
		assertEquals(665, formations.get(8).getPoint().getX(), 30);
		assertEquals(824, formations.get(9).getPoint().getX(), 30);
		
		assertEquals(74, formations.get(0).getPoint().getY(), 30);
		assertEquals(74, formations.get(1).getPoint().getY(), 30);
		assertEquals(283, formations.get(2).getPoint().getY(), 30);
		assertEquals(283, formations.get(3).getPoint().getY(), 30);
		assertEquals(466, formations.get(4).getPoint().getY(), 30);
		assertEquals(466, formations.get(5).getPoint().getY(), 30);
		assertEquals(466, formations.get(6).getPoint().getY(), 30);
		assertEquals(466, formations.get(7).getPoint().getY(), 30);
		assertEquals(466, formations.get(8).getPoint().getY(), 30);
		assertEquals(466, formations.get(9).getPoint().getY(), 30);
	}

	/**
	 * Tests that an SVG in A4 format without Licence is responsive by calculating
	 * the objects position by hand 
	 * 
	 * The test allow an uncertainty for the position because GitHub seems to make different rounded
	 *  
	 * @result the position calculated by hand and verified by Google Inspection 
	 * in the browser should be the same as the stored position of the object
	 */
	@Test
	public void testFormatA4WithoutLicence(){
		Settings settings = new Settings(true, false, false, false, false, false, true, 1052, 744);
		TestsDatabase datas = new TestsDatabase(settings);
		List<Formation> formations = datas.getFormations();

		ResponsiveSVG responsive = new ResponsiveSVG();
		responsive.defineObjectsPosition(formations, settings.getWidth(), settings.getHeight(), settings.isHiddenSubject(), settings.isHiddenLicence(), settings.isHiddenMaster());

		assertEquals(293, formations.get(2).getPoint().getX(), 30);
		assertEquals(653, formations.get(3).getPoint().getX(), 30);
		assertEquals(91, formations.get(4).getPoint().getX(), 30);
		assertEquals(224, formations.get(5).getPoint().getX(), 30);
		assertEquals(359, formations.get(6).getPoint().getX(), 30);
		assertEquals(508, formations.get(7).getPoint().getX(), 30);
		assertEquals(665, formations.get(8).getPoint().getX(), 30);
		assertEquals(824, formations.get(9).getPoint().getX(), 30);
		
		assertEquals(74, formations.get(2).getPoint().getY(), 30);
		assertEquals(74, formations.get(3).getPoint().getY(), 30);
		assertEquals(327, formations.get(4).getPoint().getY(), 30);
		assertEquals(327, formations.get(5).getPoint().getY(), 30);
		assertEquals(327, formations.get(6).getPoint().getY(), 30);
		assertEquals(327, formations.get(7).getPoint().getY(), 30);
		assertEquals(327, formations.get(8).getPoint().getY(), 30);
		assertEquals(327, formations.get(9).getPoint().getY(), 30);
	}

	/**
	 * Tests that an SVG in A4 format without Master and Courses is responsive by calculating
	 * the objects position by hand 
	 * 
	 * The test allow an uncertainty for the position because GitHub seems to make different rounded
	 *  
	 * @result the position calculated by hand and verified by Google Inspection 
	 * in the browser should be the same as the stored position of the object
	 */
	@Test
	public void testFormatA4WithoutMasterAndCourses(){
		Settings settings = new Settings(false, true, false, false, true, false, true, 1052, 744);
		TestsDatabase datas = new TestsDatabase(settings);
		List<Formation> formations = datas.getFormations();

		ResponsiveSVG responsive = new ResponsiveSVG();
		responsive.defineObjectsPosition(formations, settings.getWidth(), settings.getHeight(), settings.isHiddenSubject(), settings.isHiddenLicence(), settings.isHiddenMaster());

		assertEquals(308, formations.get(0).getPoint().getX(), 30);
		assertEquals(640, formations.get(1).getPoint().getX(), 30);
		
		assertEquals(74, formations.get(0).getPoint().getY(), 30);
		assertEquals(74, formations.get(1).getPoint().getY(), 30);
	}

	/**
	 * Tests that an SVG in A4 format with only Formations is responsive by calculating
	 * the objects position by hand 
	 * 
	 * The test allow an uncertainty for the position because GitHub seems to make different rounded
	 *  
	 * @result the position calculated by hand and verified by Google Inspection 
	 * in the browser should be the same as the stored position of the object
	 */
	@Test
	public void testFormatA4WithOnlyFormations(){
		Settings settings = new Settings(false, false, true, true, true, true, true, 1052, 744);
		TestsDatabase datas = new TestsDatabase(settings);
		List<Formation> formations = datas.getFormations();

		ResponsiveSVG responsive = new ResponsiveSVG();
		responsive.defineObjectsPosition(formations, settings.getWidth(), settings.getHeight(), settings.isHiddenSubject(), settings.isHiddenLicence(), settings.isHiddenMaster());


		assertEquals(308, formations.get(0).getPoint().getX(), 30);
		assertEquals(640, formations.get(1).getPoint().getX(), 30);
		assertEquals(306, formations.get(2).getPoint().getX(), 30);
		assertEquals(640, formations.get(3).getPoint().getX(), 30);
		assertEquals(91, formations.get(4).getPoint().getX(), 30);
		assertEquals(224, formations.get(5).getPoint().getX(), 30);
		assertEquals(359, formations.get(6).getPoint().getX(), 30);
		assertEquals(508, formations.get(7).getPoint().getX(), 30);
		assertEquals(665, formations.get(8).getPoint().getX(), 30);
		assertEquals(824, formations.get(9).getPoint().getX(), 30);
		
		assertEquals(74, formations.get(0).getPoint().getY(), 30);
		assertEquals(74, formations.get(1).getPoint().getY(), 30);
		assertEquals(260, formations.get(2).getPoint().getY(), 30);
		assertEquals(260, formations.get(3).getPoint().getY(), 30);
		assertEquals(446, formations.get(4).getPoint().getY(), 30);
		assertEquals(446, formations.get(5).getPoint().getY(), 30);
		assertEquals(446, formations.get(6).getPoint().getY(), 30);
		assertEquals(446, formations.get(7).getPoint().getY(), 30);
		assertEquals(446, formations.get(8).getPoint().getY(), 30);
		assertEquals(446, formations.get(9).getPoint().getY(), 30);
	}
}
