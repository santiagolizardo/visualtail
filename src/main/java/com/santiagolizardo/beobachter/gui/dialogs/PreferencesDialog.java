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
package com.santiagolizardo.beobachter.gui.dialogs;

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
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.santiagolizardo.beobachter.gui.MainWindow;
import com.santiagolizardo.beobachter.beans.SwingLookAndFeel;
import com.santiagolizardo.beobachter.config.ConfigData;
import com.santiagolizardo.beobachter.config.ConfigPersistence;
import com.santiagolizardo.beobachter.gui.renderers.LocaleRender;
import com.santiagolizardo.beobachter.gui.renderers.SwingLAFRenderer;
import com.santiagolizardo.beobachter.gui.util.SwingUtil;
import com.santiagolizardo.beobachter.resources.languages.Translator;
import com.santiagolizardo.beobachter.util.LocaleUtil;

public class PreferencesDialog extends AbstractDialog {

	private static final long serialVersionUID = -4534774464099526206L;

	private static final Logger logger = Logger
			.getLogger(PreferencesDialog.class.getName());

	private JComboBox<SwingLookAndFeel> lookAndFeelComboBox;

	private JComboBox<String> languageComboBox;

	private JButton okButton;
	private JButton cancelButton;

	public PreferencesDialog(final MainWindow mainGUI) {
		super(mainGUI);

		final ConfigData configManager = mainGUI.getConfigData();

		setTitle(Translator._("Preferences"));
		setResizable(false);
		setModal(true);

		LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
		SwingLookAndFeel[] lafs = new SwingLookAndFeel[infos.length];
		for (int i = 0; i < infos.length; i++) {
			LookAndFeelInfo info = infos[i];
			lafs[i] = new SwingLookAndFeel(info.getName(), info.getClassName());
		}
		lookAndFeelComboBox = new JComboBox<>(lafs);
		lookAndFeelComboBox.setRenderer(new SwingLAFRenderer());
		try {
			SwingLookAndFeel look = SwingLookAndFeel.forName(configManager
					.getWindowLAF());
			lookAndFeelComboBox.setSelectedItem(look);
		} catch (Exception e) {
			logger.warning("Unable to set the selected look&feel.");
		}

		String[] languages = LocaleUtil.getAvailableLocales();

		languageComboBox = new JComboBox<>(languages);
		languageComboBox.setSelectedItem(configManager.getLanguage());
		languageComboBox.setRenderer(new LocaleRender());

		okButton = new JButton(Translator._("Save"));
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				setVisible(false);

				SwingLookAndFeel laf = ((SwingLookAndFeel) lookAndFeelComboBox
						.getSelectedItem());
				configManager.setWindowLAF(laf.getClassName());
				SwingUtil.setLookAndFeel(laf.getClassName());
				SwingUtilities.updateComponentTreeUI(mainGUI);

				Object selectedLanguage = languageComboBox.getSelectedItem();
				if (null != selectedLanguage) {
					configManager.setLanguage(selectedLanguage.toString());
				}

				ConfigPersistence configPersistence = new ConfigPersistence();
				configPersistence.saveProperties(mainGUI,
						configManager.getConfiguration());

				dispose();
			}
		});

		cancelButton = new JButton(Translator._("Cancel"));
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				setVisible(false);
				dispose();
			}
		});

		defineLayout();
	}

	private void defineLayout() {
		Container container = getContentPane();
		SpringLayout layout = new SpringLayout();
		container.setLayout(layout);

		JLabel _lookAndFeel = new JLabel(Translator._("Look and feel"));
		JLabel _language = new JLabel(Translator._("Language"));

		container.add(_lookAndFeel);
		container.add(lookAndFeelComboBox);

		if (languageComboBox.getModel().getSize() > 0) {
			container.add(_language);
			container.add(languageComboBox);
		}

		container.add(okButton);
		container.add(cancelButton);

		layout.putConstraint(SpringLayout.WEST, _lookAndFeel, 5,
				SpringLayout.WEST, container);
		layout.putConstraint(SpringLayout.WEST, lookAndFeelComboBox, 5,
				SpringLayout.WEST, container);
		layout.putConstraint(SpringLayout.WEST, _language, 5,
				SpringLayout.WEST, container);
		layout.putConstraint(SpringLayout.WEST, languageComboBox, 5,
				SpringLayout.WEST, container);
		layout.putConstraint(SpringLayout.EAST, okButton, -5, SpringLayout.WEST,
				cancelButton);
		layout.putConstraint(SpringLayout.EAST, cancelButton, -5,
				SpringLayout.EAST, container);

		layout.putConstraint(SpringLayout.NORTH, _lookAndFeel, 5,
				SpringLayout.NORTH, container);
		layout.putConstraint(SpringLayout.NORTH, lookAndFeelComboBox, 5,
				SpringLayout.SOUTH, _lookAndFeel);
		layout.putConstraint(SpringLayout.NORTH, _language, 5,
				SpringLayout.SOUTH, lookAndFeelComboBox);
		layout.putConstraint(SpringLayout.NORTH, languageComboBox, 5,
				SpringLayout.SOUTH, _language);
		layout.putConstraint(SpringLayout.NORTH, okButton, 15, SpringLayout.SOUTH,
				languageComboBox);
		layout.putConstraint(SpringLayout.NORTH, cancelButton, 15,
				SpringLayout.SOUTH, languageComboBox);

		Constraints containerCons = layout.getConstraints(container);
		Constraints okCons = layout.getConstraints(okButton);

		containerCons.setWidth(Spring.constant(270));
		containerCons.setHeight(Spring.sum(Spring.constant(5),
				okCons.getConstraint(SOUTH)));

		getRootPane().setDefaultButton(okButton);

		pack();
		setLocationRelativeTo(getOwner());
	}
}
