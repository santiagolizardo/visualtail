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
package org.slizardo.beobachter.gui.dialogs;


import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URI;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;

import org.slizardo.beobachter.Constants;
import org.slizardo.beobachter.resources.ResourcesLoader;
import org.slizardo.beobachter.resources.languages.Translator;

public class AboutDialog extends JDialog {

	private static final long serialVersionUID = -3985858584067350439L;

	public AboutDialog() {

		setTitle(Translator.t("About_this_application"));
		setSize(320, 270);
		setModal(true);
		setResizable(false);

		defineLayout();
		setLocationRelativeTo(null);
	}

	private void defineLayout() {
		Container container = getContentPane();
		container.setBackground(Color.WHITE);
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

		String credits = ResourcesLoader.readResource(AboutDialog.class, "CREDITS.html");
		
		JLabel lblName = new JLabel(Constants.APP_NAME);
		Font fontName = lblName.getFont();
		fontName = fontName.deriveFont(25f);
		lblName.setFont(fontName);
		JLabel lblVersion = new JLabel("Version " + Constants.APP_VERSION);
		Font fontVersion = lblVersion.getFont();
		fontVersion = fontVersion.deriveFont(Font.ITALIC);
		lblVersion.setFont(fontVersion);
		JLabel lblCredits = new JLabel(credits);
		
		JScrollPane scrollPane = new JScrollPane(lblCredits);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		container.add(lblName);
		container.add(lblVersion);
		container.add(scrollPane);
		
		final Desktop desktop = Desktop.getDesktop();
		
		if(desktop.isSupported(Action.BROWSE)) {
			JButton btnWebsite = new JButton(Translator.t("Visit_the_website"));
			btnWebsite.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					try {
						URI uri = new URI("http://www.sourceforge.net/projects/beobachter");
						desktop.browse(uri);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			container.add(btnWebsite);
		} else {
			JLabel lblWebsite = new JLabel("http://www.sourceforge.net/projects/beobachter");
			lblWebsite.setForeground(Color.BLUE);
			
			container.add(lblWebsite);
		}
	}
	
	@Override
	protected JRootPane createRootPane() {
		JRootPane rootPane = super.createRootPane();

		rootPane.registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				dispose();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

		return rootPane;
	}
}
