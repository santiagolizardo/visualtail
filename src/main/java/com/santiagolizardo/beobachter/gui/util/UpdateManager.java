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
package com.santiagolizardo.beobachter.gui.util;

import static com.santiagolizardo.beobachter.resources.languages.Translator.tr;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import com.santiagolizardo.beobachter.Constants;
import com.santiagolizardo.beobachter.gui.MainWindow;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateManager extends Thread {

	private static final Logger logger = Logger.getLogger(UpdateManager.class.getName());
	
	private MainWindow mainWindow;

	public UpdateManager(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}

	public void checkForUpdate() {
		start();
	}

	@Override
	public void run() {
		InputStreamReader reader = null;
		BufferedReader buffer = null;
		try {
			URL url = new URL(Constants.APP_UPDATE_URL);
			reader = new InputStreamReader(url.openStream());
			buffer = new BufferedReader(reader);
			String version = buffer.readLine();
			int serverVersion = Integer.valueOf(version.replaceAll("\\.", ""));
			int currentVersion = Integer.valueOf(
					Constants.APP_VERSION.replaceAll("\\.", ""));
			if (serverVersion > currentVersion) {
				StringBuilder sb = new StringBuilder();
				sb.append(
						String.format(tr("New version %s is available"), version))
						.append(Constants.LINE_SEP).append(Constants.LINE_SEP);
				sb.append(tr("Please visit the project website")).append(
						Constants.LINE_SEP);
				DialogFactory.showInformationMessage(mainWindow, sb.toString());
			} else if (serverVersion <= currentVersion) {
				DialogFactory.showInformationMessage(mainWindow,
						tr("There are not updates available"));
			}

		} catch (IOException | NumberFormatException e) {
			DialogFactory.showErrorMessage(mainWindow,
					tr("Unable to fetch server information"));
		}
		finally {
			if( null != buffer ) {
				try {
					buffer.close();
				} catch (IOException ex) {
					logger.log(Level.SEVERE, null, ex);
				}
			}
			if( null != reader) {
				try {
					reader.close();
				} catch (IOException ex) {
					logger.log(Level.SEVERE, null, ex);
				}
			}
		}
	}
}
