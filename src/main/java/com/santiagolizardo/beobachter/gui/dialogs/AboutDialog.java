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
package com.santiagolizardo.beobachter.gui.dialogs;

import static com.santiagolizardo.beobachter.resources.languages.Translator._;

import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.santiagolizardo.beobachter.Constants;
import com.santiagolizardo.beobachter.gui.components.HtmlLabel;
import com.santiagolizardo.beobachter.resources.ResourcesLoader;
import com.santiagolizardo.beobachter.resources.images.IconFactory;
import com.santiagolizardo.beobachter.resources.languages.Translator;

public class AboutDialog extends AbstractDialog {

	private static final long serialVersionUID = -3985858584067350439L;

	public AboutDialog(JFrame parentFrame) {
		super(parentFrame);

		setTitle(Translator._("About this application"));
		setModal(true);

		defineLayout();
	}

	private void defineLayout() {
		Container container = getContentPane();
		container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));

		JLabel iconPanel = new JLabel(IconFactory.getImage("icon.png"));
				
		JPanel leftPanel = new JPanel();
		leftPanel.setAlignmentY(JPanel.TOP_ALIGNMENT);
		leftPanel.add(iconPanel);
	
		container.add(leftPanel);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		String headlineText = String.format("<h1>%s <em>v%s</em></h1>",
				Constants.APP_NAME, Constants.APP_VERSION);
		String infoText = String.format("<p>%s</p>", String.format(
				_("More info about the project at <a href=\"%s\">%s</a>."),
				Constants.APP_URL, Constants.APP_URL_DISPLAY));
		String creditsText = ResourcesLoader.readResource(AboutDialog.class,
				"credits.html");

		String content = headlineText.concat(infoText).concat(creditsText);
		JEditorPane lblCredits = new HtmlLabel(content);
		lblCredits.setCaretPosition(0);

		JScrollPane scrollPane = new JScrollPane(lblCredits);
		scrollPane.setPreferredSize(new Dimension(380, 240));
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		panel.setAlignmentY(JPanel.TOP_ALIGNMENT);
		panel.add(scrollPane);

		container.add(panel);

		pack();
		setLocationRelativeTo(getOwner());
	}
}

