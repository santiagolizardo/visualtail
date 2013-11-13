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
package com.santiagolizardo.beobachter.gui.util;

import java.awt.Toolkit;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class SwingUtil {

	private static Toolkit toolkit = null;

	static {
		toolkit = Toolkit.getDefaultToolkit();
	}

	public static void beep() {
		toolkit.beep();
	}

	public static void setSystemLookAndFeel() {
		setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	}

	public static void setCrossPlatformLookAndFeel() {
		setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
	}

	public static void setLookAndFeel(String className) {
		try {
			UIManager.setLookAndFeel(className);
		} catch (ClassNotFoundException cnf) {
			cnf.printStackTrace();
		} catch (UnsupportedLookAndFeelException ulaf) {
			ulaf.printStackTrace();
		} catch (IllegalAccessException ia) {
			ia.printStackTrace();
		} catch (InstantiationException i) {
			i.printStackTrace();
		}
	}
}