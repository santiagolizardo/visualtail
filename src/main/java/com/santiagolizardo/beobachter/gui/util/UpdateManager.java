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
package com.santiagolizardo.beobachter.gui.util;

import static com.santiagolizardo.beobachter.resources.languages.Translator._;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import com.santiagolizardo.beobachter.Constants;
import com.santiagolizardo.beobachter.gui.MainWindow;

public class UpdateManager extends Thread {

	private MainWindow mainGUI;

	public UpdateManager(MainWindow mainGUI) {
		this.mainGUI = mainGUI;
	}

	public void checkForUpdate() {
		start();
	}

	public void run() {
		try {
			URL url = new URL(Constants.APP_UPDATE_URL);
			InputStreamReader reader = new InputStreamReader(url.openStream());
			BufferedReader buffer = new BufferedReader(reader);
			String version = buffer.readLine();
			buffer.close();
			reader.close();
			int serverVersion = Integer.valueOf(version.replaceAll("\\.", ""))
					.intValue();
			int currentVersion = Integer.valueOf(
					Constants.APP_VERSION.replaceAll("\\.", "")).intValue();
			if (serverVersion > currentVersion) {
				StringBuilder sb = new StringBuilder();
				sb.append(
						String.format(_("New version %s is available"), version))
						.append(Constants.LINE_SEP).append(Constants.LINE_SEP);
				sb.append(_("Please visit the project website")).append(
						Constants.LINE_SEP);
				DialogFactory.showInformationMessage(mainGUI, sb.toString());
			} else if (serverVersion <= currentVersion) {
				DialogFactory.showInformationMessage(mainGUI,
						_("There are not updates available"));
			}

		} catch (Exception e) {
			DialogFactory.showErrorMessage(mainGUI,
					_("Unable to fetch server information"));
		}

	}
}
