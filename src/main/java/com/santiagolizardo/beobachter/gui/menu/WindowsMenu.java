/**
 * Beobachter is a logs watcher for the desktop. (a.k.a. full-featured tail)
 * Copyright (C) 2011 Santiago Lizardo (http://www.santiagolizardo.com)
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
package com.santiagolizardo.beobachter.gui.menu;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.santiagolizardo.beobachter.MainGUI;
import com.santiagolizardo.beobachter.engine.Controller;
import com.santiagolizardo.beobachter.resources.images.IconFactory;
import com.santiagolizardo.beobachter.resources.languages.Translator;

public class WindowsMenu extends JMenu {

	private static final long serialVersionUID = 3376590111106134179L;

	public WindowsMenu() {

		setText(Translator.t("Windows"));
		setMnemonic(KeyEvent.VK_W);

		JMenuItem itemCascade = new JMenuItem(Translator.t("Cascade_windows"));
		itemCascade.setIcon(IconFactory.getImage("application_double.png"));
		itemCascade.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				KeyEvent.CTRL_MASK));
		itemCascade.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Controller.setWindowsOnCascade(MainGUI.instance.desktop);
			}
		});

		JMenuItem itemTileVer = new JMenuItem(Translator
				.t("Tile_windows_vertically"));
		itemTileVer.setIcon(IconFactory
				.getImage("application_tile_vertical.png"));
		itemTileVer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K,
				KeyEvent.CTRL_MASK));
		itemTileVer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				Controller
						.setWindowsOnTileVertical(MainGUI.instance.desktop);
			}
		});

		JMenuItem itemTileHor = new JMenuItem(Translator
				.t("Tile_windows_horizontally"));
		itemTileHor.setIcon(IconFactory
				.getImage("application_tile_horizontal.png"));
		itemTileHor.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				KeyEvent.CTRL_MASK));
		itemTileHor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Controller
						.setWindowsOnTileHorizontal(MainGUI.instance.desktop);
			}
		});

		add(itemCascade);
		add(itemTileHor);
		add(itemTileVer);
	}
}
