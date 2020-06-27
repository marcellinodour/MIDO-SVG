package com.github.cocolollipop.mido_svg.view;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.github.cocolollipop.mido_svg.controller.ControllerJAXB;
import com.github.cocolollipop.mido_svg.database.DataBase;
import com.github.cocolollipop.mido_svg.xml.jaxb.model.Tag;

public class GUISVGTAGAjouter {

	private Button btnAjouter;

	private Button btnHome;

	private Button button;

	private Button button_1;

	private DataBase data = new DataBase();

	private ControllerJAXB jaxb = new ControllerJAXB();

	private List listMatassociees;

	private List listSujets1;

	private List listSujets2;

	private List listTags;

	private Map<String, com.github.cocolollipop.mido_svg.university.components.Subject> map = data.getSubjects();

	private String selectedSubject;

	private Tag tag = new Tag();

	private Text textNomTag;

	protected Shell shlAjouterTags;

	public Tag getTag() {
		return tag;
	}

	/**
	 * Open the window.
	 *
	 * @throws IOException
	 * @throws JAXBException
	 */
	public void open() throws JAXBException, IOException {
		Display display = Display.getDefault();
		createContents();
		shlAjouterTags.open();
		shlAjouterTags.layout();
		initTagsList();
		createEvents();
		while (!shlAjouterTags.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	private void createEvents() {

		/**
		 * Listener used to display the subjects which are linked to the selected tag
		 */
		listTags.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				listMatassociees.removeAll();
				String string = listTags.getSelection()[0];
				Set<?> listMat = getSubjects(string);
				for (Object str : listMat) {
					listMatassociees.add((String) str);
				}
			}

		});

		/** This button "Home" opens the GUI home **/

		btnHome.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				GUISVGHome h = new GUISVGHome();
				shlAjouterTags.close();
				h.open();

			}
		});

		/** This button ">>" adds a selected subject to a list **/

		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (listSujets1.getSelectionCount() == 0) {
					return;
				}
				selectedSubject = listSujets1.getSelection()[0];
				listSujets2.add(selectedSubject);
				listSujets1.remove(listSujets1.getSelectionIndex());

			}
		});

		/** This button "<<" removes un subject from a list **/

		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (listSujets2.getSelectionCount() == 0) {
					return;
				}
				selectedSubject = listSujets2.getSelection()[0];
				listSujets1.add(selectedSubject);
				listSujets2.remove(listSujets2.getSelectionIndex());
			}
		});

		/**
		 * This button " Ajouter" adds a tag with the subjects associeted to it
		 **/

		btnAjouter.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				java.util.List<Tag> userListOfTags;
				try {
					userListOfTags = jaxb.readTagsFileXML();
				} catch (@SuppressWarnings("unused") JAXBException | IOException e2) {
					throw new IllegalStateException();
				}
				Tag newTag = new Tag();
				newTag.setName(textNomTag.getText());
				for (int i = 0; i < listSujets2.getItems().length; i++) {
					newTag.addSubject(listSujets2.getItem(i));
				}
				userListOfTags.add(newTag);

				try {
					jaxb.createTagsFileXML(userListOfTags);
				} catch (JAXBException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

	}

	/**
	 * This subroutine is used to get the subjects linked to a specific tag which is
	 * given in param in
	 *
	 * @param string
	 * @return
	 */
	private Set<?> getSubjects(String string) {
		java.util.List<Tag> listOfTags;
		try {
			listOfTags = jaxb.readTagsFileXML();
		} catch (JAXBException e) {
			throw new IllegalStateException(e);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		for (Tag tag1 : listOfTags) {
			if (tag1.getName().equals(string)) {
				return tag1.getSubjects();

			}
		}
		return null;
	}

	/**
	 * Initialise ListTags
	 *
	 * 1- Adds All Subjects to ListSubject1 of GUI 2- Adds All Tags to listTags of
	 * GUI
	 *
	 * @throws IOException
	 * @throws JAXBException
	 *
	 */
	private void initTagsList() throws JAXBException, IOException {
		java.util.List<Tag> userListOfTags = jaxb.readTagsFileXML();
		Set<Tag> tagsSet = new HashSet<>();

		/* Adding the subjects to the Jlist of Subjetcs */
		for (String name : map.keySet()) {
			String value = map.get(name).getTitle();
			listSujets1.add(value);

			/* Adding the Tags to the Jlist of tags */
			for (Tag tag1 : userListOfTags) {
				tagsSet.add(tag1);

			}
		}

		for (Tag tag1 : tagsSet) {
			listTags.add(tag1.getName());
		}
	}

	/**
	 * Create contents of the window.
	 *
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		shlAjouterTags = new Shell();
		shlAjouterTags.setSize(634, 427);
		shlAjouterTags.setText("Ajouter Tags");

		Label lblAjouterTags = new Label(shlAjouterTags, SWT.NONE);
		lblAjouterTags.setBounds(210, 10, 137, 14);
		lblAjouterTags.setText("Ajouter Tags");

		Label lblListeDeM = new Label(shlAjouterTags, SWT.NONE);
		lblListeDeM.setBounds(25, 60, 186, 14);
		lblListeDeM.setText("Liste de mes tags :");

		listTags = new List(shlAjouterTags, SWT.BORDER | SWT.H_SCROLL);
		listTags.setBounds(138, 43, 150, 65);

		Label lblAjouterUnTag = new Label(shlAjouterTags, SWT.NONE);
		lblAjouterUnTag.setBounds(25, 127, 128, 14);
		lblAjouterUnTag.setText("Ajouter un Tag :");

		Label lblNomDuTag = new Label(shlAjouterTags, SWT.NONE);
		lblNomDuTag.setBounds(187, 165, 106, 14);
		lblNomDuTag.setText("Nom du Tag : ");

		textNomTag = new Text(shlAjouterTags, SWT.BORDER);
		textNomTag.setBounds(341, 162, 150, 19);

		Label lblSujetsAssocier = new Label(shlAjouterTags, SWT.NONE);
		lblSujetsAssocier.setBounds(25, 211, 128, 14);
		lblSujetsAssocier.setText("Sujets à associer:");

		listSujets1 = new List(shlAjouterTags, SWT.BORDER);
		listSujets1.setBounds(103, 231, 164, 104);

		listSujets2 = new List(shlAjouterTags, SWT.BORDER);
		listSujets2.setBounds(431, 231, 164, 104);

		button = new Button(shlAjouterTags, SWT.NONE);

		button.setBounds(317, 246, 47, 28);
		button.setText(">>");

		button_1 = new Button(shlAjouterTags, SWT.NONE);

		button_1.setText("<<");
		button_1.setBounds(317, 280, 47, 28);

		btnHome = new Button(shlAjouterTags, SWT.NONE);

		btnHome.setBounds(35, 363, 57, 28);
		btnHome.setText("Home");

		btnAjouter = new Button(shlAjouterTags, SWT.NONE);

		btnAjouter.setBounds(464, 363, 94, 28);
		btnAjouter.setText("Ajouter");

		listMatassociees = new List(shlAjouterTags, SWT.BORDER);
		listMatassociees.setEnabled(false);
		listMatassociees.setBounds(464, 43, 160, 65);

		Label lblLesMatiresAssocies = new Label(shlAjouterTags, SWT.NONE);
		lblLesMatiresAssocies.setBounds(317, 60, 128, 14);
		lblLesMatiresAssocies.setText("Les matières associées:");

	}
}
