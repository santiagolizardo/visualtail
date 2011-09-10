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
package org.slizardo.beobachter.gui.dialogs;

import static javax.swing.SpringLayout.EAST;
import static javax.swing.SpringLayout.NORTH;
import static javax.swing.SpringLayout.SOUTH;
import static javax.swing.SpringLayout.WEST;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SpringLayout.Constraints;

import org.slizardo.beobachter.MainGUI;
import org.slizardo.beobachter.beans.LogType;
import org.slizardo.beobachter.engine.Controller;
import org.slizardo.beobachter.gui.renderers.LogTypeListRenderer;
import org.slizardo.beobachter.gui.util.DialogFactory;
import org.slizardo.beobachter.gui.util.FileUtil;
import org.slizardo.beobachter.resources.languages.Translator;
import org.slizardo.beobachter.util.ArraysUtil;

/**
 * Graphic component for the "Open file" dialog.
 * 
 * @author slizardo
 * 
 */
public class OpenFileDialog extends JDialog {

	private static final long serialVersionUID = 8551819260159898361L;

	public OpenFileDialog() {
		setTitle(Translator.t("Open file..."));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);

		initComponents();
		placeComponents();
		configureEvents();
	}

	private JTextField tfFilePath;
	private JButton btnBrowse;
	private DefaultComboBoxModel logTypesModel;
	private JComboBox cbLogTypes;
	private JButton btnOpen;
	private JButton btnCancel;

	private void initComponents() {
		tfFilePath = new JTextField(50);
		btnBrowse = new JButton(Translator.t("Browse..."));

		logTypesModel = new DefaultComboBoxModel();
		Iterator<LogType> logTypes = ArraysUtil.arrayLogTypes().iterator();
		while (logTypes.hasNext()) {
			logTypesModel.addElement(logTypes.next());
		}

		cbLogTypes = new JComboBox(logTypesModel);
		cbLogTypes.setRenderer(new LogTypeListRenderer());

		btnOpen = new JButton(Translator.t("Open"));
		btnCancel = new JButton(Translator.t("Cancel"));
	}

	private void placeComponents() {
		JLabel lFilePath = new JLabel(Translator.t("File path"));
		JLabel lLogType = new JLabel(Translator.t("Log type"));

		Container container = getContentPane();
		((JPanel) container).setBorder(BorderFactory.createEmptyBorder(10, 10,
				10, 10));
		container.add(lFilePath);
		container.add(tfFilePath);
		container.add(btnBrowse);
		container.add(lLogType);
		container.add(cbLogTypes);
		container.add(btnOpen);
		container.add(btnCancel);

		SpringLayout spring = new SpringLayout();
		spring.putConstraint(NORTH, lFilePath, 0, NORTH, container);
		spring.putConstraint(NORTH, tfFilePath, 5, SOUTH, lFilePath);
		spring.putConstraint(NORTH, btnBrowse, 0, NORTH, tfFilePath);
		spring.putConstraint(NORTH, lLogType, 5, SOUTH, tfFilePath);
		spring.putConstraint(NORTH, cbLogTypes, 5, SOUTH, lLogType);
		spring.putConstraint(NORTH, btnOpen, 5, SOUTH, cbLogTypes);
		spring.putConstraint(NORTH, btnCancel, 0, NORTH, btnOpen);
		spring.putConstraint(WEST, btnBrowse, 5, EAST, tfFilePath);
		spring.putConstraint(EAST, btnCancel, 0, EAST, container);
		spring.putConstraint(EAST, btnOpen, -5, WEST, btnCancel);
		container.setLayout(spring);

		Constraints constraints = spring.getConstraints(container);
		constraints.setWidth(spring.getConstraint(EAST, btnBrowse));
		constraints.setHeight(spring.getConstraint(SOUTH, btnOpen));
		pack();
	}

	private void configureEvents() {
		btnBrowse.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				File lastSelected = new File(MainGUI.instance.configManager
						.getLastPath());

				JFileChooser chooser = new JFileChooser();
				chooser.setSelectedFile(lastSelected);
				int resp = chooser.showOpenDialog(OpenFileDialog.this);
				if (resp == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					tfFilePath.setText(file.getAbsolutePath());
				}
			}
		});
		btnOpen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				LogType logType = (LogType) cbLogTypes.getSelectedItem();

				String filePath = tfFilePath.getText();
				File file = new File(filePath);

				try {
					FileUtil.isReadable(file);
				} catch (Exception ex) {
					DialogFactory.showErrorMessage(null, ex.getMessage());
					return;
				}

				dispose();

				Controller.addRecent(filePath);
				Controller.openFile(filePath, logType);
			}
		});
		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
	}
}
