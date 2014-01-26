/**
 * This file is part of Beobachter, a graphical log file monitor.
 *
 * Beobachter is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Beobachter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Beobachter.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.santiagolizardo.beobachter.gui.menu;

import static com.santiagolizardo.beobachter.resources.languages.Translator._;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.santiagolizardo.beobachter.gui.MainWindow;
import com.santiagolizardo.beobachter.gui.dialogs.AboutDialog;
import com.santiagolizardo.beobachter.gui.util.UpdateManager;
import com.santiagolizardo.beobachter.resources.images.IconFactory;

public class HelpMenu extends JMenu {

	private static final long serialVersionUID = -3653494784002339461L;

	public HelpMenu(final MainWindow mainGUI) {

		setText(_("Help"));
		setMnemonic(KeyEvent.VK_H);

		JMenuItem checkForUpdates = new JMenuItem(_("Check for updates"));
		checkForUpdates.setIcon(IconFactory.getImage("check_for_updates.png"));
		checkForUpdates.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				UpdateManager updateManager = new UpdateManager(mainGUI);
				updateManager.checkForUpdate();
			}
		});
		JMenuItem about = new JMenuItem(_("About this application..."));
		about.setIcon(IconFactory.getImage("help.png"));
		about.setMnemonic(KeyEvent.VK_F1);
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				AboutDialog aboutDialog = new AboutDialog(mainGUI);
				aboutDialog.setVisible(true);
			}
		});

		add(checkForUpdates);
		addSeparator();
		add(about);
	}
}
