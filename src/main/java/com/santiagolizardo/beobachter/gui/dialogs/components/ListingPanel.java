/**
 * Beobachter is a logs watcher for the desktop. (a.k.a. full-featured tail)
 * Copyright (C) 2011 Santiago Lizardo (http://www.santiagolizardo.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.santiagolizardo.beobachter.gui.dialogs.components;

import static javax.swing.SpringLayout.EAST;
import static javax.swing.SpringLayout.NORTH;
import static javax.swing.SpringLayout.SOUTH;
import static javax.swing.SpringLayout.WEST;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

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

import org.apache.commons.configuration.ConfigurationException;

import com.santiagolizardo.beobachter.beans.LogType;
import com.santiagolizardo.beobachter.config.EntitiesConfiguration;
import com.santiagolizardo.beobachter.gui.renderers.LogTypeListRenderer;
import com.santiagolizardo.beobachter.gui.util.DialogFactory;
import com.santiagolizardo.beobachter.resources.languages.Translator;
import com.santiagolizardo.beobachter.util.ArraysUtil;

public class ListingPanel extends JPanel {

	private static final long serialVersionUID = -4713179101047059768L;

	private EditionPanel editionPanel;

	private DefaultListModel modelTypes;

	private JList lstTypes;
	private JScrollPane scrollTypes;

	private JButton btnAdd;
	private JButton btnRename;
	private JButton btnRemove;

	public ListingPanel() {
		setPreferredSize(new Dimension(180, 340));

		modelTypes = new DefaultListModel();
		lstTypes = new JList(modelTypes);
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
					try {
						logType = EntitiesConfiguration.loadFromFile(logType
								.getName());
					} catch (ConfigurationException e1) {
						e1.printStackTrace();
					}
					editionPanel.setLogType(logType);
					btnRename.setEnabled(true);
					btnRemove.setEnabled(true);
				}
			}
		});

		scrollTypes = new JScrollPane(lstTypes);

		btnAdd = new JButton(Translator.t("Add"));
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = JOptionPane.showInputDialog(getParent(),
						Translator.t("Enter_the_name_of_the_new_log_type"));
				if (name != null && name.trim().length() > 0) {
					LogType logType = new LogType(name);
					try {
						EntitiesConfiguration.saveToFile(logType);

						updateLogTypes();
					} catch (Exception ee) {
						ee.printStackTrace();
					}
				}
			}
		});

		btnRename = new JButton(Translator.t("Rename"));
		btnRename.setEnabled(false);
		btnRename.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				LogType selected = (LogType) lstTypes.getSelectedValue();
				String newName = JOptionPane.showInputDialog(getParent(),
						"Enter the new name of the log type:",
						selected.getName());
				if (newName == null)
					return;
				newName = newName.trim();
				if (newName.length() > 0 && !newName.equals(selected.getName())) {
					File file = new File(selected.getPath());
					selected.setName(newName);
					File newFile = new File(selected.getPath());
					file.renameTo(newFile);
					try {
						EntitiesConfiguration.saveToFile(selected);
					} catch (Exception e) {
						e.printStackTrace();
					}
					updateLogTypes();
				}
			}
		});

		btnRemove = new JButton(Translator.t("Remove"));
		btnRemove.setEnabled(false);
		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (DialogFactory.showQuestionDialog(getParent(), Translator
						.t("Sure_you_want_to_delete_the_selected_log_type"))) {
					LogType selected = (LogType) lstTypes.getSelectedValue();
					File file = new File(selected.getPath());
					System.out.printf("Trying to delete '%s'...\n",
							file.getAbsolutePath());
					if (!file.delete()) {
						DialogFactory.showErrorMessage(getParent(),
								Translator.t("Unable_to_delete_the_log_type"));
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
		JLabel lblTypes = new JLabel(Translator.t("Log types"));

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
		Vector<LogType> logTypes = ArraysUtil.arrayLogTypes();
		for (LogType logType : logTypes) {
			if (!"Default".equals(logType.getName())) {
				modelTypes.addElement(logType);
			}
		}
	}
}
