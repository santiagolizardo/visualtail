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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.santiagolizardo.beobachter.gui.MainWindow;
import com.santiagolizardo.beobachter.gui.dialogs.LogWindow;
import com.santiagolizardo.beobachter.resources.images.IconFactory;
import com.santiagolizardo.beobachter.resources.languages.Translator;

public class FindPanel extends JPanel {

	private static final long serialVersionUID = -750096502886630895L;

	private JTextField searchTextField;
	private JButton closeButton;

	public FindPanel(final MainWindow mainWindow) {

		searchTextField = new JTextField(20);
		searchTextField.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent ev) {
				int keyCode = ev.getKeyCode();
				if (keyCode == KeyEvent.VK_ENTER) {
					LogWindow log = (LogWindow) mainWindow.getDesktop()
							.getSelectedFrame();
					if (log != null) {
						log.searchText(searchTextField.getText());
					}
				}
			}
		});

		closeButton = new JButton(IconFactory.getImage("close.png"));
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				mainWindow.removeFindPanel();
			}
		});

		defineLayout();
	}

	public void focus() {
		searchTextField.requestFocusInWindow();
	}

	public void defineLayout() {
		BoxLayout box = new BoxLayout(this, BoxLayout.X_AXIS);
		setLayout(box);

		JLabel searchLabel = new JLabel(Translator.tr("Search") + ":");

		add(searchLabel);
		add(searchTextField);
		add(closeButton);
	}
}
