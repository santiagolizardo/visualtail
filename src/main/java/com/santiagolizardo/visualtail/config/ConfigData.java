/*
  This file is part of VisualTail, a graphical log file monitor.

  VisualTail is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  VisualTail is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with VisualTail.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.santiagolizardo.visualtail.config;

import com.santiagolizardo.visualtail.beans.Session;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.UIManager;

public class ConfigData {

	private String windowLookAndFeel;
	private Point windowPosition;
	private Dimension windowDimension;
	
	private String language;
	
	private Font font;
	
	private final List<Session> sessions;
	private final List<String> recentFiles;

	public ConfigData() {
		windowLookAndFeel = UIManager.getSystemLookAndFeelClassName();
		windowPosition = new Point(20, 20);
		windowDimension = new Dimension(640, 480);
		
		language = "en";
		
		font = new Font("Courier", Font.PLAIN, 11);
		
		sessions = new LinkedList<>();
		recentFiles = new ArrayList<>(10);
	}

	public List<String> getRecentFiles() {
		return recentFiles;
	}

	public void setWindowDimension(Dimension windowDimension) {
		this.windowDimension = windowDimension;
	}

	public Dimension getWindowDimension() {
		return windowDimension;
	}

	public String getWindowLookAndFeel() {
		return windowLookAndFeel;
	}

	public void setWindowLookAndFeel(String windowLookAndFeel) {
		this.windowLookAndFeel = windowLookAndFeel;
	}

	public Point getWindowPosition() {
		return windowPosition;
	}

	public void setWindowPosition(Point windowPosition) {
		this.windowPosition = windowPosition;
	}

	public String getLastPath() {
		if(recentFiles.isEmpty()) {
			return System.getProperty("user.home");
		}
		return recentFiles.get(recentFiles.size() - 1);
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public List<Session> getSessions() {
		return sessions;
	}
}
