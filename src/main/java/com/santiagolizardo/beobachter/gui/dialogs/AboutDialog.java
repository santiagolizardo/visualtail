/**
 * Beobachter is a logs watcher for the desktop. (a.k.a. full-featured tail)
 * Copyright (C) 2013 Santiago Lizardo (http://www.santiagolizardo.com)
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
package com.santiagolizardo.beobachter.gui.dialogs;

import static com.santiagolizardo.beobachter.resources.languages.Translator._;

import java.awt.Container;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import com.santiagolizardo.beobachter.Constants;
import com.santiagolizardo.beobachter.resources.ResourcesLoader;
import com.santiagolizardo.beobachter.resources.languages.Translator;

public class AboutDialog extends AbstractDialog {

	private static final long serialVersionUID = -3985858584067350439L;

	public AboutDialog(JFrame parentFrame) {
		super(parentFrame);

		setTitle(Translator._("About this application"));
		setSize(720, 480);
		setModal(true);
		setResizable(false);

		defineLayout();
	}

	private void defineLayout() {
		Container container = getContentPane();
		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));

		String versionUrl = String
				.format("<strong>%s</strong> <em>v%s</em><br />%s",
						Constants.APP_NAME,
						Constants.APP_VERSION,
						String.format(
								_("More info about the project at <a href=\"%s\">%s</a>."),
								Constants.APP_URL, Constants.APP_URL));
		JEditorPane lblVersion = new HtmlLabel(versionUrl);
		lblVersion.setOpaque(false);

		String credits = ResourcesLoader.readResource(AboutDialog.class,
				"credits.html");
		JEditorPane lblCredits = new HtmlLabel(credits);

		JScrollPane scrollPane = new JScrollPane(lblCredits);
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		container.add(lblVersion);
		container.add(scrollPane);

		setLocationRelativeTo(getOwner());
	}
}

class HtmlLabel extends JEditorPane {

	private static final long serialVersionUID = 1L;

	public HtmlLabel(String text) {
		super("text/html", text);

		setEditable(false);
		setHighlighter(null);

		addHyperlinkListener(new HyperlinkListener() {

			@Override
			public void hyperlinkUpdate(HyperlinkEvent ev) {
				if (ev.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					try {
						URI uri = ev.getURL().toURI();
						Desktop.getDesktop().browse(uri);
					} catch (IOException | URISyntaxException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
}
