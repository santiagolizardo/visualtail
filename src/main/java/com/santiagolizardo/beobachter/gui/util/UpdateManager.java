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
import java.text.MessageFormat;

import com.santiagolizardo.beobachter.Constants;
import com.santiagolizardo.beobachter.MainGUI;

public class UpdateManager extends Thread {

	public static void checkForUpdate() {
		UpdateManager updateManager = new UpdateManager();
		updateManager.start();
	}

	private UpdateManager() {
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
						MessageFormat.format(_("New_version_0_available"),
								new Object[] { version }))
						.append(Constants.LINE_SEP).append(Constants.LINE_SEP);
				sb.append(_("Please_visit_us_on_sourceforge")).append(
						Constants.LINE_SEP);
				DialogFactory.showInformationMessage(MainGUI.instance,
						sb.toString());
			} else if (serverVersion <= currentVersion) {
				DialogFactory.showInformationMessage(MainGUI.instance,
						_("There_are_not_updates_available"));
			}

		} catch (Exception e) {
			DialogFactory.showErrorMessage(MainGUI.instance,
					_("Unable_to_fetch_server_information"));
		}

	}
}