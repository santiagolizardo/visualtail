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

import static com.santiagolizardo.visualtail.resources.languages.Translator.tr;

import java.awt.BorderLayout;
import java.awt.Container;


import com.santiagolizardo.visualtail.gui.MainWindow;
import com.santiagolizardo.visualtail.gui.dialogs.components.EditionPanel;
import com.santiagolizardo.visualtail.gui.dialogs.components.ListingPanel;

public class LogTypesDialog extends AbstractDialog {

	private static final long serialVersionUID = -1516233460385847703L;

	private final ListingPanel listingPanel;
	private final EditionPanel editionPanel;

	public LogTypesDialog(MainWindow mainWindow) {
		super(mainWindow);

		setTitle(tr("Log types management"));
		setModal(true);
		setResizable(true);

		listingPanel = new ListingPanel();		
		
		editionPanel = new EditionPanel(this);
		listingPanel.setEditionPanel(editionPanel);
		
		Container container = getContentPane();
		container.add(listingPanel, BorderLayout.WEST);
		container.add(editionPanel, BorderLayout.CENTER);

		pack();
		
		setLocationRelativeTo(mainWindow);
	}
}
