/**
 * Beobachter is a logs watcher for the desktop. (a.k.a. full-featured tail)
 * Copyright (C) 2011 Santiago Lizardo (http://www.santiagolizardo.com)
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
package com.santiagolizardo.beobachter.gui.dialogs.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.colorchooser.AbstractColorChooserPanel;

public class ColorChooser  {

	private JColorChooser colorChooser;
	private JDialog dialog;
	private Color returnColor;
	
	public ColorChooser(Component parent, String title, Color initialColor) {
		colorChooser = new JColorChooser(initialColor);
		colorChooser.setPreviewPanel(new JPanel());

		AbstractColorChooserPanel[] panel = { colorChooser.getChooserPanels()[0] };
		colorChooser.setChooserPanels(panel);
		dialog = JColorChooser.createDialog(parent, title, true, colorChooser, 
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						returnColor = colorChooser.getColor();
						colorChooser.setVisible(false);
						dialog.dispose();
					}
				}, new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						returnColor = null;
						colorChooser.setVisible(false);
						dialog.dispose();
					}
				});
		dialog.setResizable(false);
		dialog.pack();
		dialog.setVisible(true);
	}
	
	public Color pickColor() {
		return returnColor;
	}
}
