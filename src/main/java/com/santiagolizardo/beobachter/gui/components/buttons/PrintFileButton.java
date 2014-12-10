/**
 * This file is part of Beobachter, a graphical log file monitor.
 *
 * Beobachter is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Beobachter is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Beobachter. If not, see <http://www.gnu.org/licenses/>.
 */
package com.santiagolizardo.beobachter.gui.components.buttons;

import com.santiagolizardo.beobachter.gui.dialogs.LogWindow;
import com.santiagolizardo.beobachter.resources.languages.Translator;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Logger;
import javax.swing.JButton;

public class PrintFileButton extends JButton implements ActionListener {

	private static final Logger logger = Logger.getLogger(PrintFileButton.class.getName());

	private LogWindow logWindow;
	
	public PrintFileButton(LogWindow logWindow) {
		super(Translator.tr("Print this file"));
		
		addActionListener(this);
		
		this.logWindow = logWindow;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final Desktop desktop = Desktop.getDesktop();
		try {
			desktop.print(logWindow.getFile());
		} catch( IOException ioe ) {
			logger.warning(ioe.getMessage());
		}
	}
}