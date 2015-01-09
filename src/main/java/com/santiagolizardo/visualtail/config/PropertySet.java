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
package com.santiagolizardo.visualtail.config;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class PropertySet extends Properties {

	private static final Logger logger = Logger.getLogger(PropertySet.class.getName());

	private String path;

	public PropertySet() {
	}

	public PropertySet(String path) {
		this.path = path;
		load();
	}

	public short getShort(String key) {
		return Short.valueOf(getProperty(key));
	}

	public void setProperty(String key, short value) {
		setProperty(key, Short.toString(value));
	}

	public void setProperty(String key, int value) {
		setProperty(key, Integer.toString(value));
	}

	public void setProperty(String key, boolean value) {
		setProperty(key, Boolean.toString(value));
	}
	
	public int getInt(String key) {
		return Integer.valueOf(getProperty(key));
	}
	
	public String getString(String key) {
		return getProperty(key);
	}

	public boolean getBoolean(String key) {
		return Boolean.valueOf(getProperty(key));
	}

	public void setColor(String key, Color value) {
		try {
			String intColorString = String.valueOf(value.getRGB());
			setProperty(key, intColorString);
		} catch (Exception e) {
			logger.warning(e.getMessage());
		}
	}

	public Color getColor(String key) {
		try {
			String intColorString = getProperty(key);
			return Color.decode(intColorString);
		} catch (NumberFormatException e) {
			logger.warning(e.getMessage());
			return null;
		}
	}

	public void load() {
		File file = new File(path);
		try {
			FileInputStream fis = new FileInputStream(file);
			load(fis);
		} catch(IOException ex) {
			logger.severe(ex.getMessage());
		}
	}

	public void save() throws FileNotFoundException, IOException {
		File file = new File(path);
		FileOutputStream fos = new FileOutputStream(file);
		store(fos, null);
	}

	public void save(String path) throws IOException {
		this.path = path;
		save();
	}
}
