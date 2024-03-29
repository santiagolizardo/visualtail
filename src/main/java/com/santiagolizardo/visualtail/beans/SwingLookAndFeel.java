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
package com.santiagolizardo.visualtail.beans;

import javax.swing.LookAndFeel;

public class SwingLookAndFeel {

	private String name;
	private String className;

	/**
	 * Instantiates a new look and feel from its class name.
	 * 
	 * @return The instantiated look and feel
	 * @throws Exception
	 *             in case of an error
	 */
	public static SwingLookAndFeel forName(String className) throws Exception {
		LookAndFeel lookAndFeel = (LookAndFeel) Class.forName(className).getDeclaredConstructor().newInstance();
		return new SwingLookAndFeel(lookAndFeel.getName(), lookAndFeel
				.getClass().getName());
	}

	public SwingLookAndFeel(String name, String className) {
		this.name = name;
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	public String getName() {
		return name;
	}

	@Override
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

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return name;
	}
}
