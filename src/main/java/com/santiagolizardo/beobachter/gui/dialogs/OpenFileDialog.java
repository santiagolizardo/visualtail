/**
 * Beobachter is a logs watcher for the desktop. (a.k.a. full-featured tail)
 * Copyright (C) 2013 Santiago Lizardo (http://www.santiagolizardo.com)
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
package com.santiagolizardo.beobachter.gui.dialogs;

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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SpringLayout.Constraints;

import com.santiagolizardo.beobachter.MainGUI;
import com.santiagolizardo.beobachter.beans.LogType;
import com.santiagolizardo.beobachter.engine.Controller;
import com.santiagolizardo.beobachter.gui.renderers.LogTypeListRenderer;
import com.santiagolizardo.beobachter.gui.util.DialogFactory;
import com.santiagolizardo.beobachter.gui.util.FileUtil;
import com.santiagolizardo.beobachter.resources.languages.Translator;
import com.santiagolizardo.beobachter.util.ArraysUtil;

/**
 * Graphic component for the "Open file" dialog.
 * 
 * @author slizardo
 * 
 */
public class OpenFileDialog extends AbstractDialog {

	private static final long serialVersionUID = 8551819260159898361L;

	private JTextField tfFilePath;
	private JButton btnBrowse;
	private DefaultComboBoxModel<LogType> logTypesModel;
	private JComboBox<LogType> cbLogTypes;
	private JButton btnOpen;
	private JButton btnCancel;

	public OpenFileDialog(JFrame parentFrame) {
		super(parentFrame);
		
		setTitle(Translator._("Open file..."));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);

		initComponents();
		placeComponents();
		configureEvents();
	}

	@Override
	protected JRootPane createRootPane() {
		JRootPane rootPane = super.createRootPane();
		rootPane.setDefaultButton(btnOpen);
		return rootPane;
	}

	private void initComponents() {
		tfFilePath = new JTextField(50);
		btnBrowse = new JButton(Translator._("Browse..."));

		logTypesModel = new DefaultComboBoxModel<LogType>();
		Iterator<LogType> logTypes = ArraysUtil.arrayLogTypes().iterator();
		while (logTypes.hasNext()) {
			logTypesModel.addElement(logTypes.next());
		}

		cbLogTypes = new JComboBox<LogType>(logTypesModel);
		cbLogTypes.setRenderer(new LogTypeListRenderer());

		btnOpen = new JButton(Translator._("Open"));
		btnCancel = new JButton(Translator._("Cancel"));
	}

	private void placeComponents() {
		JLabel lFilePath = new JLabel(Translator._("File path"));
		JLabel lLogType = new JLabel(Translator._("Log type"));

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
		setLocationRelativeTo(parentFrame);
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

				setVisible(false);

				Controller.addRecent(filePath);
				Controller.openFile(filePath, logType);

				dispose();
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
