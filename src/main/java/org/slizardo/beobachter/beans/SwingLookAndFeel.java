/**
 * Beobachter, the universal logs watcher
 * Copyright (C) 2009  Santiago Lizardo

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.slizardo.beobachter.beans;

import javax.swing.LookAndFeel;

public class SwingLookAndFeel {

	private String name;
	private String className;

	/**
	 * Instantiates a new look and feel from its class name.
	 * @param className
	 * @return The instantiated look and feel
	 * @throws Exception in case of an error
	 */
	public static SwingLookAndFeel forName(String className) throws Exception {
		LookAndFeel lookAndFeel = (LookAndFeel) Class.forName(className)
				.newInstance();
		return new SwingLookAndFeel(lookAndFeel.getName(), lookAndFeel.getClass()
				.getName());
	}

	public SwingLookAndFeel(String name, String className) {
		this.name = name;
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}

		if (o instanceof SwingLookAndFeel) {
			SwingLookAndFeel laf = (SwingLookAndFeel) o;
			return className.equals(laf.getClassName());
		}

		return false;
	}

	public int hashCode() {
		return name.hashCode();
	}

	public String toString() {
		return name;
	}
}
