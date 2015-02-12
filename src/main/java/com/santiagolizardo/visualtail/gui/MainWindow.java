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
package com.santiagolizardo.visualtail.gui;

import com.santiagolizardo.visualtail.Constants;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import com.santiagolizardo.visualtail.config.ConfigData;
import com.santiagolizardo.visualtail.config.ConfigPersistence;
import com.santiagolizardo.visualtail.gui.actions.ActionFactory;
import com.santiagolizardo.visualtail.gui.components.DesktopPanel;
import com.santiagolizardo.visualtail.gui.menu.Menu;
import com.santiagolizardo.visualtail.resources.images.IconFactory;
import java.util.List;

/**
 * This is the main application entry point. It constructs the initial window.
 */
public class MainWindow extends JFrame {

	private static final long serialVersionUID = -349295815866572937L;

	private ConfigData configData;
	private DesktopPanel desktop;

	private ActionFactory actionFactory;

	private Menu menu;

	private List<String> recentFiles;

	public MainWindow(ConfigData configData) {
		this.configData = configData;

		recentFiles = configData.getRecentFiles();

		actionFactory = new ActionFactory(this);

		desktop = new DesktopPanel();

		menu = new Menu(desktop, this);
		setJMenuBar(menu);

		setTitle(Constants.APP_NAME);

		setSize(configData.getWindowWidth(), configData.getWindowHeight());
		setLocation(configData.getWindowX(), configData.getWindowY());
		setIconImage(IconFactory.getImage("icon.png").getImage());

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				quit();
			}
		});

		JScrollPane scroll = new JScrollPane(desktop);
		getContentPane().add(scroll);
	}

	public void setTitle(String title, String fileName) {
		setTitle(title + " - " + fileName);
	}

	/**
	 * This method terminates the application and saves all the runtime
	 * information on the filesystem.
	 */
	public void quit() {

		int exitCode = 1;

		setVisible(false);

		configData.setWindowHeight(getHeight());
		configData.setWindowWidth(getWidth());
		configData.setWindowX(getX());
		configData.setWindowY(getY());

		ConfigPersistence configPersistence = new ConfigPersistence();
		configPersistence.saveProperties(this,
				configData.getConfiguration());

		dispose();
		exitCode = 0;

		System.exit(exitCode);
	}

	public void updateActions(int delta) {
		boolean areWindowsOpen = desktop.getAllFrames().length + delta > 0;
		actionFactory.getSelectAllAction().setEnabled(areWindowsOpen);

		Menu mainMenu = ((Menu) getJMenuBar());
		mainMenu.getFileMenu().getSaveSessionMenuItem()
				.setEnabled(areWindowsOpen);
		mainMenu.getEditMenu().setEnabled(areWindowsOpen);
		mainMenu.getWindowMenu().setEnabled(areWindowsOpen);
	}

	public List<String> getRecentFiles() {
		return recentFiles;
	}

	public Menu getMenu() {
		return menu;
	}

	public ConfigData getConfigData() {
		return configData;
	}

	public ActionFactory getActionFactory() {
		return actionFactory;
	}

	public DesktopPanel getDesktop() {
		return desktop;
	}
}