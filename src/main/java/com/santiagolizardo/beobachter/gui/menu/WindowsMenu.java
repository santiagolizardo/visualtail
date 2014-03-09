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
package com.santiagolizardo.beobachter.gui.menu;

import com.santiagolizardo.beobachter.Constants;
import static com.santiagolizardo.beobachter.resources.languages.Translator._;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.santiagolizardo.beobachter.gui.MainWindow;
import com.santiagolizardo.beobachter.resources.images.IconFactory;

public class WindowsMenu extends JMenu {

	private static final long serialVersionUID = 3376590111106134179L;

	public WindowsMenu(final MainWindow parentFrame) {

		setText(_("Windows"));
		setMnemonic(KeyEvent.VK_W);

		JMenuItem itemCascade = new JMenuItem(_("Cascade windows"));
		itemCascade.setIcon(IconFactory.getImage("application_double.png"));
		itemCascade.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				KeyEvent.CTRL_MASK));
		itemCascade.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				parentFrame.getDesktop().setWindowsOnCascade();
			}
		});

		JMenuItem itemTileVer = new JMenuItem(_("Tile windows vertically"));
		itemTileVer.setIcon(IconFactory
				.getImage("application_tile_vertical.png"));
		itemTileVer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K,
				KeyEvent.CTRL_MASK));
		itemTileVer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				parentFrame.getDesktop().setWindowsOnTileVertical();
			}
		});

		JMenuItem itemTileHor = new JMenuItem(_("Tile windows horizontally"));
		itemTileHor.setIcon(IconFactory
				.getImage("application_tile_horizontal.png"));
		itemTileHor.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				KeyEvent.CTRL_MASK));
		itemTileHor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				parentFrame.getDesktop().setWindowsOnTileHorizontal();
			}
		});

		JMenuItem miCloseAllWindows = new JMenuItem(_("Close all windows"));
		miCloseAllWindows.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
				KeyEvent.CTRL_MASK));
		miCloseAllWindows.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				parentFrame.getDesktop().closeAllWindows();

				parentFrame.setTitle(Constants.APP_NAME);
				parentFrame.updateActions(0);
			}
		});

		add(itemCascade);
		add(itemTileHor);
		add(itemTileVer);
		addSeparator();
		add(miCloseAllWindows);
	}
}
