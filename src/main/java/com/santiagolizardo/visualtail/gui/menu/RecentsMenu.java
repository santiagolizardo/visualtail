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
import java.io.File;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.santiagolizardo.visualtail.gui.MainWindow;
import com.santiagolizardo.visualtail.beans.LogType;
import com.santiagolizardo.visualtail.gui.util.DialogFactory;
import com.santiagolizardo.visualtail.gui.util.EmptyIcon;
import com.santiagolizardo.visualtail.resources.images.IconFactory;
import com.santiagolizardo.visualtail.util.FileUtil;
import java.util.List;
import java.util.ListIterator;

public class RecentsMenu extends JMenu implements ActionListener {

	private static final long serialVersionUID = 1L;

	private final MainWindow mainWindow;

	private final JMenuItem cleanRecentsMenuItem;

	public RecentsMenu(MainWindow mainWindow) {
		super(tr("Open recents"));

		this.mainWindow = mainWindow;

		setIcon(EmptyIcon.SIZE_16);

		cleanRecentsMenuItem = new JMenuItem(tr("Clean recents"));
		cleanRecentsMenuItem.setIcon(IconFactory.getImage("bin_empty.png"));
		cleanRecentsMenuItem.addActionListener(this);
	}

	public void addRecent(String fileName) {
		List<String> recentFiles = mainWindow.getConfigData().getRecentFiles();
		if (!recentFiles.contains(fileName)) {
			JMenuItem item = new JMenuItem(fileName);
			item.addActionListener((ActionEvent ev) -> {
				JMenuItem item1 = (JMenuItem) ev.getSource();
				String filePath = item1.getText();
				File file = new File(filePath);
				try {
					FileUtil.tryReading(file);
				} catch (Exception e) {
					DialogFactory.showErrorMessage(mainWindow, e.getMessage());
					return;
				}
				mainWindow.getActionFactory().getOpenAction().openFile(filePath, new LogType(
						"Default"));
			});
			add(item);
		}
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if (cleanRecentsMenuItem == ev.getSource()) {
			mainWindow.getConfigData().getRecentFiles().clear();
			refresh();

			setEnabled(false);
		}
	}

	public void refresh() {
		removeAll();

		List<String> recentFileNames = mainWindow.getConfigData().getRecentFiles();
		ListIterator<String> it = recentFileNames.listIterator(recentFileNames.size());
		while (it.hasPrevious()) {
			String recentFilePath = it.previous();
			JMenuItem item = new JMenuItem(recentFilePath);
			item.addActionListener((ActionEvent ev) -> {
				JMenuItem item1 = (JMenuItem) ev.getSource();
				String filePath = item1.getText();
				File file = new File(filePath);
				try {
					FileUtil.tryReading(file);
				} catch (Exception e) {
					DialogFactory.showErrorMessage(mainWindow, e.getMessage());
					return;
				}
				mainWindow.getActionFactory().getOpenAction().openFile(filePath, new LogType(
						"Default"));
			});
			add(item);
		}

		addSeparator();
		add(cleanRecentsMenuItem);
	}
}
