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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.SpringLayout.Constraints;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.santiagolizardo.beobachter.MainGUI;
import com.santiagolizardo.beobachter.beans.SwingLookAndFeel;
import com.santiagolizardo.beobachter.config.ConfigManager;
import com.santiagolizardo.beobachter.gui.renderers.LocaleRender;
import com.santiagolizardo.beobachter.gui.renderers.SwingLAFRenderer;
import com.santiagolizardo.beobachter.gui.util.SwingUtil;
import com.santiagolizardo.beobachter.resources.languages.Translator;
import com.santiagolizardo.beobachter.util.ArraysUtil;

public class PreferencesDialog extends AbstractDialog {

	private static final long serialVersionUID = -4534774464099526206L;

	private static final Logger logger = Logger
			.getLogger(PreferencesDialog.class.getName());

	private JComboBox<SwingLookAndFeel> lookAndFeel;

	private JComboBox<String> languagesList;
	private JComboBox<String> fontsList;

	private JSpinner size;

	private JButton btnOk;
	private JButton btnCancel;

	public PreferencesDialog(JFrame parentFrame) {
		super(parentFrame);

		final ConfigManager configManager = MainGUI.instance.configManager;

		setTitle(Translator._("Preferences"));
		setResizable(false);
		setModal(true);

		LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
		SwingLookAndFeel[] lafs = new SwingLookAndFeel[infos.length];
		for (int i = 0; i < infos.length; i++) {
			LookAndFeelInfo info = infos[i];
			lafs[i] = new SwingLookAndFeel(info.getName(), info.getClassName());
		}
		lookAndFeel = new JComboBox<SwingLookAndFeel>(lafs);
		lookAndFeel.setRenderer(new SwingLAFRenderer());
		try {
			SwingLookAndFeel look = SwingLookAndFeel.forName(configManager
					.getWindowLAF());
			lookAndFeel.setSelectedItem(look);
		} catch (Exception e) {
			logger.warning("Unable to set the selected look&feel.");
		}

		String[] languages = ArraysUtil.arrayLanguages();

		languagesList = new JComboBox<String>(languages);
		languagesList.setSelectedItem(configManager.getLanguage());
		languagesList.setRenderer(new LocaleRender());

		String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getAvailableFontFamilyNames();
		fontsList = new JComboBox<String>(fonts);
		fontsList.setSelectedItem(configManager.getFontFamily());

		size = new JSpinner(new SpinnerNumberModel(configManager.getFontSize(),
				8, 18, 1));

		btnOk = new JButton(Translator._("Ok"));
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				setVisible(false);
				SwingLookAndFeel laf = ((SwingLookAndFeel) lookAndFeel
						.getSelectedItem());
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

		btnCancel = new JButton(Translator._("Cancel"));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				setVisible(false);
				dispose();
			}
		});

		defineLayout();
	}

	@Override
	protected JRootPane createRootPane() {
		JRootPane rootPane = super.createRootPane();
		rootPane.setDefaultButton(btnOk);
		return rootPane;
	}

	private void defineLayout() {
		Container container = getContentPane();
		SpringLayout layout = new SpringLayout();
		container.setLayout(layout);

		JLabel _lookAndFeel = new JLabel(Translator._("Look_and_feel"));
		JLabel _language = new JLabel(Translator._("Language"));
		JLabel _font = new JLabel(Translator._("Font_family"));
		JLabel _size = new JLabel(Translator._("Font_size"));

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

		containerCons.setWidth(Spring.sum(Spring.constant(5),
				fontCons.getConstraint(EAST)));
		containerCons.setHeight(Spring.sum(Spring.constant(5),
				okCons.getConstraint(SOUTH)));

		pack();
		setLocationRelativeTo(parentFrame);
	}
}
