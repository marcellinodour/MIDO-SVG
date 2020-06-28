package com.github.cocolollipop.mido_svg.view;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.cocolollipop.mido_svg.database.DataBase;
import com.github.cocolollipop.mido_svg.svg_generator.DrawerSVGGen;
import com.github.cocolollipop.mido_svg.svg_generator.ResponsiveSVG;
import com.github.cocolollipop.mido_svg.svg_generator.Settings;

public class GUISVGGenerator {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(GUISVGGenerator.class);

	public static void main(String[] args) throws Exception {
		new GUISVGGenerator().open("ocailloux");
	}

	private boolean affAdmission;

	private boolean affFormationLicence;

	private boolean affFormationMaster;

	private boolean affPrereq;

	private boolean affResponsable;

	private boolean affSubject;

	private boolean affTeacher;

	private Button btnCheckButtonA3;

	private Button btnCheckButtonA4;

	private Button btnCheckButtonAutre;

	private Button btnFermer;

	private Button btnLeModeDadmission;

	private Button btnLesEnseignants;

	private Button btnLesMatires;

	private Button btnLesprerequis;

	private Button btnLesResponsables;

	private Button btnLicence;

	private Button btnMaster;

	private Button btnPush;

	private DataBase datas;

	private int height;

	private Label labelEtat;

	private Label lblChoixDeLaffichage;

	private Label lblEtou;

	private Label lblLargeur;

	private Label lblLongueur;

	private Label lblMsg1;

	private Label lblOptionsDaffichage;

	private Settings settings;

	private Spinner spinnerheight;

	private Spinner spinnerwidth;

	private DrawerSVGGen svg = new DrawerSVGGen();

	private String USERNAME;

	private int width;

	protected Shell shell;

	ResponsiveSVG responsive = new ResponsiveSVG();

	/**
	 * Open the window.
	 *
	 * @throws IOException
	 */

