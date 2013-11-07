/**
 * Beobachter is a logs watcher for the desktop. (a.k.a. full-featured tail)
 * Copyright (C) 2011 Santiago Lizardo (http://www.santiagolizardo.com)
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
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.LogManager;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import com.santiagolizardo.beobachter.config.ConfigManager;
import com.santiagolizardo.beobachter.engine.Controller;
import com.santiagolizardo.beobachter.gui.dialogs.components.FindPanel;
import com.santiagolizardo.beobachter.gui.menu.Menu;
import com.santiagolizardo.beobachter.gui.util.SwingUtil;
import com.santiagolizardo.beobachter.resources.images.IconFactory;
import com.santiagolizardo.beobachter.resources.languages.Translator;

/**
 * This is the main application entry point. It constructs the initial window.
 * 
 * @author slizardo
 * 
 */
public class MainGUI extends JFrame {

	private static final long serialVersionUID = -349295815866572937L;

	public static MainGUI instance = null;

	public static void main(String[] args) {
		ConfigManager configManager = null;

		try {
			Properties prop = System.getProperties();
			prop.setProperty("java.util.logging.config.file",
					"logging.properties");
			LogManager.getLogManager().readConfiguration();
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
		
		try {
			configManager = new ConfigManager(Constants.CONFIG_FILE);
			configManager.loadConfiguration();

			SwingUtil.setLookAndFeel(configManager.getWindowLAF());

			Translator.start(configManager);

			File dirLogTypes = new File(Constants.FOLDER_LOG_TYPES);
			if (!dirLogTypes.exists()) {
				dirLogTypes.mkdirs();
			}
			File dirSessions = new File(Constants.FOLDER_SESSIONS);
			if (!dirSessions.exists()) {
				dirSessions.mkdirs();
			}

			instance = new MainGUI(configManager);
			instance.setVisible(true);
		} catch (Exception ioe) {
			ioe.printStackTrace();
		}
	}

	public ConfigManager configManager;
	public JDesktopPane desktop;
	private FindPanel findPanel;

	public MainGUI(ConfigManager configManager) {
		this.configManager = configManager;

		desktop = new JDesktopPane();
		Menu menu = new Menu(desktop, this);
		setJMenuBar(menu);
		setTitle(Constants.APP_NAME);
		setSize(configManager.getWindowWidth(), configManager.getWindowHeight());
		setLocation(configManager.getWindowX(), configManager.getWindowY());
		setIconImage(IconFactory.getImage("logo.png").getImage());
		desktop.setDragMode(configManager.getDragMode());

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				super.windowClosing(event);
				Controller.exit();
			}
		});

		JScrollPane scroll = new JScrollPane(desktop);
		getContentPane().add(scroll);
	}

	public ConfigManager getConfigManager() {
		return configManager;
	}

	public void setConfigManager(ConfigManager configManager) {
		this.configManager = configManager;
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
}
