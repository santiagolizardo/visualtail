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
package com.santiagolizardo.beobachter.gui.menu;

import static com.santiagolizardo.beobachter.resources.languages.Translator.tr;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.santiagolizardo.beobachter.gui.MainWindow;
import com.santiagolizardo.beobachter.gui.dialogs.AboutDialog;
import com.santiagolizardo.beobachter.gui.util.UpdateManager;
import com.santiagolizardo.beobachter.resources.images.IconFactory;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

public class HelpMenu extends JMenu implements ActionListener {

	private static final long serialVersionUID = -3653494784002339461L;

	private static final Logger logger = Logger.getLogger(HelpMenu.class.getName());

	private MainWindow mainWindow;

	private JMenuItem reportBugFeatureRequestMenuItem;
	private JMenuItem checkForUpdatesMenuItem;
	private JMenuItem aboutThisAppMenuItem;

	public HelpMenu(MainWindow mainWindow) {

		this.mainWindow = mainWindow;

		setText(tr("Help"));
		setMnemonic(KeyEvent.VK_H);

		reportBugFeatureRequestMenuItem = new JMenuItem(tr("Report bug/Request feature"));
		reportBugFeatureRequestMenuItem.setIcon(IconFactory.getImage("lightbulb.png"));
		reportBugFeatureRequestMenuItem.addActionListener(this);

		checkForUpdatesMenuItem = new JMenuItem(tr("Check for updates"));
		checkForUpdatesMenuItem.setIcon(IconFactory.getImage("arrow_rotate_clockwise.png"));
		checkForUpdatesMenuItem.addActionListener(this);

		aboutThisAppMenuItem = new JMenuItem(tr("About this application..."));
		aboutThisAppMenuItem.setIcon(IconFactory.getImage("information.png"));
		aboutThisAppMenuItem.setMnemonic(KeyEvent.VK_F1);
		aboutThisAppMenuItem.addActionListener(this);

		add(reportBugFeatureRequestMenuItem);
		addSeparator();
		add(checkForUpdatesMenuItem);
		add(aboutThisAppMenuItem);
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if (reportBugFeatureRequestMenuItem == ev.getSource()) {
			try {
				URI uri = new URI("https://sourceforge.net/p/beobachter/issues/");
				Desktop.getDesktop().browse(uri);
			} catch (URISyntaxException | IOException ex) {
				logger.warning(ex.getMessage());
			}
		} else if (checkForUpdatesMenuItem == ev.getSource()) {
			UpdateManager updateManager = new UpdateManager(mainWindow);
			updateManager.checkForUpdate();
		} else if (aboutThisAppMenuItem == ev.getSource()) {
			AboutDialog aboutDialog = new AboutDialog(mainWindow);
			aboutDialog.setVisible(true);
		}
	}
}
