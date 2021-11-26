/*
  This file is part of VisualTail, a graphical log file monitor.

  VisualTail is free software: you can redistribute it and/or modify it under
  the terms of the GNU General Public License as published by the Free Software
  Foundation, either version 3 of the License, or (at your option) any later
  version.

  VisualTail is distributed in the hope that it will be useful, but WITHOUT ANY
  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
  A PARTICULAR PURPOSE. See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with
  VisualTail. If not, see <http://www.gnu.org/licenses/>.
 */
package com.santiagolizardo.visualtail.gui.dialogs.components;

import static com.santiagolizardo.visualtail.resources.languages.Translator.tr;
import static javax.swing.SpringLayout.EAST;
import static javax.swing.SpringLayout.NORTH;
import static javax.swing.SpringLayout.SOUTH;
import static javax.swing.SpringLayout.WEST;

import java.awt.Dimension;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

import com.santiagolizardo.visualtail.beans.LogType;
import com.santiagolizardo.visualtail.config.LogTypeFileReader;
import com.santiagolizardo.visualtail.gui.renderers.LogTypeListRenderer;
import com.santiagolizardo.visualtail.gui.util.DialogFactory;
import com.santiagolizardo.visualtail.resources.languages.Translator;
import com.santiagolizardo.visualtail.config.LogTypeFileWriter;
import java.io.IOException;
import java.util.logging.Logger;

public class ListingPanel extends JPanel {


	private static final Logger logger = Logger.getLogger(ListingPanel.class.getName());

	private EditionPanel editionPanel;

	private final DefaultListModel<LogType> modelTypes;

	private JList<LogType> logTypesJList;
	private final JScrollPane scrollTypes;

	private final JButton addButton;
	private JButton renameButton;
	private JButton removeButton;

	private LogTypeFileReader logTypeManager;
	private final LogTypeFileWriter logTypeFileWriter;

	public ListingPanel() {
		setPreferredSize(new Dimension(180, 300));

		logTypeManager = LogTypeFileReader.getInstance();
		logTypeFileWriter = LogTypeFileWriter.getInstance();

		modelTypes = new DefaultListModel<>();
		logTypesJList = new JList<>(modelTypes);
		logTypesJList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		logTypesJList.setCellRenderer(new LogTypeListRenderer());
		logTypesJList.addListSelectionListener((ListSelectionEvent) -> {
			boolean isEmpty = logTypesJList.isSelectionEmpty();
			editionPanel.setEnabled(!isEmpty);
			if (isEmpty) {
				renameButton.setEnabled(false);
				removeButton.setEnabled(false);
			} else {
				LogType logType = (LogType) logTypesJList.getSelectedValue();
				logType = logTypeManager.read(logType
						.getName());
				
				editionPanel.setLogType(logType);
				renameButton.setEnabled(true);
				removeButton.setEnabled(true);
			}
		});

		scrollTypes = new JScrollPane(logTypesJList);

		addButton = new JButton(Translator.tr("Add"));
		addButton.addActionListener((ActionEvent) -> {
			onAddLogType();
		});

		renameButton = new JButton(Translator.tr("Rename"));
		renameButton.setEnabled(false);
		renameButton.addActionListener((ActionEvent) -> {
			onRenameLogType();
		});

		removeButton = new JButton(Translator.tr("Remove"));
		removeButton.setEnabled(false);
		removeButton.addActionListener((ActionEvent) -> {
			onRemoveLogType();
		});

		placeComponents();

		updateLogTypes();
	}

	public void setEditionPanel(EditionPanel editionPanel) {
		this.editionPanel = editionPanel;
	}

	private void placeComponents() {
		JLabel lblTypes = new JLabel(Translator.tr("Log types"));

		SpringLayout layout = new SpringLayout();
		setLayout(layout);

		layout.putConstraint(NORTH, lblTypes, 5, NORTH, this);
		layout.putConstraint(NORTH, scrollTypes, 5, SOUTH, lblTypes);
		layout.putConstraint(NORTH, addButton, 5, SOUTH, scrollTypes);
		layout.putConstraint(NORTH, renameButton, 5, SOUTH, addButton);
		layout.putConstraint(NORTH, removeButton, 5, SOUTH, renameButton);

		layout.putConstraint(WEST, lblTypes, 5, WEST, this);
		layout.putConstraint(WEST, scrollTypes, 5, WEST, this);
		layout.putConstraint(EAST, scrollTypes, -5, EAST, this);
		layout.putConstraint(WEST, addButton, 5, WEST, this);
		layout.putConstraint(EAST, addButton, -5, EAST, this);
		layout.putConstraint(WEST, renameButton, 5, WEST, this);
		layout.putConstraint(EAST, renameButton, -5, EAST, this);
		layout.putConstraint(WEST, removeButton, 5, WEST, this);
		layout.putConstraint(EAST, removeButton, -5, EAST, this);

		add(lblTypes);
		add(scrollTypes);
		add(addButton);
		add(renameButton);
		add(removeButton);
	}

	public void updateLogTypes() {
		modelTypes.clear();

		LogType[] logTypes = logTypeManager.readAll();
		for (LogType logType : logTypes) {
			if (!"Default".equals(logType.getName())) {
				modelTypes.addElement(logType);
			}
		}
	}

	private void onAddLogType() {
		String name = JOptionPane.showInputDialog(getParent(),
				Translator.tr("Enter the name of the new log type"));
		if (name == null) {
			return;
		}
		name = name.trim();
		if (name.length() == 0) {
			DialogFactory.showErrorMessage(getParent(),
					Translator.tr("Invalid log type name"));
			return;
		}
		LogType logType = new LogType(name);
		try {
			logTypeFileWriter.write(logType);

			updateLogTypes();

			logTypesJList.setSelectedValue(logType, true);
		} catch (IOException ee) {
			logger.warning(ee.getMessage());
		}
	}

	private void onRenameLogType() {
		LogType selected = (LogType) logTypesJList.getSelectedValue();
		String newName = JOptionPane.showInputDialog(getParent(),
				tr("Enter the new name of the log type:"),
				selected.getName());
		if (newName == null) {
			return;
		}
		newName = newName.trim();
		if (newName.length() > 0 && !newName.equals(selected.getName())) {
			if (logTypeFileWriter.rename(selected, newName)) {
				updateLogTypes();

				LogType newLogType = logTypeManager.read(newName);
				logTypesJList.setSelectedValue(newLogType, true);
			}
		}
	}

	private void onRemoveLogType() {
		if (DialogFactory.showQuestionDialog(getParent(), Translator
				.tr("Are you sure you want to delete the selected log type?"))) {
			LogType selected = (LogType) logTypesJList.getSelectedValue();
			if (!logTypeFileWriter.remove(selected)) {
				DialogFactory.showErrorMessage(getParent(),
						Translator.tr("Unable to delete the log type"));
			}

			updateLogTypes();
		}
	}
}
