/**
 * This file is part of Beobachter, a graphical log file monitor.
 *
 * Beobachter is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Beobachter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Beobachter.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.santiagolizardo.beobachter.gui.components;

import java.beans.PropertyVetoException;
import java.util.logging.Logger;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

public class DesktopPanel extends JDesktopPane {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(DesktopPanel.class
			.getName());
	
	/**
	 * This method relocates all frames contained on the desktop and sets them
	 * on cascade.
	 */
	public void setWindowsOnCascade() {
		JInternalFrame[] frames = getAllFrames();
		int numFrames = frames.length;
		if (numFrames == 0)
			return;

		int frameWidth = (int) (getWidth() * .75);
		int frameHeight = (int) (getHeight() * .75);

		int diffWidth = (int) (getWidth() * .25) / numFrames;
		int diffHeight = (int) (getHeight() * .25) / numFrames;

		short x = 0;
		short y = 0;

		for (byte i = 0; i < numFrames; i++) {
			try {
				frames[i].setMaximum(false);
				frames[i].setIcon(false);
			} catch (PropertyVetoException e) {
				logger.warning(e.getMessage());
			}

			frames[i].setSize(frameWidth, frameHeight);
			frames[i].setLocation(x, y);

			x += diffWidth;
			y += diffHeight;
		}
	}

	/**
	 * This method relocates all frames contained on the desktop and sets them
	 * on vertical tile.
	 */
	public void setWindowsOnTileVertical() {
		JInternalFrame[] frames = getAllFrames();
		int numFrames = frames.length;
		if (numFrames == 0)
			return;

		int frameWidth = getWidth();
		int frameHeight = getHeight() / numFrames;

		for (short i = 0; i < numFrames; i++) {
			try {
				frames[i].setMaximum(false);
				frames[i].setIcon(false);
			} catch (PropertyVetoException e) {
				logger.warning(e.getMessage());
			}

			frames[i].setSize(frameWidth, frameHeight);
			frames[i].setLocation(0, i * frameHeight);
		}
	}

	/**
	 * This method relocates all frames contained on the desktop and sets them
	 * on tile horizontal.
	 */
	public void setWindowsOnTileHorizontal() {
		JInternalFrame[] frames = getAllFrames();
		int numFrames = frames.length;
		if (numFrames == 0)
			return;

		int frameWidth = (getWidth() / numFrames);
		int frameHeight = getHeight();

		for (short i = 0; i < numFrames; i++) {
			try {
				frames[i].setMaximum(false);
				frames[i].setIcon(false);
			} catch (PropertyVetoException e) {
				logger.warning(e.getMessage());
			}

			frames[i].setSize(frameWidth, frameHeight);
			frames[i].setLocation(i * frameWidth, 0);
		}
	}
}