	public void open(String username) throws IOException {
		this.USERNAME = username;
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		createEvents();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	private void createEvents() {

		btnCheckButtonAutre.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnCheckButtonAutre.getSelection()) {

					lblMsg1.setText("Saisissez le format souhaité :");
					lblLargeur.setText("Largeur");
					lblLongueur.setText("Longueur");

					spinnerwidth = new Spinner(shell, SWT.BORDER);
					spinnerwidth.setMaximum(5000);
					spinnerwidth.setMinimum(1000);
					spinnerwidth.setBounds(469, 141, 71, 27);

					spinnerheight = new Spinner(shell, SWT.BORDER);
					spinnerheight.setMaximum(5000);
					spinnerheight.setMinimum(1000);
					spinnerheight.setBounds(469, 181, 71, 28);

				}

			}
		});

		/**
		 * Check box to choose Licence
		 *
		 */

		btnLicence.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnLicence.getSelection()) {
					affFormationLicence = false;
				} else {
					affFormationLicence = true;
				}

			}
		});

		/**
		 * Check box to choose Master
		 *
		 */

		btnMaster.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (btnMaster.getSelection()) {
					affFormationMaster = false;
				} else {
					affFormationMaster = true;
				}
			}
		});

		/**
		 * Check box to choose display responsibles of a Formation
		 *
		 */

		btnLesResponsables.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (btnLesResponsables.getSelection()) {
					affResponsable = false;
				} else {
					affResponsable = true;

				}
			}
		});

		/**
		 * Check box to choose display Subjects of a Formation
		 *
		 */

		btnLesMatires.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnLesMatires.getSelection()) {
					affSubject = false;
				} else {
					affSubject = true;

				}

			}
		});

		btnLesEnseignants.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnLesEnseignants.getSelection()) {
					affTeacher = false;
				} else {
					affTeacher = true;

				}
			}
		});

		/**
		 * Check box to choose display Prerequisites of a subject
		 *
		 */

		btnLesprerequis.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (btnLesprerequis.getSelection()) {
					affPrereq = false;
				} else {
					affPrereq = true;

				}
			}
		});

		/**
		 * Check box to choose display "Admission" or not
		 *
		 */
		btnLeModeDadmission.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (btnLeModeDadmission.getSelection()) {
					affAdmission = false;
				} else {
					affAdmission = true;

				}
			}
		});

		/**** Close Button ****/

		btnFermer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});

		/**** Push Button to generate the SVG ****/

		btnPush.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				try {
					/*
					 * If this radio button is selected form = "A3"
					 */
					if (btnCheckButtonA3.getSelection()) { 
						settings = new Settings(affFormationLicence, affFormationMaster, affResponsable, affAdmission,
								affSubject, affTeacher, affPrereq, "A3");

					} 
					/*
					 * If this radio button is selected form = "A4"
					 */
					else if (btnCheckButtonA4.getSelection()) {
						settings = new Settings(affFormationLicence, affFormationMaster, affResponsable, affAdmission,
								affSubject, affTeacher, affPrereq, "A4");

					} 
					/*
					 * Else If this radio button is selected the user get to choose his own values height, width
					 */
					else if (btnCheckButtonAutre.getSelection()) {
						/*
						 * we should get back the values that the user has entered in the spinners
						 */
						height = spinnerheight.getSelection();
						width = spinnerwidth.getSelection();

						settings = new Settings(affFormationLicence, affFormationMaster, affResponsable, affAdmission,
								affSubject, affTeacher, affPrereq, width, height);
					}

					if( (affFormationLicence == true) && (affFormationMaster == true) ) {
						labelEtat.setBounds(148, 467, 300, 18);
						labelEtat.setText(" Veuillez sélectionner un choix de parcours ");
					}
					else {
						datas = new DataBase(settings);
						responsive.defineObjectsPosition(datas.getFormations(), settings.getWidth(), settings.getHeight(), settings.isHiddenSubject(), settings.isHiddenLicence(), settings.isHiddenMaster());
						LOGGER.info("Painting.");
						svg.paint(settings, datas);

						File file = new File(DrawerSVGGen.DRAWING_SVG);
						file.createNewFile();
					

						try {
							java.awt.Desktop.getDesktop().open(file);
						} catch (IOException exc) {
							System.out.println("Exception: " + exc.toString());
						}
						labelEtat.setBounds(235, 467, 183, 20);
						labelEtat.setText(" SVG généré ");
					}
					

				} catch (Exception e1) {
					e1.printStackTrace();
				}

				
			}
		});

	}

	/**
	 * Create contents of the window.
	 *
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(550, 592);
		shell.setText("SWT Application");
		shell.setLayout(null);

		Label lblLogin = new Label(shell, SWT.NONE);
		lblLogin.setBounds(20, 20, 300, 200);
		lblLogin.setText(USERNAME);

		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(226, 47, 108, 27);
		lblNewLabel.setText("MIDO Application");

		Label lblChoixDuFormat = new Label(shell, SWT.NONE);
		lblChoixDuFormat.setBounds(32, 130, 102, 18);
		lblChoixDuFormat.setText("Choix du format: ");

		btnCheckButtonA3 = new Button(shell, SWT.RADIO);

		btnCheckButtonA3.setBounds(52, 160, 94, 18);
		btnCheckButtonA3.setText("A3");

		btnCheckButtonA4 = new Button(shell, SWT.RADIO);
		btnCheckButtonA4.setSelection(true);

		btnCheckButtonA4.setBounds(170, 160, 94, 18);
		btnCheckButtonA4.setText("A4");

		btnCheckButtonAutre = new Button(shell, SWT.RADIO);

		btnCheckButtonAutre.setBounds(287, 160, 94, 18);
		btnCheckButtonAutre.setText("Autre");

		lblLargeur = new Label(shell, SWT.NONE);
		lblLargeur.setBounds(404, 146, 59, 18);
		lblLargeur.setText("");

		lblLongueur = new Label(shell, SWT.NONE);
		lblLongueur.setBounds(404, 186, 59, 18);
		lblLongueur.setText("");

		lblMsg1 = new Label(shell, SWT.NONE);
		lblMsg1.setBounds(374, 108, 166, 18);
		lblMsg1.setText("");

		lblChoixDeLaffichage = new Label(shell, SWT.NONE);
		lblChoixDeLaffichage.setBounds(32, 222, 128, 18);
		lblChoixDeLaffichage.setText("Choix de Parcours :");

		btnLesprerequis = new Button(shell, SWT.CHECK);
		btnLesprerequis.setSelection(true);

		btnLesprerequis.setBounds(131, 393, 152, 18);
		btnLesprerequis.setText("Les Prérequis");

		btnLesEnseignants = new Button(shell, SWT.CHECK);
		btnLesEnseignants.setSelection(true);

		btnLesEnseignants.setBounds(380, 348, 143, 18);
		btnLesEnseignants.setText("Les enseignants");

		btnLesMatires = new Button(shell, SWT.CHECK);
		btnLesMatires.setSelection(true);

		btnLesMatires.setBounds(226, 348, 119, 18);
		btnLesMatires.setText("Les matières");

		btnLesResponsables = new Button(shell, SWT.CHECK);
		btnLesResponsables.setSelection(true);

		btnLesResponsables.setBounds(52, 348, 152, 18);
		btnLesResponsables.setText("Les responsables ");

		btnLicence = new Button(shell, SWT.CHECK);
		btnLicence.setSelection(true);

		btnLicence.setBounds(131, 257, 90, 18);
		btnLicence.setText("Licence ");

		btnMaster = new Button(shell, SWT.CHECK);
		btnMaster.setSelection(true);

		btnMaster.setBounds(342, 257, 90, 18);
		btnMaster.setText("Master");

		lblEtou = new Label(shell, SWT.NONE);
		lblEtou.setBounds(260, 260, 59, 18);
		lblEtou.setText("Et/Ou");

		lblOptionsDaffichage = new Label(shell, SWT.NONE);
		lblOptionsDaffichage.setBounds(32, 305, 114, 18);
		lblOptionsDaffichage.setText("Options d'affichage :");

		btnLeModeDadmission = new Button(shell, SWT.CHECK);
		btnLeModeDadmission.setSelection(true);

		btnLeModeDadmission.setBounds(314, 393, 183, 18);
		btnLeModeDadmission.setText("Le mode d'admission");

		btnPush = new Button(shell, SWT.NONE);

		btnPush.setBounds(404, 516, 119, 28);
		btnPush.setText("Générer le SVG");

		btnFermer = new Button(shell, SWT.NONE);

		btnFermer.setBounds(32, 516, 94, 28);
		btnFermer.setText("Fermer");

		labelEtat = new Label(shell, SWT.NONE);
		labelEtat.setBounds(185, 467, 183, 18);

	}
}
