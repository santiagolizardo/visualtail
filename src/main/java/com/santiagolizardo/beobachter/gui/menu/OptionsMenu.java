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

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.santiagolizardo.beobachter.gui.MainWindow;
import com.santiagolizardo.beobachter.gui.dialogs.LogTypesDialog;
import com.santiagolizardo.beobachter.gui.dialogs.PreferencesDialog;
import com.santiagolizardo.beobachter.resources.images.IconFactory;

public class OptionsMenu extends JMenu implements ActionListener {

	private static final long serialVersionUID = 4390929385204480673L;

	private MainWindow mainGUI;

	private JMenuItem alwaysOnTop;
	private JMenuItem manageLogTypes;
	private JMenuItem preferences;

	public OptionsMenu(MainWindow mainGUI) {

		setText(_("Options"));
		setMnemonic(KeyEvent.VK_O);

		this.mainGUI = mainGUI;

		alwaysOnTop = new JCheckBoxMenuItem(_("Always on top"));
		alwaysOnTop.addActionListener(this);

		manageLogTypes = new JMenuItem(_("Manage log types..."));
		manageLogTypes.setIcon(IconFactory.getImage("manage_log_types.png"));
		manageLogTypes.addActionListener(this);

		preferences = new JMenuItem(_("Preferences..."));
		preferences.setIcon(IconFactory.getImage("preferences.png"));
		preferences.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
				KeyEvent.CTRL_MASK));
		preferences.addActionListener(this);

		add(alwaysOnTop);
		addSeparator();
		add(manageLogTypes);
		addSeparator();
		add(preferences);
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if (alwaysOnTop == ev.getSource()) {
			mainGUI.setAlwaysOnTop(alwaysOnTop.isSelected());
		} else if (manageLogTypes == ev.getSource()) {
			LogTypesDialog dialog = new LogTypesDialog(mainGUI);
			dialog.setVisible(true);
		} else if (preferences == ev.getSource()) {
			PreferencesDialog dialog = new PreferencesDialog(mainGUI);
			dialog.setVisible(true);
		}
	}
}
