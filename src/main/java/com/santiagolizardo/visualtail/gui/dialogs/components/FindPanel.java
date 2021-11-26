/*
  This file is part of VisualTail, a graphical log file monitor.

  VisualTail is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  VisualTail is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with VisualTail.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.santiagolizardo.visualtail.gui.dialogs.components;

import com.santiagolizardo.visualtail.gui.components.EnhancedTextField;
import com.santiagolizardo.visualtail.gui.components.buttons.CloseButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.santiagolizardo.visualtail.gui.dialogs.LogWindow;
import com.santiagolizardo.visualtail.resources.languages.Translator;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JToggleButton;

public class FindPanel extends JPanel {


	private EnhancedTextField searchTextField;
	private JToggleButton caseSensitiveButton;
	private final JButton closeButton;

	public FindPanel(final LogWindow logWindow) {

		searchTextField = new EnhancedTextField(40);
		searchTextField.setPlaceholder(Translator.tr("Search pattern..."));
		searchTextField.setMaximumSize(searchTextField.getPreferredSize());
		searchTextField.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent ev) {
				int keyCode = ev.getKeyCode();
				if (KeyEvent.VK_ENTER == keyCode ) {
					String searchValue = searchTextField.getText();
					if(!searchValue.isEmpty()) {
						logWindow.searchText(searchValue, caseSensitiveButton.isSelected());
					}
				}
				else if (KeyEvent.VK_ESCAPE == keyCode) {
					logWindow.hideFindPanel();
				}
			}
		});
		
		caseSensitiveButton = new JToggleButton(Translator.tr("Case sensitive"));

		closeButton = new CloseButton();
		closeButton.addActionListener((ActionEvent) -> {
			logWindow.hideFindPanel();
		});

		defineLayout();
	}

	public void focus() {
		searchTextField.selectAll();
		searchTextField.requestFocusInWindow();
	}

	public void defineLayout() {
		BoxLayout box = new BoxLayout(this, BoxLayout.X_AXIS);
		setLayout(box);
		setBorder(BorderFactory.createEmptyBorder( 1, 3, 1, 3 ));

		add(searchTextField);
		add(caseSensitiveButton);
		add(Box.createHorizontalGlue());
		add(closeButton);
	}
}
