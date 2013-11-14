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
package com.santiagolizardo.beobachter.config;

import java.util.ArrayList;
import java.util.List;

import javax.swing.UIManager;

import org.apache.commons.configuration.PropertiesConfiguration;

import com.santiagolizardo.beobachter.MainGUI;

public class ConfigData {

	// Window properties
	private final String WINDOW_POSITION_X = "window.position.x";

	private final String WINDOW_POSITION_Y = "window.position.y";

	private final String WINDOW_DIMENSION_WIDTH = "window.dimension.width";

	private final String WINDOW_DIMENSION_HEIGHT = "window.dimension.height";

	private final String WINDOW_LAF = "window.laf";

	// App properties
	private final String LANGUAGE = "language";

	private final String DRAG_MODE = "drag_mode";

	private final String LAST_PATH = "last_path";

	// Font properties
	private final String FONT_FAMILY = "font.family";

	private final String FONT_SIZE = "font.size";

	private PropertiesConfiguration configuration;

	public ConfigData() {
	}

	public List<String> getRecentFiles() {
		List<String> recentFiles = new ArrayList<String>();
		for (byte i = 0; i < 10; i++) {
			String recentFile = configuration.getString("recent." + i
					+ ".file_name");
			if (recentFile != null)
				recentFiles.add(recentFile);
		}
		return recentFiles;
	}

	public int getWindowWidth() {
		return configuration.getInt(WINDOW_DIMENSION_WIDTH);
	}

	public void setWindowWidth(int windowWidth) {
		configuration.setProperty(WINDOW_DIMENSION_WIDTH, windowWidth);
	}

	public int getWindowHeight() {
		return configuration.getInt(WINDOW_DIMENSION_HEIGHT);
	}

	public void setWindowHeight(int windowHeight) {
		configuration.setProperty(WINDOW_DIMENSION_HEIGHT, windowHeight);
	}

	public String getWindowLAF() {
		String laf = configuration.getString(WINDOW_LAF);
		if (laf == null) {
			laf = UIManager.getSystemLookAndFeelClassName();
			configuration.setProperty(WINDOW_LAF, laf);
		}
		return laf;
	}

	public void setWindowLAF(String laf) {
		configuration.setProperty(WINDOW_LAF, laf);
	}

	public int getWindowX() {
		return configuration.getInt(WINDOW_POSITION_X);
	}

	public void setWindowX(int windowX) {
		configuration.setProperty(WINDOW_POSITION_X, windowX);
	}

	public int getWindowY() {
		return configuration.getInt(WINDOW_POSITION_Y);
	}

	public void setWindowY(int windowY) {
		configuration.setProperty(WINDOW_POSITION_Y, windowY);
	}

	public String getLastPath() {
		String lastPath = configuration.getString(LAST_PATH);
		if (lastPath == null) {
			lastPath = System.getProperty("user.home");
			configuration.setProperty(LAST_PATH, lastPath);
		}
		return lastPath;
	}

	public void setLastPath(String lastPath) {
		configuration.setProperty(LAST_PATH, lastPath);
	}

	public int getDragMode() {
		return configuration.getInt(DRAG_MODE);
	}

	public void setDragMode(int dragMode) {
		configuration.setProperty(DRAG_MODE, dragMode);
		MainGUI.instance.desktop.setDragMode(dragMode);
	}

	public String getLanguage() {
		return configuration.getString(LANGUAGE);
	}

	public void setLanguage(String language) {
		configuration.setProperty(LANGUAGE, language);
	}

	public String getFontFamily() {
		return configuration.getString(FONT_FAMILY);
	}

	public void setFontFamily(String fontFamily) {
		configuration.setProperty(FONT_FAMILY, fontFamily);
	}

	public int getFontSize() {
		return configuration.getInt(FONT_SIZE);
	}

	public void setFontSize(int fontSize) {
		configuration.setProperty(FONT_SIZE, fontSize);
	}

	public PropertiesConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(PropertiesConfiguration configuration) {
		this.configuration = configuration;
	}
}
