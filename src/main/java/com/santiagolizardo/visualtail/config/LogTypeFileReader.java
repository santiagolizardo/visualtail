/**
 * This file is part of VisualTail, a graphical log file monitor.
 *
 * VisualTail is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * VisualTail is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * VisualTail. If not, see <http://www.gnu.org/licenses/>.
 */
package com.santiagolizardo.visualtail.config;

import com.santiagolizardo.visualtail.beans.LogType;
import com.santiagolizardo.visualtail.beans.Rule;
import java.io.File;
import java.io.FileFilter;

import java.awt.Color;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class LogTypeFileReader extends LogTypeFileCommon {

	private static LogTypeFileReader singleton;

	public static final LogTypeFileReader getInstance() {
		if (null == singleton) {
			singleton = new LogTypeFileReader();
		}
		return singleton;
	}

	public LogType read(String name) {
		LogType logType = new LogType(name);
		try {
			String path = getPath(name);

			Document doc = documentBuilderFactory.newDocumentBuilder().parse(path);
			Element rootNode = doc.getDocumentElement();

			logType.setRefreshInterval(Integer.parseInt(rootNode.getAttribute(TAG_REFRESH_INTERVAL)));

			NodeList children = rootNode.getElementsByTagName(TAG_RULE);
			for (int i = 0; i < children.getLength(); i++) {
				Element ruleNode = (Element) children.item(i);
				Rule rule = new Rule();
				rule.setPattern(ruleNode.getAttribute(TAG_PATTERN));
				rule.setRegularExpression(Boolean.valueOf(ruleNode.getAttribute(TAG_REGULAR_EXPRESSION)));
				rule.setIgnoreCase(Boolean.valueOf(ruleNode.getAttribute(TAG_IGNORE_CASE)));
				rule.setBackgroundColor(Color.decode((ruleNode.getAttribute(TAG_BACKGROUND_COLOR))));
				rule.setForegroundColor(Color.decode((ruleNode.getAttribute(TAG_FOREGROUND_COLOR))));
				logType.addRule(rule);
			}

		} catch (ParserConfigurationException | SAXException | IOException | NumberFormatException e) {
			logger.warning(e.getMessage());
		}
		return logType;
	}
	
	public LogType[] readAll() {
		File logTypesDir = new File(FOLDER_LOG_TYPES);
		FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return (pathname.getName().endsWith(LOG_TYPE_FILE_EXTENSION));
			}
		};
		File[] files = logTypesDir.listFiles(filter);

		LogType[] logTypes = new LogType[files.length + 1];
		logTypes[0] = new LogType("Default");
		
		int i = 1;
		for (File file : files) {
			String fileName = file.getName();
			String name = fileName.replaceAll(LOG_TYPE_FILE_EXTENSION, "");
			LogType logType = read(name);
			logTypes[i++] = logType;
		}

		return logTypes;
	}
}
