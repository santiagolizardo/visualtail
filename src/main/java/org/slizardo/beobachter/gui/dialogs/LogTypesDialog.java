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
package org.slizardo.beobachter.gui.dialogs;


import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import org.slizardo.beobachter.gui.dialogs.components.EditionPanel;
import org.slizardo.beobachter.gui.dialogs.components.ListingPanel;

public class LogTypesDialog extends JDialog {

	private static final long serialVersionUID = -1516233460385847703L;

	private ListingPanel listingPanel;
	private EditionPanel editionPanel;

	public LogTypesDialog() {
		setTitle("Log types configuration");
		setModal(true);

		editionPanel = new EditionPanel();
		
		listingPanel = new ListingPanel();
		listingPanel.setEditionPanel(editionPanel);

		Container container = getContentPane();
		container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));

		container.add(listingPanel);
		container.add(editionPanel);

		pack();
		setLocationRelativeTo(null);
	}

	@Override
	protected JRootPane createRootPane() {
		JRootPane rootPane = super.createRootPane();

		rootPane.registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				dispose();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

		return rootPane;
	}
}
