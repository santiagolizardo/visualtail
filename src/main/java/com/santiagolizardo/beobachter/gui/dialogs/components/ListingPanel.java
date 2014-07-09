/**
 * This file is part of Beobachter, a graphical log file monitor.
 *
 * Beobachter is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Beobachter is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Beobachter. If not, see <http://www.gnu.org/licenses/>.
 */
package com.santiagolizardo.beobachter.gui.dialogs.components;

import static com.santiagolizardo.beobachter.resources.languages.Translator.tr;
import static javax.swing.SpringLayout.EAST;
import static javax.swing.SpringLayout.NORTH;
import static javax.swing.SpringLayout.SOUTH;
import static javax.swing.SpringLayout.WEST;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.santiagolizardo.beobachter.beans.LogType;
import com.santiagolizardo.beobachter.gui.renderers.LogTypeListRenderer;
import com.santiagolizardo.beobachter.gui.util.DialogFactory;
import com.santiagolizardo.beobachter.resources.languages.Translator;
import com.santiagolizardo.beobachter.beans.LogTypeManager;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class ListingPanel extends JPanel {

	private static final long serialVersionUID = -4713179101047059768L;

	private static final Logger logger = Logger.getLogger(ListingPanel.class.getName());

	private EditionPanel editionPanel;

	private DefaultListModel<LogType> modelTypes;

	private JList<LogType> lstTypes;
	private JScrollPane scrollTypes;

	private JButton btnAdd;
	private JButton btnRename;
	private JButton btnRemove;

	public ListingPanel() {
		setPreferredSize(new Dimension(180, 300));

		modelTypes = new DefaultListModel<>();
		lstTypes = new JList<>(modelTypes);
		lstTypes.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		lstTypes.setCellRenderer(new LogTypeListRenderer());
		lstTypes.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				boolean isEmpty = lstTypes.isSelectionEmpty();
				editionPanel.setEnabled(!isEmpty);
				if (isEmpty) {
					btnRename.setEnabled(false);
					btnRemove.setEnabled(false);
				} else {
					LogType logType = (LogType) lstTypes.getSelectedValue();
					LogTypeManager logTypes = LogTypeManager.getInstance();
					logType = logTypes.loadFromFile(logType
							.getName());

					editionPanel.setLogType(logType);
					btnRename.setEnabled(true);
					btnRemove.setEnabled(true);
				}
			}
		});

		scrollTypes = new JScrollPane(lstTypes);

		btnAdd = new JButton(Translator.tr("Add"));
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = JOptionPane.showInputDialog(getParent(),
						Translator.tr("Enter the name of the new log type"));
				if (name == null) {
					return;
				}
				name = name.trim();
				if (name.length() == 0) {
					DialogFactory.showErrorMessage(null,
							Translator.tr("Invalid log type name"));
					return;
				}
				LogType logType = new LogType(name);
				try {
					LogTypeManager logTypes = LogTypeManager.getInstance();
					logTypes.saveToFile(logType);

					updateLogTypes();
				} catch (IOException ee) {
					logger.warning(ee.getMessage());
				}
			}
		});

		btnRename = new JButton(Translator.tr("Rename"));
		btnRename.setEnabled(false);
		btnRename.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				LogType selected = (LogType) lstTypes.getSelectedValue();
				String newName = JOptionPane.showInputDialog(getParent(),
						tr("Enter the new name of the log type:"),
						selected.getName());
				if (newName == null) {
					return;
				}
				newName = newName.trim();
				if (newName.length() > 0 && !newName.equals(selected.getName())) {
					LogTypeManager logTypes = LogTypeManager.getInstance();
					logTypes.rename(selected, newName);

					updateLogTypes();
				}
			}
		});

		btnRemove = new JButton(Translator.tr("Remove"));
		btnRemove.setEnabled(false);
		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				if (DialogFactory.showQuestionDialog(getParent(), Translator
						.tr("Are you sure you want to delete the selected log type?"))) {
					LogType selected = (LogType) lstTypes.getSelectedValue();
					LogTypeManager logTypes = LogTypeManager.getInstance();
					if (!logTypes.remove(selected)) {
						DialogFactory.showErrorMessage(getParent(),
								Translator.tr("Unable to delete the log type"));
					}

					updateLogTypes();
				}
			}
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
		layout.putConstraint(NORTH, btnAdd, 5, SOUTH, scrollTypes);
		layout.putConstraint(NORTH, btnRename, 5, SOUTH, btnAdd);
		layout.putConstraint(NORTH, btnRemove, 5, SOUTH, btnRename);

		layout.putConstraint(WEST, lblTypes, 5, WEST, this);
		layout.putConstraint(WEST, scrollTypes, 5, WEST, this);
		layout.putConstraint(EAST, scrollTypes, -5, EAST, this);
		layout.putConstraint(WEST, btnAdd, 5, WEST, this);
		layout.putConstraint(EAST, btnAdd, -5, EAST, this);
		layout.putConstraint(WEST, btnRename, 5, WEST, this);
		layout.putConstraint(EAST, btnRename, -5, EAST, this);
		layout.putConstraint(WEST, btnRemove, 5, WEST, this);
		layout.putConstraint(EAST, btnRemove, -5, EAST, this);

		add(lblTypes);
		add(scrollTypes);
		add(btnAdd);
		add(btnRename);
		add(btnRemove);
	}

	public void updateLogTypes() {
		modelTypes.clear();

		LogTypeManager logTypesLoader = LogTypeManager.getInstance();
		List<LogType> logTypes = logTypesLoader.getAll();
		for (LogType logType : logTypes) {
			if (!"Default".equals(logType.getName())) {
				modelTypes.addElement(logType);
			}
		}
	}
}
