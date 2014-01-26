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
import java.io.File;
import java.util.Vector;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.santiagolizardo.beobachter.gui.MainWindow;
import com.santiagolizardo.beobachter.beans.LogType;
import com.santiagolizardo.beobachter.config.ConfigData;
import com.santiagolizardo.beobachter.engine.Controller;
import com.santiagolizardo.beobachter.gui.util.DialogFactory;
import com.santiagolizardo.beobachter.gui.util.EmptyIcon;
import com.santiagolizardo.beobachter.gui.util.FileUtil;

public class RecentsMenu extends JMenu implements ActionListener {

	private static final long serialVersionUID = 1L;

	private MainWindow mainGUI;

	private JMenuItem cleanRecentsMenuItem;

	public RecentsMenu(MainWindow mainGUI) {
		super(_("Open recents"));

		this.mainGUI = mainGUI;

		setIcon(EmptyIcon.SIZE_16);

		cleanRecentsMenuItem = new JMenuItem(_("Clean recents"));
		cleanRecentsMenuItem.addActionListener(this);

		add(cleanRecentsMenuItem);
		addSeparator();

		ConfigData configData = mainGUI.getConfigData();

		for (String fileName : configData.getRecentFiles()) {
			addRecent(fileName);
		}
	}

	public void addRecent(String fileName) {
		Vector<String> recentFiles = mainGUI.getRecentFiles();
		if (!recentFiles.contains(fileName)) {
			JMenuItem item = new JMenuItem(fileName);
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					JMenuItem item = (JMenuItem) event.getSource();
					String filePath = item.getText();
					File file = new File(filePath);

					try {
						FileUtil.tryReading(file);
					} catch (Exception e) {
						DialogFactory.showErrorMessage(mainGUI, e.getMessage());
						return;
					}

					Controller.openFile(mainGUI, filePath, new LogType(
							"Default"));
				}
			});
			recentFiles.add(fileName);
			add(item);
		}
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if (cleanRecentsMenuItem == ev.getSource()) {
			mainGUI.getRecentFiles().clear();
			int count = getMenuComponentCount();
			for (int i = count - 1; i > 1; i--) {
				remove(i);
			}
			
			setEnabled(false);
		}
	}
}
