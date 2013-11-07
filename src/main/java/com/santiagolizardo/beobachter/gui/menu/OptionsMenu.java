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
package com.santiagolizardo.beobachter.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.santiagolizardo.beobachter.gui.dialogs.LogTypesDialog;
import com.santiagolizardo.beobachter.gui.dialogs.PreferencesDialog;
import com.santiagolizardo.beobachter.resources.images.IconFactory;
import com.santiagolizardo.beobachter.resources.languages.Translator;

public class OptionsMenu extends JMenu {

	private static final long serialVersionUID = 4390929385204480673L;

	public OptionsMenu(final JFrame parentFrame) {

		setText(Translator.t("Options"));
		setMnemonic(KeyEvent.VK_O);

		JMenuItem manageLogTypes = new JMenuItem(
				Translator.t("Manage_log_types..."));
		manageLogTypes.setIcon(IconFactory.getImage("manage_log_types.png"));
		manageLogTypes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				LogTypesDialog dialog = new LogTypesDialog(parentFrame);
				dialog.setVisible(true);
			}
		});

		JMenuItem preferences = new JMenuItem(Translator.t("Preferences..."));
		preferences.setIcon(IconFactory.getImage("preferences.png"));
		preferences.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
				KeyEvent.CTRL_MASK));
		preferences.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				PreferencesDialog dialog = new PreferencesDialog(parentFrame);
				dialog.setVisible(true);
			}
		});

		add(manageLogTypes);
		addSeparator();
		add(preferences);
	}
}
