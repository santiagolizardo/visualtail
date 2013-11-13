/**
 * Beobachter is a logs watcher for the desktop. (a.k.a. full-featured tail)
 * Copyright (C) 2013 Santiago Lizardo (http://www.santiagolizardo.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.santiagolizardo.beobachter;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.apache.commons.configuration.ConfigurationException;

import com.santiagolizardo.beobachter.config.ConfigManager;
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
public class MainGUI extends JFrame {

	private static final long serialVersionUID = -349295815866572937L;

	public static MainGUI instance = null;

	public ConfigManager configManager;
	public DesktopPanel desktop;
	private FindPanel findPanel;

	private Logger logger;

	public MainGUI(ConfigManager configManager) {
		this.configManager = configManager;

		logger = Logger.getLogger(MainGUI.class.getName());

		desktop = new DesktopPanel();
		Menu menu = new Menu(desktop, this);
		setJMenuBar(menu);
		setTitle(Constants.APP_NAME);
		setSize(configManager.getWindowWidth(), configManager.getWindowHeight());
		setLocation(configManager.getWindowX(), configManager.getWindowY());
		setIconImage(IconFactory.getImage("logo.png").getImage());
		desktop.setDragMode(configManager.getDragMode());

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				quit();
			}
		});

		JScrollPane scroll = new JScrollPane(desktop);
		getContentPane().add(scroll);
	}

	public void addFindPanel() {
		if (findPanel == null) {
			findPanel = new FindPanel();

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

		try {
			setVisible(false);

			configManager.setWindowHeight(getHeight());
			configManager.setWindowWidth(getWidth());
			configManager.setWindowX(getX());
			configManager.setWindowY(getY());
			configManager.saveConfiguration();

			dispose();

		} catch (ConfigurationException e) {
			logger.severe(e.getMessage());
			System.exit(1);
		}
	}
}
