package com.github.cocolollipop.mido_svg.svg_generator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import com.github.cocolollipop.mido_svg.database.DataBase;
import com.github.cocolollipop.mido_svg.university.components.Formation;
import com.github.cocolollipop.mido_svg.university.components.Subject;

/**
 * This class draws and generates an SVG for specifics datas
 *
 */

public class DrawerSVGGen {

	private enum DrawOnly {
		BOTH, LICENCE, MASTER
	}

	public static final String DRAWING_SVG = "./src/main/resources/images/mido-drawing.svg";

	private SVGGeneratorContext ctx;

	private DataBase datas;

	private DocumentBuilder db;

	private DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

	private Document document;

	private DOMImplementation domImpl;

	private Enum<?> drawOnly;

	private SVGGraphics2D g;

	private int lineYDOWN = 7; 

	private int lineYUP = -20; 

	private int police;

	private String svgNS;

	/**
	 * This method shows the Admission of a "formation" only if it's SHOWN in
	 * the SVG (the user chose to draw it in the SVG)
	 *
	 * The admission is written in BLUE
	 *
	 * @param settings 
	 * 				: corresponds to the settings of the user, if he chose to show
	 * 				the Licence formations or the Master formations or both, for example.
	 *
	 */

	public void drawAdmission(Settings settings) {

		if (settings.isHiddenAdmission() == false) {

			for (Formation f : this.datas.getFormations()) {
				if (f.isShown() == true) {
					g.setPaint(Color.blue);
					g.drawString(f.getAdmisssion(), f.getPoint().x - 30, f.getPoint().y - 30);
				}
			}
		}

	}

	/**
	 * This method draws a line with an arrow 
	 * from the point (x1,y1) to the point (x2,y2)
	 * in the graphic g. 
	 *
	 * @param g1 : a 2D graphic 
	 * @param x1 : an integer corresponding to the abscissa of the first point 
	 * @param y1 : an integer corresponding to the ordinate of the first point
	 * @param x2 : an integer corresponding to the abscissa of the second point
	 * @param y2 : an integer corresponding to the ordinate of the second  point
	 */
	public void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
		Graphics2D graphic = (Graphics2D) g1.create();
		int ARR_SIZE = 5;

		double dx = x2 - x1, dy = y2 - y1;
		double angle = Math.atan2(dy, dx);
		int len = (int) Math.sqrt(dx * dx + dy * dy);
		AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
		at.concatenate(AffineTransform.getRotateInstance(angle));
		graphic.transform(at);

