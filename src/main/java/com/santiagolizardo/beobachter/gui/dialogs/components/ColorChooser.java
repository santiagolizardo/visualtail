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
package com.santiagolizardo.beobachter.gui.dialogs.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.colorchooser.AbstractColorChooserPanel;

public class ColorChooser {

	private JColorChooser colorChooser;
	private JDialog dialog;
	private Color returnColor;

	public ColorChooser(Component parent, String title, Color initialColor, final ActionListener actionListener) {
		colorChooser = new JColorChooser(initialColor);
		colorChooser.setPreviewPanel(new JPanel());

		AbstractColorChooserPanel[] panel = {colorChooser.getChooserPanels()[0]};
		colorChooser.setChooserPanels(panel);
		dialog = JColorChooser.createDialog(parent, title, true, colorChooser,
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						returnColor = colorChooser.getColor();
						dialog.setVisible(false);
						actionListener.actionPerformed(e);
					}
				}, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						returnColor = null;
						dialog.setVisible(false);
						actionListener.actionPerformed(e);
					}
				});
		dialog.setResizable(false);
		dialog.pack();
	}

	public void setVisible(boolean visible) {
		dialog.setVisible(visible);
	}

	public Color getSelectedColor() {
		return returnColor;
	}

	public JColorChooser getColorChooser() {
		return colorChooser;
	}
}
