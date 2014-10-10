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
import java.io.File;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.santiagolizardo.beobachter.gui.MainWindow;
import com.santiagolizardo.beobachter.beans.LogType;
import com.santiagolizardo.beobachter.gui.util.DialogFactory;
import com.santiagolizardo.beobachter.gui.util.EmptyIcon;
import com.santiagolizardo.beobachter.resources.images.IconFactory;
import com.santiagolizardo.beobachter.util.FileUtil;
import java.util.List;
import java.util.ListIterator;

public class RecentsMenu extends JMenu implements ActionListener {

	private static final long serialVersionUID = 1L;

	private MainWindow mainWindow;

	private JMenuItem cleanRecentsMenuItem;

	public RecentsMenu(MainWindow mainWindow) {
		super(tr("Open recents"));

		this.mainWindow = mainWindow;

		setIcon(EmptyIcon.SIZE_16);

		cleanRecentsMenuItem = new JMenuItem(tr("Clean recents"));
		cleanRecentsMenuItem.setIcon(IconFactory.getImage("bin_empty.png"));
		cleanRecentsMenuItem.addActionListener(this);
	}

	public void addRecent(String fileName) {
		List<String> recentFiles = mainWindow.getRecentFiles();
		if (!recentFiles.contains(fileName)) {
			JMenuItem item = new JMenuItem(fileName);
			item.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ev) {
					JMenuItem item = (JMenuItem) ev.getSource();
					String filePath = item.getText();
					File file = new File(filePath);

					try {
						FileUtil.tryReading(file);
					} catch (Exception e) {
						DialogFactory.showErrorMessage(mainWindow, e.getMessage());
						return;
					}

					mainWindow.getActionFactory().getOpenAction().openFile(filePath, new LogType(
							"Default"));
				}
			});
			add(item);
		}
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if (cleanRecentsMenuItem == ev.getSource()) {
			mainWindow.getRecentFiles().clear();
			refresh();

			setEnabled(false);
		}
	}

	public void refresh() {
		removeAll();

		List<String> recentFileNames = mainWindow.getRecentFiles();
		ListIterator<String> it = recentFileNames.listIterator(recentFileNames.size());
		while (it.hasPrevious()) {
			String recentFilePath = it.previous();
			JMenuItem item = new JMenuItem(recentFilePath);
			item.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ev) {
					JMenuItem item = (JMenuItem) ev.getSource();
					String filePath = item.getText();
					File file = new File(filePath);

					try {
						FileUtil.tryReading(file);
					} catch (Exception e) {
						DialogFactory.showErrorMessage(mainWindow, e.getMessage());
						return;
					}

					mainWindow.getActionFactory().getOpenAction().openFile(filePath, new LogType(
							"Default"));
				}
			});
			add(item);
		}

		addSeparator();
		add(cleanRecentsMenuItem);
	}
}
