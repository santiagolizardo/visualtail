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
package com.santiagolizardo.beobachter.gui;

import com.santiagolizardo.beobachter.Constants;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.apache.commons.configuration.ConfigurationException;

import com.santiagolizardo.beobachter.config.ConfigData;
import com.santiagolizardo.beobachter.config.ConfigPersistence;
import com.santiagolizardo.beobachter.gui.actions.ActionFactory;
import com.santiagolizardo.beobachter.gui.components.DesktopPanel;
import com.santiagolizardo.beobachter.gui.dialogs.components.FindPanel;
import com.santiagolizardo.beobachter.gui.menu.Menu;
import com.santiagolizardo.beobachter.resources.images.IconFactory;

/**
 * This is the main application entry point. It constructs the initial window.
 * 
 * @author slizardo
 * 
 */
public class MainWindow extends JFrame {

	private static final long serialVersionUID = -349295815866572937L;

	private ConfigData configData;
	public DesktopPanel desktop;
	private FindPanel findPanel;

	private ActionFactory actionFactory;

	private Logger logger;

	private Menu menu;

	/**
	 * @todo Move this to a better place.
	 */
	private Vector<String> recentFiles;

	public MainWindow(ConfigData configData) {
		this.configData = configData;

		logger = Logger.getLogger(MainWindow.class.getName());

		recentFiles = new Vector<>();

		actionFactory = new ActionFactory(this);

		desktop = new DesktopPanel();

		menu = new Menu(desktop, this);
		setJMenuBar(menu);

		setTitle(Constants.APP_NAME);

		setSize(configData.getWindowWidth(), configData.getWindowHeight());
		setLocation(configData.getWindowX(), configData.getWindowY());
		setIconImage(IconFactory.getImage("icon.png").getImage());

		addWindowListener(new WindowAdapter() {
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

	public void addFindPanel() {
		if (findPanel == null) {
			findPanel = new FindPanel(this);

			getContentPane().add(findPanel, BorderLayout.PAGE_END);
			getContentPane().validate();

			findPanel.focus();
		}
	}

	public void removeFindPanel() {
		getContentPane().remove(findPanel);
		getContentPane().validate();

		findPanel = null;
	}

	/**
	 * This method terminates the application and saves all the runtime
	 * information on the filesystem.
	 */
	public void quit() {

		int exitCode = 1;

		try {
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

		} catch (ConfigurationException e) {
			logger.severe(e.getMessage());
		}

		System.exit(exitCode);
	}

	public void updateActions(int delta) {
		boolean areWindowsOpen = desktop.getAllFrames().length + delta > 0;
		actionFactory.createSelectAllAction().setEnabled(areWindowsOpen);

		Menu mainMenu = ((Menu) getJMenuBar());
		mainMenu.getWindowMenu().setEnabled(areWindowsOpen);
		mainMenu.getFileMenu().getSaveSessionMenuItem()
				.setEnabled(areWindowsOpen);
	}

	public Vector<String> getRecentFiles() {
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
}
