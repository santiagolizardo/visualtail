/**
 * Beobachter, the universal logs watcher
 * Copyright (C) 2009  Santiago Lizardo

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.slizardo.beobachter.gui.dialogs;

import static javax.swing.SpringLayout.EAST;
import static javax.swing.SpringLayout.SOUTH;

import java.awt.Container;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.JSpinner;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.SpringLayout.Constraints;
import javax.swing.UIManager.LookAndFeelInfo;

import org.slizardo.beobachter.MainGUI;
import org.slizardo.beobachter.beans.SwingLookAndFeel;
import org.slizardo.beobachter.config.ConfigManager;
import org.slizardo.beobachter.gui.renderers.LocaleRender;
import org.slizardo.beobachter.gui.renderers.SwingLAFRenderer;
import org.slizardo.beobachter.gui.util.SwingUtil;
import org.slizardo.beobachter.resources.languages.Translator;
import org.slizardo.beobachter.util.ArraysUtil;

public class PreferencesDialog extends JDialog {

	private static final long serialVersionUID = -4534774464099526206L;
	
	private static final Logger logger = Logger.getLogger(PreferencesDialog.class.getName());

	private JComboBox lookAndFeel;

	private JComboBox languagesList;
	private JComboBox fontsList;

	private JSpinner size;

	private JButton btnOk;
	private JButton btnCancel;

	public PreferencesDialog() {
		final ConfigManager configManager = MainGUI.instance.configManager;

		setTitle(Translator.t("Preferences"));
		setResizable(false);
		setModal(true);

		LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
		SwingLookAndFeel[] lafs = new SwingLookAndFeel[infos.length];
		for (int i = 0; i < infos.length; i++) {
			LookAndFeelInfo info = infos[i];
			lafs[i] = new SwingLookAndFeel(info.getName(), info.getClassName());
		}
		lookAndFeel = new JComboBox(lafs);
		lookAndFeel.setRenderer(new SwingLAFRenderer());
		try {
			SwingLookAndFeel look = SwingLookAndFeel.forName(configManager.getWindowLAF());
			lookAndFeel.setSelectedItem(look);
		} catch (Exception e) {
			logger.warning("Unable to set the selected look&feel.");
		}

		String[] languages = ArraysUtil.arrayLanguages();

		languagesList = new JComboBox(languages);
		languagesList.setSelectedItem(configManager.getLanguage());
		languagesList.setRenderer(new LocaleRender());

		String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getAvailableFontFamilyNames();
		fontsList = new JComboBox(fonts);
		fontsList.setSelectedItem(configManager.getFontFamily());

		size = new JSpinner(new SpinnerNumberModel(configManager.getFontSize(),
				8, 18, 1));

		btnOk = new JButton(Translator.t("Ok"));
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				setVisible(false);
				SwingLookAndFeel laf = ((SwingLookAndFeel) lookAndFeel.getSelectedItem());
				configManager.setWindowLAF(laf.getClassName());
				SwingUtil.setLookAndFeel(laf.getClassName());
				SwingUtilities.updateComponentTreeUI(MainGUI.instance);
				configManager.setLanguage(languagesList.getSelectedItem()
						.toString());
				configManager.setFontFamily(fontsList.getSelectedItem()
						.toString());
				configManager.setFontSize(Integer.parseInt(size.getValue()
						.toString()));
				try {
					configManager.saveConfiguration();
				} catch (Exception e) {
					e.printStackTrace();
				}
				dispose();
			}
		});

		btnCancel = new JButton(Translator.t("Cancel"));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				setVisible(false);
				dispose();
			}
		});

		defineLayout();
		setLocationRelativeTo(null);
	}

	@Override
	protected JRootPane createRootPane() {
		JRootPane rootPane = super.createRootPane();

		rootPane.registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				dispose();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

		rootPane.setDefaultButton(btnOk);

		return rootPane;
	}

	private void defineLayout() {
		Container container = getContentPane();
		SpringLayout layout = new SpringLayout();
		container.setLayout(layout);

		JLabel _lookAndFeel = new JLabel(Translator.t("Look_and_feel"));
		JLabel _language = new JLabel(Translator.t("Language"));
		JLabel _font = new JLabel(Translator.t("Font_family"));
		JLabel _size = new JLabel(Translator.t("Font_size"));

		container.add(_lookAndFeel);
		container.add(lookAndFeel);
		container.add(_language);
		container.add(languagesList);
		container.add(_font);
		container.add(fontsList);
		container.add(_size);
		container.add(size);

		container.add(btnOk);
		container.add(btnCancel);

		layout.putConstraint(SpringLayout.WEST, _lookAndFeel, 5,
				SpringLayout.WEST, container);
		layout.putConstraint(SpringLayout.WEST, lookAndFeel, 5,
				SpringLayout.WEST, container);
		layout.putConstraint(SpringLayout.WEST, _language, 5,
				SpringLayout.WEST, container);
		layout.putConstraint(SpringLayout.WEST, languagesList, 5,
				SpringLayout.WEST, container);
		layout.putConstraint(SpringLayout.WEST, _font, 5, SpringLayout.WEST,
				container);
		layout.putConstraint(SpringLayout.WEST, fontsList, 5,
				SpringLayout.WEST, container);
		layout.putConstraint(SpringLayout.WEST, _size, 5, SpringLayout.WEST,
				container);
		layout.putConstraint(SpringLayout.WEST, size, 5, SpringLayout.WEST,
				container);
		layout.putConstraint(SpringLayout.WEST, btnOk, 5, SpringLayout.WEST,
				container);
		layout.putConstraint(SpringLayout.WEST, btnCancel, 5,
				SpringLayout.EAST, btnOk);

		layout.putConstraint(SpringLayout.NORTH, _lookAndFeel, 5,
				SpringLayout.NORTH, container);
		layout.putConstraint(SpringLayout.NORTH, lookAndFeel, 5,
				SpringLayout.SOUTH, _lookAndFeel);
		layout.putConstraint(SpringLayout.NORTH, _language, 5,
				SpringLayout.SOUTH, lookAndFeel);
		layout.putConstraint(SpringLayout.NORTH, languagesList, 5,
				SpringLayout.SOUTH, _language);
		layout.putConstraint(SpringLayout.NORTH, _font, 5, SpringLayout.SOUTH,
				languagesList);
		layout.putConstraint(SpringLayout.NORTH, fontsList, 5,
				SpringLayout.SOUTH, _font);
		layout.putConstraint(SpringLayout.NORTH, _size, 5, SpringLayout.SOUTH,
				fontsList);
		layout.putConstraint(SpringLayout.NORTH, size, 5, SpringLayout.SOUTH,
				_size);
		layout.putConstraint(SpringLayout.NORTH, btnOk, 15, SpringLayout.SOUTH,
				size);
		layout.putConstraint(SpringLayout.NORTH, btnCancel, 15,
				SpringLayout.SOUTH, size);

		Constraints containerCons = layout.getConstraints(container);
		Constraints fontCons = layout.getConstraints(fontsList);
		Constraints okCons = layout.getConstraints(btnOk);

		containerCons.setWidth(Spring.sum(Spring.constant(5), fontCons
				.getConstraint(EAST)));
		containerCons.setHeight(Spring.sum(Spring.constant(5), okCons
				.getConstraint(SOUTH)));

		pack();
	}
}
