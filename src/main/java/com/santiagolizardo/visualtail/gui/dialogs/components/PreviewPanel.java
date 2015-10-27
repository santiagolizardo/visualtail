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
package com.santiagolizardo.visualtail.gui.dialogs.components;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PreviewPanel extends JPanel implements ChangeListener {

	private static final long serialVersionUID = -7883502338884979025L;

	private final JLabel label;

	public PreviewPanel(JColorChooser chooser) {
		super();
		label = new JLabel("EXAMPLE");

		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setLayout(new BorderLayout());

		add(label);

		chooser.getSelectionModel().addChangeListener(this);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		ColorSelectionModel model = (ColorSelectionModel) e.getSource();
		label.setForeground(model.getSelectedColor());
	}
}
