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
package com.santiagolizardo.beobachter.gui.dialogs;

import static com.santiagolizardo.beobachter.resources.languages.Translator._;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.BoxLayout;

import com.santiagolizardo.beobachter.gui.MainWindow;
import com.santiagolizardo.beobachter.gui.dialogs.components.EditionPanel;
import com.santiagolizardo.beobachter.gui.dialogs.components.ListingPanel;

public class LogTypesDialog extends AbstractDialog {

	private static final long serialVersionUID = -1516233460385847703L;

	private ListingPanel listingPanel;
	private EditionPanel editionPanel;

	public LogTypesDialog(MainWindow mainGUI) {
		super(mainGUI);

		setTitle(_("Log types management"));
		setModal(true);
		setResizable(true);

		listingPanel = new ListingPanel();		
		
		editionPanel = new EditionPanel(this);
		listingPanel.setEditionPanel(editionPanel);
		
		Container container = getContentPane();
		//container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));

		container.add(listingPanel, BorderLayout.WEST);
		container.add(editionPanel, BorderLayout.CENTER);

		pack();
		
		setLocationRelativeTo(mainGUI);
	}
}
