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

		int frameWidth = (getWidth() * 75) / 100;
		int frameHeight = (getHeight() * 75) / 100;

		int diffWidth = ((getWidth() * 25) / 100) / numFrames;
		int diffHeight = ((getHeight() * 25) / 100) / numFrames;

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