		// Draw horizontal arrow starting in (0, 0)
		graphic.drawLine(0, 0, len, 0);
		graphic.fillPolygon(new int[] { len, len - ARR_SIZE, len - ARR_SIZE, len }, new int[] { 0, -ARR_SIZE, ARR_SIZE, 0 },
				4);
	}

	/**
	 * Draws the rectangles and the formations titles in black according to the user's settings.
	 * It also draws the lines between the formations in blue according to the relation between 
	 * the formations. 
	 * 
	 * @param settings 
	 * 				: corresponds to the settings of the user, if he chose to show
	 * 				the Licence formations or the Master formations or both, for example.
	 *
	 */
	public void drawFormation(Settings settings) {
		if (settings.isHiddenLicence() == false && settings.isHiddenMaster() == false) {
			this.drawOnly = DrawOnly.BOTH;
		} else if (settings.isHiddenLicence() == false && settings.isHiddenMaster() == true) {
			this.drawOnly = DrawOnly.LICENCE;
		} else if (settings.isHiddenLicence() == true && settings.isHiddenMaster() == false) {
			this.drawOnly = DrawOnly.MASTER;
		}

		List<Formation> listToDraw = new LinkedList<>();
		
		int centerx1, centerx2;
		// showing only licence formations
		if (this.drawOnly == DrawOnly.LICENCE || this.drawOnly == DrawOnly.MASTER) {
			listToDraw.addAll(this.fillListOfFormationToShow(drawOnly.toString()));
		}
		if (this.drawOnly == DrawOnly.BOTH) {
			listToDraw = this.datas.getFormations();
		}

		for (Formation l : listToDraw) {
			l.setShown(true);
			g.setPaint(Color.black);
			java.awt.Font Basicfont = new java.awt.Font("TimesRoman", 12, 12);
			g.setFont(Basicfont);
			g.drawString(l.getFullNameWithLink(), l.getPoint().x, l.getPoint().y);
			// write the name of formation
			Rectangle t = new Rectangle(l.getPoint().x - 10, l.getPoint().y - 20,
					g.getFontMetrics().stringWidth(l.getFullName()) + 20, 25); // draw
			// rectangle
			g.draw(t);
			g.setPaint(new Color(223,242,255));
			for (Formation l2 : l.getAvailableFormations()) {
				// draw the lines between the formation and the avalaible
				// formations
				centerx1 = g.getFontMetrics().stringWidth(l.getFullName()) / 2;
				centerx2 = g.getFontMetrics().stringWidth(l2.getFullName()) / 2;
				
				g.drawLine(l.getPoint().x + centerx1, l.getPoint().y + lineYDOWN, l2.getPoint().x + centerx2,
						l2.getPoint().y + lineYUP);

			}

		}

	}

	/**
	 * This method shows the name of the manager of a formation only if it's
	 * SHOWN in the SVG (the user choose to show it or no via the settings)
	 *
	 * The manager name is written in GREEN
	 *
	 * @param settings : 
	 * 				: corresponds to the settings of the user, if he chose to show
	 * 				the formations' manager or not, for example.
	 *
	 */

	public void drawResponsable(Settings settings) {

		if (settings.isHiddenResponsable() == false) {

			g.setPaint(new Color(31,160,85));

			for (Formation f : this.datas.getFormations()) {
				if (f.isShown() == true) {
					if (f.hasGotATeacher(f) == true) {
						g.drawString(f.getTeacher().getFullNameTeacher(),
								f.getPoint().x - 5,
								f.getPoint().y  - g.getFontMetrics().getHeight() - 10);
					}
				}
			}

			g.setPaint(Color.black);

		}

	}

	/**
	 * This method shows the name of the subjects or teachers of a formation only if
	 * it's SHOWN in the SVG.
	 *
	 * If the subjects are not shown, teachers won't appear in the SVG.
	 *
	 * Either we'll have both, or only subjects.
	 *
	 * The size of teachers is smaller and in RED.
	 *
	 * This function draws also lines between a subject and his prerequisites
	 *
	 *
	 * @param settings :
	 * 				 corresponds to the settings of the user, if he chose to show
	 * 				the Licence formations or the Master formations or both, for example.
	 *
	 */

	public void drawSubjectTeacher(Settings settings) {
		int decX = 0;
		int decY = 0;
		if (settings.isHiddenSubject() == false) {

			for (Formation f : this.datas.getFormations()) {
				
				/*For every formation we define horizontal and vertical 
				 * shifts to write the courses and their managers beside
				 * the corresponding formation.
				 */ 
				decX = g.getFontMetrics().stringWidth(f.getFullName()) + 12;
				decY = g.getFontMetrics().getHeight() + 28;
				

				if (f.isShown() == true) {
					g.setPaint(Color.pink);
					g.drawString("Cours: ", f.getPoint().x + decX, f.getPoint().y + 18);
					g.setPaint(Color.black);
					for (Subject s : f.getSubjects()) {

						g.drawString(s.getTitle(), f.getPoint().x + decX, f.getPoint().y + decY);
						s.setPosX(f.getPoint().x + decX);
						s.setPosY(f.getPoint().y + decY);
						
						/* We increment the vertical offset so that the next course will be 
						 * written below.
						*/
						decY += g.getFontMetrics().getHeight() + 14;
						
						// DRAW PREREQUISITES LINES
						for (Subject s2 : f.getSubjects()) {
							if (settings.isHiddenPrerequisites() == false) {
								for (Subject p : s2.getListOfPrerequisites()) {
									g.setPaint(Color.orange);
									drawArrow(g, s.getPoint().x + 15, s.getPoint().y - 20, p.getPoint().x + 30,
											p.getPoint().y + 3);
								}
							}
							g.setPaint(Color.black);
						}
					}
				}
			}

			if (settings.isHiddenTeacher() == false) {
				g.setPaint(Color.red);

				for (Formation f : this.datas.getFormations()) {
					if (f.isShown() == true) {

						for (Subject s : f.getSubjects()) {

							java.awt.Font font = new java.awt.Font("TimesRoman", 9, 9);
							g.setFont(font);
							g.drawString(s.getResponsible().getLastName(), s.getPoint().x, s.getPoint().y - 15);

						}
					}

				}
			}

		}

	}

	/**
	 * This method fills the listOfFormationToShow with objects
	 * from FormationList that are of type typeOfFormation
	 *
	 * @param type: 
	 * 			a String corresponding to a formation's type
	 * @return : a list of formation of type "type"
	 */
	public List<Formation> fillListOfFormationToShow(String type) {
		List<Formation> listOfFormationToShow = new LinkedList<>();

		for (Formation formation : this.datas.getFormations()) {
			if (formation.getCategory().toString() == type) {
				listOfFormationToShow.add(formation);
			}
		}
		return listOfFormationToShow;
	}

	public int getPolice() {
		return police;
	}

	public void paint(Settings settings, DataBase datas1) throws Exception {
		this.datas = datas1;

		db = dbf.newDocumentBuilder();
		// Get a DOMImplementation.
		domImpl = db.getDOMImplementation();
		// Create an instance of org.w3c.dom.Document.
		svgNS = "http://www.w3.org/2000/svg";
		document = domImpl.createDocument(svgNS, "svg", null);

		// Create an instance of the SVG Generator.
		ctx = SVGGeneratorContext.createDefault(document);
		ctx.setEmbeddedFontsOn(true);
		g = new SVGGraphics2D(ctx, false);
		// Create position variables

		// this.defineObjectsPosition(this.data.getFormations(), 1920, 1080);

		this.drawAdmission(settings);
		this.drawFormation(settings);
		this.drawResponsable(settings);
		this.drawSubjectTeacher(settings);
		this.datas.setPaper(settings.getFormat(), settings.getWidth(), settings.getHeight());

		g.setSVGCanvasSize(new Dimension(this.datas.getPaper().getDimXCanvas(), this.datas.getPaper().getDimYCanvas()));
		g.drawString(this.datas.getDepartment().getNomDepartement(), this.datas.getDepartment().getX(),
				this.datas.getDepartment().getY());

		// The tag that the user selected (he wants to see what are the
		// formation that teaches this course)
		// String userSelectedTag = "Probas";
		// String userSelectedTags[] = { "Rugby", "ADD", "Espagnol" };

		// Ask the test to render into the SVG Graphics2D implementation.

		// Tag checking
		/*
		 * int cptTags = 0; for (Formation f : this.data.getListOfFormations()) {
		 * cptTags = 0; for (String str : userSelectedTags) {
		 *
		 * if (Arrays.asList(f.getTagslist()).contains(str)) { cptTags++;
		 *
		 * if (cptTags == userSelectedTags.length) { g.setPaint(Color.red);
		 * g.drawString("(X)", f.getPoint().x + 50, f.getPoint().y + 20); } } } }
		 */

		// g.setPaint(Color.black);

		// Finally, stream out SVG using UTF-8 encoding.
		boolean useCSS = true; // we want to use CSS style attributes
		//FileOutputStream output =;
		try (Writer out = new OutputStreamWriter( extracted(), "UTF-8")) {
			g.stream(out, useCSS);
		}

		String content = this.svgLinkable();
		IOUtils.write(content,  extracted(), "UTF-8");

	}

	private FileOutputStream extracted() throws FileNotFoundException {
		return new FileOutputStream(DRAWING_SVG);
	}

	public void setPolice(int police) {
		this.police = police;
	}

	/**
	 * This is to replace "&lt;" by "<" and "&gt;" by ">" because I did not found
	 * how to avoid converting < into &lt; and > into &gt;
	 **/
	@SuppressWarnings("resource")
	public String svgLinkable() throws FileNotFoundException, IOException {
		String content = IOUtils.toString(new FileInputStream(DRAWING_SVG), "UTF-8");
		content = content.replaceAll("&lt;", "<");
		content = content.replaceAll("&gt;", ">");
		return content = content.replaceAll("unicode=\"<\"", "unicode=\"\"");

	}

}
