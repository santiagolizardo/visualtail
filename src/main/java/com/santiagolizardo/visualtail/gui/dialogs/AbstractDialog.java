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
package com.santiagolizardo.visualtail.gui.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

public abstract class AbstractDialog extends JDialog {

	private static final long serialVersionUID = -8559961506669731891L;

	public AbstractDialog(JFrame parentFrame) {
		super(parentFrame);
	}

	@Override
	protected JRootPane createRootPane() {
		JRootPane superRootPane = super.createRootPane();

		superRootPane.registerKeyboardAction((ActionEvent) -> {
			setVisible(false);
			dispose();
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

		return superRootPane;
	}
}
