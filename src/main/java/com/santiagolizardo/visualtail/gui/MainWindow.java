/**
 * This file is part of VisualTail, a graphical log file monitor.
 *
 * VisualTail is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * VisualTail is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * VisualTail. If not, see <http://www.gnu.org/licenses/>.
 */
package com.santiagolizardo.visualtail.gui;

import com.santiagolizardo.visualtail.Constants;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import com.santiagolizardo.visualtail.config.ConfigData;
import com.santiagolizardo.visualtail.config.ConfigFileWriter;
import com.santiagolizardo.visualtail.gui.actions.ActionFactory;
import com.santiagolizardo.visualtail.gui.components.DesktopPanel;
import com.santiagolizardo.visualtail.gui.dialogs.LogWindow;
import com.santiagolizardo.visualtail.gui.menu.Menu;
import com.santiagolizardo.visualtail.resources.images.IconFactory;

/**
 * This is the main application entry point. It constructs the initial window.
 */
public class MainWindow extends JFrame {

	private static final long serialVersionUID = -349295815866572937L;

	private final ConfigData configData;
	private final DesktopPanel desktop;

	private final ActionFactory actionFactory;

	private final Menu menu;

	public MainWindow(ConfigData configData) {
		this.configData = configData;

		actionFactory = new ActionFactory(this);

		desktop = new DesktopPanel();

		menu = new Menu(desktop, this);
		setJMenuBar(menu);

		setTitle(Constants.APP_NAME);

		setSize(configData.getWindowDimension());
		setLocation(configData.getWindowPosition());
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

		setVisible(false);

		configData.setWindowDimension(getSize());
		configData.setWindowPosition(getLocation());

		ConfigFileWriter configPersistence = new ConfigFileWriter();
		configPersistence.write(configData);

		dispose();

		System.exit(0);
	}

	public void updateActions(int delta) {
		boolean areWindowsOpen = desktop.getAllFrames().length + delta > 0;
		actionFactory.getSelectAllAction().setEnabled(areWindowsOpen);

		Menu mainMenu = ((Menu) getJMenuBar());
		mainMenu.getFileMenu().getSaveSessionMenuItem()
				.setEnabled(areWindowsOpen);
		mainMenu.getFileMenu().getDeleteFileMenuItem().setEnabled(areWindowsOpen);
		mainMenu.getEditMenu().setEnabled(areWindowsOpen);
		mainMenu.getWindowMenu().setEnabled(areWindowsOpen);
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
