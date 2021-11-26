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
package com.santiagolizardo.visualtail.gui.menu;

import com.santiagolizardo.visualtail.Constants;
import static com.santiagolizardo.visualtail.resources.languages.Translator.tr;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.santiagolizardo.visualtail.gui.MainWindow;
import com.santiagolizardo.visualtail.resources.images.IconFactory;
import java.awt.Toolkit;

public class WindowsMenu extends JMenu {


	public WindowsMenu(final MainWindow parentFrame) {

		int menuShortcutKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
		
		setText(tr("Windows"));
		setMnemonic(KeyEvent.VK_W);

		JMenuItem itemCascade = new JMenuItem(tr("Cascade windows"));
		itemCascade.setIcon(IconFactory.getImage("application_double.png"));
		itemCascade.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, menuShortcutKeyMask));
		itemCascade.addActionListener((ActionEvent) -> {
			parentFrame.getDesktop().setWindowsOnCascade();
		});

		JMenuItem itemTileVer = new JMenuItem(tr("Tile windows vertically"));
		itemTileVer.setIcon(IconFactory
				.getImage("application_tile_vertical.png"));
		itemTileVer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K,menuShortcutKeyMask));
		itemTileVer.addActionListener((ActionEvent) -> {
			parentFrame.getDesktop().setWindowsOnTileVertical();
		});

		JMenuItem itemTileHor = new JMenuItem(tr("Tile windows horizontally"));
		itemTileHor.setIcon(IconFactory
				.getImage("application_tile_horizontal.png"));
		itemTileHor.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,menuShortcutKeyMask));
		itemTileHor.addActionListener((ActionEvent) -> {
			parentFrame.getDesktop().setWindowsOnTileHorizontal();
		});

		JMenuItem miCloseAllWindows = new JMenuItem(tr("Close all windows"));
		miCloseAllWindows.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,menuShortcutKeyMask));
		miCloseAllWindows.addActionListener((ActionEvent) -> {
			parentFrame.getDesktop().closeAllWindows();
			
			parentFrame.setTitle(Constants.APP_NAME);
			parentFrame.updateActions(0);
		});

		add(itemCascade);
		add(itemTileHor);
		add(itemTileVer);
		addSeparator();
		add(miCloseAllWindows);
	}
}
