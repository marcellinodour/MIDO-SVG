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

import com.github.cocolollipop.mido_svg.model.DataBase;
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

	private int lineCENTER = 50; // Makes the line arrive in the center of the

	// rectangle
	private int lineYDOWN = 7; // Makes the line go DOWN a little so the line is
								// not on the text

	private int lineYUP = -20; // Makes the line go UP a little so the line is
								// not on the text

	private int police;

	private String svgNS;

	/**
	 * This function shows the Admission of a "formation" (if this one is SHOWN in
	 * the SVG)
	 *
	 * The admission is written in BLUE
	 *
	 * @param admission
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
	 * This methode drawArrow, draws a line with an arrow
	 *
	 * @param g1
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
		Graphics2D g = (Graphics2D) g1.create();
		int ARR_SIZE = 5;

		double dx = x2 - x1, dy = y2 - y1;
		double angle = Math.atan2(dy, dx);
		int len = (int) Math.sqrt(dx * dx + dy * dy);
		AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
		at.concatenate(AffineTransform.getRotateInstance(angle));
		g.transform(at);

		// Draw horizontal arrow starting in (0, 0)
		g.drawLine(0, 0, len, 0);
		g.fillPolygon(new int[] { len, len - ARR_SIZE, len - ARR_SIZE, len }, new int[] { 0, -ARR_SIZE, ARR_SIZE, 0 },
				4);
	}

	/**
	 * Drawing of the objects the user has the choice between showing all
	 * "formations" or only "licence" or master for that he has to change the @param
	 * showOnly to (licenceOnly, masterOnly, both) then the rectangles arround the
	 * "formations" are drawn, lines also
	 *
	 * @param lineCENTER
	 * @param lineYDOWN
	 * @param lineYUP
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
			g.setPaint(Color.blue);
			for (Formation l2 : l.getAvailableFormations()) {
				// draw the lines between the formation and the avalaible
				// formations
				g.drawLine(l.getPoint().x + lineCENTER, l.getPoint().y + lineYDOWN, l2.getPoint().x + lineCENTER,
						l2.getPoint().y + lineYUP);

			}

		}

	}

	/**
	 * This function shows the name of the responsable of a formation (if this one
	 * is SHOWN in the SVG)
	 *
	 * The responsable name is written in GREEN
	 *
	 * @param responsable
	 *
	 */

	public void drawResponsable(Settings settings) {

		if (settings.isHiddenResponsable() == false) {

			g.setPaint(Color.green);

			for (Formation f : this.datas.getFormations()) {
				if (f.isShown() == true) {
					if (f.hasGotATeacher(f) == true) {
						g.drawString(f.getTeacher().getFullNameTeacher(),
								f.getPoint().x - (g.getFontMetrics().stringWidth(f.getFullName()) + 30),
								f.getPoint().y);
					}
				}
			}

			g.setPaint(Color.black);

		}

	}

	/**
	 * This function shows the name of the subjects or teachers of a formation (if
	 * this one is SHOWN in the SVG)
	 *
	 * If the subjects are not shown, teachers wont appear in the SVG also
	 *
	 * Either we'll have both, or only subjects
	 *
	 * The size of teachers is smaller and in RED
	 *
	 * this function draws also lines between a subject and his prerequisites
	 *
	 *
	 * @param settings
	 *
	 */

	public void drawSubjectTeacher(Settings settings) {
		int decX = 0;
		int decY = 0;
		if (settings.isHiddenSubject() == false) {

			for (Formation f : this.datas.getFormations()) {

				decX = g.getFontMetrics().stringWidth(f.getFullName()) + 20;

				if (f.isShown() == true) {
					for (Subject s : f.getSubjects()) {

						g.drawString(s.getTitle(), f.getPoint().x + decX, f.getPoint().y);
						s.setPosX(f.getPoint().x + decX);
						s.setPosY(f.getPoint().y);
						decX += g.getFontMetrics().stringWidth(s.getTitle()) + 7;

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
						// }
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
							decY += 15;

						}
					}

				}
			}

		}

	}

	/**
	 * fillListOfFormationToShow method fills the listOfFormationToShow with objects
	 * from FormationList that are of type typeOfFormation
	 *
	 * @param typeOfFormation
	 * @return
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

	@SuppressWarnings("resource")
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
		try (Writer out = new OutputStreamWriter(new FileOutputStream(DRAWING_SVG), "UTF-8")) {
			g.stream(out, useCSS);
		}

		String content = this.svgLinkable();
		IOUtils.write(content, new FileOutputStream(DRAWING_SVG), "UTF-8");

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
