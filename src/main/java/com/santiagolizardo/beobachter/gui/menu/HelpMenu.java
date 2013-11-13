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
package com.santiagolizardo.beobachter.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.santiagolizardo.beobachter.gui.dialogs.AboutDialog;
import com.santiagolizardo.beobachter.gui.util.UpdateManager;
import com.santiagolizardo.beobachter.resources.images.IconFactory;
import com.santiagolizardo.beobachter.resources.languages.Translator;

public class HelpMenu extends JMenu {

	private static final long serialVersionUID = -3653494784002339461L;

	public HelpMenu(final JFrame parentFrame) {

		setText(Translator._("Help"));
		setMnemonic(KeyEvent.VK_H);

		JMenuItem checkForUpdates = new JMenuItem(
				Translator._("Check_for_updates"));
		checkForUpdates.setIcon(IconFactory.getImage("check_for_updates.png"));
		checkForUpdates.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				UpdateManager.checkForUpdate();
			}
		});
		JMenuItem about = new JMenuItem(
				Translator._("About_this_application..."));
		about.setIcon(IconFactory.getImage("help.png"));
		about.setMnemonic(KeyEvent.VK_F1);
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				AboutDialog aboutDialog = new AboutDialog(parentFrame);
				aboutDialog.setVisible(true);
			}
		});

		add(checkForUpdates);
		addSeparator();
		add(about);
	}
}