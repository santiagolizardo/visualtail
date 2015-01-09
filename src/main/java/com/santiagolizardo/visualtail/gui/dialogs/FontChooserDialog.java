/**
 * This file is part of VisualTail, a graphical log file monitor.
 *
 * VisualTail is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VisualTail is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VisualTail.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.santiagolizardo.visualtail.gui.dialogs;

import static javax.swing.SpringLayout.EAST;
import static javax.swing.SpringLayout.SOUTH;

import java.awt.Container;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.SpringLayout.Constraints;

import com.santiagolizardo.visualtail.gui.MainWindow;
import com.santiagolizardo.visualtail.config.ConfigData;
import com.santiagolizardo.visualtail.config.ConfigPersistence;
import com.santiagolizardo.visualtail.resources.languages.Translator;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JInternalFrame;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class FontChooserDialog extends AbstractDialog {

	private static final long serialVersionUID = -4534774464099526206L;

	private static final Logger logger = Logger
			.getLogger(FontChooserDialog.class.getName());

	private JComboBox<String> fontFamilyCombo;
	private JSpinner fontSizeCombo;
	private JTextField previewTextField;

	private JButton okButton;
	private JButton cancelButton;

	public FontChooserDialog(final MainWindow mainWindow) {
		super(mainWindow);

		final ConfigData configManager = mainWindow.getConfigData();

		setTitle(Translator.tr("Font settings"));
		setResizable(false);
		setModal(true);

		String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getAvailableFontFamilyNames();
		fontFamilyCombo = new JComboBox<>(fonts);
		fontFamilyCombo.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					updatePreview();
				}
			}
		});

		fontSizeCombo = new JSpinner(new SpinnerNumberModel(10,
				8, 72, 1));
		fontSizeCombo.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				updatePreview();
			}
		});

		previewTextField = new JTextField("[INFO] Log line example");

		okButton = new JButton(Translator.tr("Save"));
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				setVisible(false);

				Font font = getSelectedFont();

				configManager.setFontFamily(fontFamilyCombo.getSelectedItem()
						.toString());
				configManager.setFontSize(Integer.parseInt(fontSizeCombo.getValue()
						.toString()));

				ConfigPersistence configPersistence = new ConfigPersistence();
				configPersistence.saveProperties(mainWindow,
						configManager.getConfiguration());

				JInternalFrame[] internalFrames = mainWindow.getDesktop().getAllFrames();
				for (JInternalFrame internalFrame : internalFrames) {
					LogWindow logWindow = (LogWindow) internalFrame;
					logWindow.updateFont(font);
				}

				dispose();
			}
		});

		cancelButton = new JButton(Translator.tr("Cancel"));
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				setVisible(false);
				dispose();
			}
		});

		fontFamilyCombo.setSelectedItem(configManager.getFontFamily());
		fontSizeCombo.setValue(configManager.getFontSize());
		updatePreview();

		defineLayout();
	}

	private Font getSelectedFont() {
		String fontName = (String) fontFamilyCombo.getSelectedItem();
		int fontSize = (int) fontSizeCombo.getValue();
		Font font = new Font(fontName, Font.PLAIN, fontSize);
		return font;
	}

	private void updatePreview() {
		Font font = getSelectedFont();
		previewTextField.setFont(font);
	}

	private void defineLayout() {
		Container container = getContentPane();
		SpringLayout layout = new SpringLayout();
		container.setLayout(layout);

		JLabel fontLabel = new JLabel(Translator.tr("Font family"));
		JLabel sizeLabel = new JLabel(Translator.tr("Font size"));
		JLabel previewLabel = new JLabel(Translator.tr("Preview"));

		container.add(fontLabel);
		container.add(fontFamilyCombo);
		container.add(sizeLabel);
		container.add(fontSizeCombo);
		container.add(previewLabel);
		container.add(previewTextField);

		previewTextField.setPreferredSize(new Dimension(300, 100));

		container.add(okButton);
		container.add(cancelButton);

		layout.putConstraint(SpringLayout.WEST, fontLabel, 5, SpringLayout.WEST,
				container);
		layout.putConstraint(SpringLayout.WEST, fontFamilyCombo, 5,
				SpringLayout.WEST, container);
		layout.putConstraint(SpringLayout.WEST, sizeLabel, 5, SpringLayout.WEST,
				container);
		layout.putConstraint(SpringLayout.WEST, fontSizeCombo, 5, SpringLayout.WEST,
				container);
		layout.putConstraint(SpringLayout.WEST, previewLabel, 5, SpringLayout.WEST,
				container);
		layout.putConstraint(SpringLayout.WEST, previewTextField, 5, SpringLayout.WEST,
				container);
		layout.putConstraint(SpringLayout.EAST, okButton, -5, SpringLayout.WEST,
				cancelButton);
		layout.putConstraint(SpringLayout.EAST, cancelButton, -5,
				SpringLayout.EAST, container);

		layout.putConstraint(SpringLayout.NORTH, fontLabel, 5, SpringLayout.NORTH,
				container);
		layout.putConstraint(SpringLayout.NORTH, fontFamilyCombo, 5,
				SpringLayout.SOUTH, fontLabel);
		layout.putConstraint(SpringLayout.NORTH, sizeLabel, 5, SpringLayout.SOUTH,
				fontFamilyCombo);
		layout.putConstraint(SpringLayout.NORTH, fontSizeCombo, 5, SpringLayout.SOUTH,
				sizeLabel);
		layout.putConstraint(SpringLayout.NORTH, previewLabel, 5, SOUTH, fontSizeCombo);
		layout.putConstraint(SpringLayout.NORTH, previewTextField, 5, SOUTH, previewLabel);
		layout.putConstraint(SpringLayout.NORTH, okButton, 15, SpringLayout.SOUTH,
				previewTextField);
		layout.putConstraint(SpringLayout.NORTH, cancelButton, 15,
				SpringLayout.SOUTH, previewTextField);

		Constraints containerCons = layout.getConstraints(container);
		Constraints fontCons = layout.getConstraints(previewTextField);
		Constraints okCons = layout.getConstraints(okButton);

		containerCons.setWidth(Spring.sum(Spring.constant(5),
				fontCons.getConstraint(EAST)));
		containerCons.setHeight(Spring.sum(Spring.constant(5),
				okCons.getConstraint(SOUTH)));

		getRootPane().setDefaultButton(okButton);

		pack();
		setLocationRelativeTo(getOwner());
	}
}
