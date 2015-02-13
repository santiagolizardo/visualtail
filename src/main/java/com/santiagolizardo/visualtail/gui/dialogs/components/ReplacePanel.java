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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.santiagolizardo.visualtail.gui.dialogs.LogWindow;
import com.santiagolizardo.visualtail.resources.images.IconFactory;
import com.santiagolizardo.visualtail.resources.languages.Translator;
import java.awt.Container;

public class ReplacePanel extends JPanel {

	private static final long serialVersionUID = -750096502886630895L;

	private JTextField searchTextField;
	private JTextField replaceTextField;
	private JButton closeButton;

	public ReplacePanel(final LogWindow logWindow) {

		replaceTextField = new JTextField(20);
		
		searchTextField = new JTextField(20);
		searchTextField.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent ev) {
				int keyCode = ev.getKeyCode();
				if (KeyEvent.VK_ENTER == keyCode ) {
					logWindow.searchText(searchTextField.getText());
				}
				else if (KeyEvent.VK_ESCAPE == keyCode) {
					logWindow.hideFindPanel();
				}
			}
		});

		closeButton = new JButton(IconFactory.getImage("close.png"));
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				logWindow.hideReplacePanel();
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

		JLabel searchLabel = new JLabel(Translator.tr("Replace") + ":");

		add(searchLabel);
		add(searchTextField);
		add(replaceTextField);
		add(closeButton);
	}
}