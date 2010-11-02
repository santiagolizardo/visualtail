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
package org.slizardo.beobachter.gui.dialogs.components;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.slizardo.beobachter.MainGUI;
import org.slizardo.beobachter.gui.dialogs.LogWindow;
import org.slizardo.beobachter.resources.images.IconFactory;
import org.slizardo.beobachter.resources.languages.Translator;

public class FindPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -750096502886630895L;
	private JTextField search;
	private JButton close;
	
	public FindPanel() {

		search = new JTextField(20);
		search.addKeyListener(new KeyAdapter() {
			
			public void keyReleased(KeyEvent e) {
				int modifiers = e.getModifiers();
				int keyCode = e.getKeyCode();
				if(modifiers == KeyEvent.CTRL_MASK && keyCode == KeyEvent.VK_ENTER) {
					LogWindow log = (LogWindow) MainGUI.instance.desktop
					.getSelectedFrame();
					if (log != null) {
						log.searchText(search.getText());
					}
				}
			}
		});
				
		close = new JButton(IconFactory.getImage("close.png"));
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainGUI.instance.removeFindPanel();
			}
		});
		
		defineLayout();
	}
	
	public void focus() {
		search.requestFocusInWindow();
	}
	
	public void defineLayout() {
		BoxLayout box = new BoxLayout(this, BoxLayout.X_AXIS);
		setLayout(box);

		JLabel _search = new JLabel(Translator.t("Search")+":");
		
		add(_search);
		add(search);
		add(close);
	}
}
