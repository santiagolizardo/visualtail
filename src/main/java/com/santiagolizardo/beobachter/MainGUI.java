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

import com.santiagolizardo.beobachter.config.ConfigData;
import com.santiagolizardo.beobachter.config.ConfigPersistence;
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

	public ConfigData configData;
	public DesktopPanel desktop;
	private FindPanel findPanel;

	private Logger logger;

	public MainGUI(ConfigData configData) {
		this.configData = configData;

		logger = Logger.getLogger(MainGUI.class.getName());

		desktop = new DesktopPanel();
		Menu menu = new Menu(desktop, this);
		setJMenuBar(menu);
		setTitle(Constants.APP_NAME);
		setSize(configData.getWindowWidth(), configData.getWindowHeight());
		setLocation(configData.getWindowX(), configData.getWindowY());
		setIconImage(IconFactory.getImage("icon.png").getImage());
		desktop.setDragMode(configData.getDragMode());

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

			configData.setWindowHeight(getHeight());
			configData.setWindowWidth(getWidth());
			configData.setWindowX(getX());
			configData.setWindowY(getY());

			ConfigPersistence configPersistence = new ConfigPersistence();
			configPersistence.saveProperties(configData.getConfiguration());

			dispose();

		} catch (ConfigurationException e) {
			logger.severe(e.getMessage());
			System.exit(1);
		}
	}
}
