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
package com.santiagolizardo.visualtail.gui.menu;

import static com.santiagolizardo.visualtail.resources.languages.Translator.tr;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.santiagolizardo.visualtail.gui.MainWindow;
import com.santiagolizardo.visualtail.gui.dialogs.FontChooserDialog;
import com.santiagolizardo.visualtail.gui.dialogs.LogTypesDialog;
import com.santiagolizardo.visualtail.gui.dialogs.PreferencesDialog;
import com.santiagolizardo.visualtail.resources.images.IconFactory;
import java.awt.Toolkit;

public class OptionsMenu extends JMenu implements ActionListener {

	private static final long serialVersionUID = 4390929385204480673L;

	private final MainWindow mainWindow;

	private final JMenuItem alwaysOnTopMenuItem;
	private final JMenuItem manageLogTypesMenuItem;
	private final JMenuItem fontSelectorMenuItem;
	private final JMenuItem preferencesMenuItem;

	public OptionsMenu(MainWindow mainWindow) {

		setText(tr("Options"));
		setMnemonic(KeyEvent.VK_O);

		this.mainWindow = mainWindow;

		alwaysOnTopMenuItem = new JCheckBoxMenuItem(tr("Always on top"));
		alwaysOnTopMenuItem.addActionListener(this);

		manageLogTypesMenuItem = new JMenuItem(tr("Manage log types..."));
		manageLogTypesMenuItem.setIcon(IconFactory.getImage("manage_log_types.png"));
		manageLogTypesMenuItem.addActionListener(this);

		fontSelectorMenuItem = new JMenuItem(tr("Font settings..."));
		fontSelectorMenuItem.addActionListener(this);

		preferencesMenuItem = new JMenuItem(tr("Preferences..."));
		preferencesMenuItem.setIcon(IconFactory.getImage("preferences.png"));
		preferencesMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
		preferencesMenuItem.addActionListener(this);

		add(alwaysOnTopMenuItem);
		addSeparator();
		add(manageLogTypesMenuItem);
		addSeparator();
		add(fontSelectorMenuItem);
		add(preferencesMenuItem);
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		Object source = ev.getSource();
		if (alwaysOnTopMenuItem == source) {
			mainWindow.setAlwaysOnTop(alwaysOnTopMenuItem.isSelected());
		} else if (manageLogTypesMenuItem == source) {
			LogTypesDialog dialog = new LogTypesDialog(mainWindow);
			dialog.setVisible(true);
		} else if (fontSelectorMenuItem == source) {
			FontChooserDialog dialog = new FontChooserDialog(mainWindow);
			dialog.setVisible(true);			
		} else if (preferencesMenuItem == source) {
			PreferencesDialog dialog = new PreferencesDialog(mainWindow);
			dialog.setVisible(true);
		}
	}
}
