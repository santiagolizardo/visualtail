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
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import com.santiagolizardo.beobachter.gui.MainWindow;
import com.santiagolizardo.beobachter.beans.LogType;
import com.santiagolizardo.beobachter.beans.SessionManager;
import com.santiagolizardo.beobachter.gui.actions.ExitAction;
import com.santiagolizardo.beobachter.gui.dialogs.LogWindow;
import com.santiagolizardo.beobachter.gui.dialogs.SessionsDialog;
import com.santiagolizardo.beobachter.gui.util.DialogFactory;
import com.santiagolizardo.beobachter.util.FileUtil;
import com.santiagolizardo.beobachter.resources.images.IconFactory;
import static com.santiagolizardo.beobachter.resources.languages.Translator.trn;
import java.io.IOException;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;

public class FileMenu extends JMenu implements ActionListener {

	private static final long serialVersionUID = -9095266179967845006L;

	private static final Logger logger = Logger.getLogger(FileMenu.class.getName());

	private MainWindow mainWindow;

	private RecentsMenu recentsMenu;

	private JMenuItem openMenuItem;
	private JMenuItem loadSessionMenuItem;
	private JMenuItem saveSessionMenuItem;

	public FileMenu(final MainWindow mainWindow) {
		setText(tr("File"));
		setMnemonic(KeyEvent.VK_F);

		this.mainWindow = mainWindow;

		openMenuItem = new JMenuItem(tr("Open..."));
		openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				KeyEvent.CTRL_MASK));
		openMenuItem.addActionListener(this);

		recentsMenu = new RecentsMenu(mainWindow);
		recentsMenu.setEnabled(!mainWindow.getConfigData().getRecentFiles().isEmpty());
		recentsMenu.refresh();

		JMenuItem exit = new JMenuItem(new ExitAction(mainWindow));
		exit.setIcon(IconFactory.getImage("exit.png"));

		loadSessionMenuItem = new JMenuItem(tr("Manage sessions..."));
		loadSessionMenuItem.addActionListener(this);

		saveSessionMenuItem = new JMenuItem(tr("Save current session"));
		saveSessionMenuItem.setIcon(IconFactory.getImage("disk.png"));
		saveSessionMenuItem.setEnabled(false);
		saveSessionMenuItem.addActionListener(this);

		add(openMenuItem);
		add(recentsMenu);
		addSeparator();
		add(loadSessionMenuItem);
		add(saveSessionMenuItem);
		addSeparator();
		add(exit);
	}

	public RecentsMenu getRecentsMenu() {
		return recentsMenu;
	}

	public JMenuItem getSaveSessionMenuItem() {
		return saveSessionMenuItem;
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if (openMenuItem == ev.getSource()) {
			File lastSelected = new File(mainWindow.getConfigData().getLastPath());

			JFileChooser chooser = new JFileChooser();
			chooser.setSelectedFile(lastSelected);
			chooser.setMultiSelectionEnabled(true);
			int resp = chooser.showOpenDialog(mainWindow);
			if (resp == JFileChooser.APPROVE_OPTION) {
				File[] files = chooser.getSelectedFiles();
				LogType logType = new LogType("Default");

				List<File> unreadableFiles = new ArrayList<>();

				for (File file : files) {

					try {
						FileUtil.tryReading(file);
					} catch (Exception ex) {
						unreadableFiles.add(file);
						continue;
					}

					mainWindow.getActionFactory().getOpenAction().openFile(file.getAbsolutePath(),
							logType);

					mainWindow.getRecentFiles().remove(file.getAbsolutePath());
					mainWindow.getRecentFiles().add(file.getAbsolutePath());
					recentsMenu.refresh();
					recentsMenu.setEnabled(true);
				}

				mainWindow.getDesktop().setWindowsOnTileHorizontal();

				int numberOfUnreadableFiles = unreadableFiles.size();
				if (numberOfUnreadableFiles > 0) {
					String text = trn("The file could not be opened for reading:", "These files could not be opened for reading:", numberOfUnreadableFiles);
					StringBuilder message = new StringBuilder();
					message.append(text.concat("\n"));
					for (File file : unreadableFiles) {
						message.append("    - ")
								.append(file.getAbsolutePath())
								.append("\n");
					}
					DialogFactory.showErrorMessage(mainWindow,
							message.toString());
				}
			}
		} else if (loadSessionMenuItem == ev.getSource()) {
			SessionsDialog dialog = new SessionsDialog(mainWindow, recentsMenu);
			dialog.setVisible(true);
		} else if (saveSessionMenuItem
				== ev.getSource()) {
			String name = JOptionPane.showInputDialog(getParent()
					.getParent(), tr("Please enter the session name:"),
					tr("Session name"), JOptionPane.QUESTION_MESSAGE);
			if (name == null) {
				return;
			}

			name = name.trim();

			if (name.length() == 0) {
				DialogFactory.showErrorMessage(null,
						tr("Invalid session name"));
				return;
			}

			List<String> filePaths = new ArrayList<>();
			JInternalFrame[] frames = mainWindow.getDesktop().getAllFrames();
			for (JInternalFrame frame : frames) {
				LogWindow logWindow = (LogWindow) frame;
				filePaths.add(logWindow.getFile()
						.getAbsolutePath());
			}

			SessionManager sessionManager = new SessionManager();
			try {
				sessionManager.save(name, filePaths);
			} catch (IOException e) {
				logger.warning(e.getMessage());
				DialogFactory.showErrorMessage(null, tr("Invalid session name") + "\r\n" + e.getMessage());
			}
		}
	}
}
