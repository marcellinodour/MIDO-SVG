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

import com.github.cocolollipop.mido_svg.controller.ControllerJAXB;
import com.github.cocolollipop.mido_svg.database.DataBase;
import com.github.cocolollipop.mido_svg.xml.jaxb.model.Tag;

public class GUISVGTAGSupprimer {

	private Button btnHome;

	private Button btnSupprimer;

	private DataBase data = new DataBase();

	private ControllerJAXB jaxb = new ControllerJAXB();

	private Label lblListeDesMatires;

	private List listMatassociees;

	private List listTags;

	private Map<String, com.github.cocolollipop.mido_svg.university.components.Subject> map = data.getSubjects();

	private Set<Tag> tags;

	private String USERNAME;

	protected Shell shlSupprimerTag;

	public Map<String, com.github.cocolollipop.mido_svg.university.components.Subject> getMap() {
		return map;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	/**
	 * Open the window.
	 *
	 * @throws IOException
	 * @throws JAXBException
	 */
	public void open(String username) throws JAXBException, IOException {
		this.USERNAME = username;
		Display display = Display.getDefault();
		createContents();
		shlSupprimerTag.open();
		shlSupprimerTag.layout();
		initTagsList();
		createEvents();
		while (!shlSupprimerTag.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public void setMap(Map<String, com.github.cocolollipop.mido_svg.university.components.Subject> map) {
		this.map = map;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
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
				h.open(USERNAME);
				shlSupprimerTag.close();

			}
		});

		/** This button "Supprimer" removes the selected Tag from the List **/

		btnSupprimer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				java.util.List<Tag> userListOfTags;
				try {
					// we get the user's tags
					userListOfTags = jaxb.readTagsFileXML(USERNAME);
				} catch (@SuppressWarnings("unused") JAXBException | IOException e1) {
					throw new IllegalStateException();
				}
				for (Tag tag1 : userListOfTags) {
					if (tag1.getName().equals(listTags.getSelection()[0])) {
						// we remove the selected tag from the user's tags
						userListOfTags.remove(tag1);
					}
					try {
						// we save the action by replacing the old user's tags
						// file by
						// the new one
						jaxb.createTagsFileXML(USERNAME, userListOfTags);
					} catch (@SuppressWarnings("unused") JAXBException e1) {
						throw new IllegalStateException();
					}
				}
			}

		});
	}

	/**
	 * This subroutine is used to get the subjects linked to a specific tag which is
	 * given in param in
	 *
	 * @param specificTag
	 * @return
	 */
	private Set<?> getSubjects(String specificTag) {
		java.util.List<Tag> listOfTags;
		try {
			listOfTags = jaxb.readTagsFileXML(USERNAME);
		} catch (JAXBException e) {
			throw new IllegalStateException(e);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		for (Tag tag : listOfTags) {
			if (tag.getName().equals(specificTag)) {
				return tag.getSubjects();

			}
		}
		return null;
	}

	/**
	 * Initialise ListTags
	 *
	 * Add All Tags to ListTags
	 *
	 * @throws IOException
	 * @throws JAXBException
	 *
	 */
	private void initTagsList() throws JAXBException, IOException {
		// we get the tags from the user's tags file
		java.util.List<Tag> userListOfTags = jaxb.readTagsFileXML(USERNAME);
		Set<Tag> tagsSet = new HashSet<>();
		for (Tag tag : userListOfTags) {
			tagsSet.add(tag);
		}

		/* Adding the Tags to the Jlist of tags */
		for (Tag tag : tagsSet) {
			listTags.add(tag.getName());
		}

	}

	/**
	 * Create contents of the window.
	 *
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		shlSupprimerTag = new Shell();
		shlSupprimerTag.setSize(577, 293);
		shlSupprimerTag.setText("Supprimer Tag");

		Label lblLogin = new Label(shlSupprimerTag, SWT.NONE);
		lblLogin.setBounds(21, 42, 210, 133);
		lblLogin.setText(USERNAME);

		Label lblSupprimerTags = new Label(shlSupprimerTag, SWT.NONE);
		lblSupprimerTags.setBounds(190, 27, 109, 14);
		lblSupprimerTags.setText("Supprimer Tags");

		Label lblListeDesTags = new Label(shlSupprimerTag, SWT.NONE);
		lblListeDesTags.setBounds(34, 72, 125, 14);
		lblListeDesTags.setText("Liste des tags :");

		listTags = new List(shlSupprimerTag, SWT.BORDER);
		listTags.setBounds(31, 92, 197, 110);

		btnSupprimer = new Button(shlSupprimerTag, SWT.NONE);

		btnSupprimer.setBounds(473, 228, 94, 28);
		btnSupprimer.setText("Supprimer");

		btnHome = new Button(shlSupprimerTag, SWT.NONE);

		btnHome.setBounds(10, 228, 73, 28);
		btnHome.setText("Home");

		lblListeDesMatires = new Label(shlSupprimerTag, SWT.NONE);
		lblListeDesMatires.setBounds(346, 72, 185, 14);
		lblListeDesMatires.setText("Liste des matières associées :");

		listMatassociees = new List(shlSupprimerTag, SWT.BORDER);
		listMatassociees.setEnabled(false);
		listMatassociees.setBounds(346, 92, 197, 110);

	}
}
