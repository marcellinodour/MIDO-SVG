package com.github.cocolollipop.mido_svg.view;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class GUISVGHome {

	private Button btnAjouterTags;

	private Button btnSupprimerTag;

	private Button GenererButton;

	private Button tagsbutton;

	private String USERNAME;

	protected Shell shlHome;
	
	

	public static void main(String[] args) throws Exception {
		new GUISVGHome().open("ocailloux");
	}


	/**
	 * Open the window.
	 */
	public void open(String username) {
		this.USERNAME = username;
		Display display = Display.getDefault();
		createContents();
		shlHome.open();
		shlHome.layout();
		createEvents();
		while (!shlHome.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	private void createEvents() {

		/** This button opens the GUI of settings */
		GenererButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				GUISVGGenerator svg = new GUISVGGenerator();
				try {
					shlHome.close();
					svg.open(USERNAME);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

		/** This button opens the GUI of Tags */
		tagsbutton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				btnAjouterTags.setVisible(true);
				btnSupprimerTag.setVisible(true);

			}
		});

		/** This button opens the GUI of Adding a Tag */
		btnAjouterTags.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				GUISVGTAGAjouter a = new GUISVGTAGAjouter();

				try {
					shlHome.close();
					a.open(USERNAME);
				} catch (JAXBException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

		/** This button opens the GUI of Deleting a Tag */
		btnSupprimerTag.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				GUISVGTAGSupprimer s = new GUISVGTAGSupprimer();
				shlHome.close();
				try {
					s.open(USERNAME);
				} catch (JAXBException | IOException e1) {
					// TODO Auto-generated catch block
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
		shlHome = new Shell();
		shlHome.setSize(503, 273);
		shlHome.setText("Home");

		tagsbutton = new Button(shlHome, SWT.NONE);

		tagsbutton.setBounds(178, 53, 159, 57);
		tagsbutton.setText("Gestion des Tags");

		GenererButton = new Button(shlHome, SWT.NONE);

		GenererButton.setBounds(178, 152, 159, 57);
		GenererButton.setText("Générer SVG");
		Label lblLogin = new Label(shlHome, SWT.NONE);
		lblLogin.setBounds(20, 20, 37, 21);
		lblLogin.setText(USERNAME);

		btnAjouterTags = new Button(shlHome, SWT.NONE);

		btnAjouterTags.setBounds(30, 69, 127, 28);
		btnAjouterTags.setText("Ajouter les Tags");
		btnAjouterTags.setVisible(false);

		btnSupprimerTag = new Button(shlHome, SWT.NONE);

		btnSupprimerTag.setBounds(354, 69, 139, 28);
		btnSupprimerTag.setText("Supprimer les Tags");
		btnSupprimerTag.setVisible(false);

	}
}
