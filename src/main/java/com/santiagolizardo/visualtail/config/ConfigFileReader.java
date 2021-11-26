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
package com.santiagolizardo.visualtail.config;

import com.santiagolizardo.visualtail.beans.Session;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ConfigFileReader extends ConfigFileCommon {

	public ConfigData read() {
		ConfigData config = new ConfigData();

		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			Document doc = documentBuilderFactory.newDocumentBuilder().parse(CONFIG_FILE_PATH);
			Element rootNode = doc.getDocumentElement();

			config.setLanguage(rootNode.getAttribute(TAG_LANGUAGE));

			readWindow(rootNode, config);
			readFont(rootNode, config);
			readRecentFiles(rootNode, config);
			readSessions(rootNode, config);

		} catch (ParserConfigurationException | SAXException | IOException | NumberFormatException | DOMException e) {
			logger.warning(e.getMessage());
		}

		return config;
	}

	private void readFont(Element rootNode, ConfigData config) {
		Element fontNode = (Element) rootNode.getElementsByTagName(TAG_FONT).item(0);
		Font font = new Font(fontNode.getAttribute(TAG_FAMILY), Font.PLAIN, Integer.parseInt(fontNode.getAttribute(TAG_SIZE)));
		config.setFont(font);
	}

	private void readWindow(Element rootNode, ConfigData config) {
		Element windowNode = (Element) rootNode.getElementsByTagName(TAG_WINDOW).item(0);
		config.setWindowLookAndFeel(windowNode.getAttribute(TAG_LOOK_AND_FEEL));
		Point position = new Point();
		position.x = Integer.parseInt(windowNode.getAttribute(TAG_X));
		position.y = Integer.parseInt(windowNode.getAttribute(TAG_Y));
		config.setWindowPosition(position);
		Dimension dimension = new Dimension();
		dimension.width = Integer.parseInt(windowNode.getAttribute(TAG_W));
		dimension.height = Integer.parseInt(windowNode.getAttribute(TAG_H));
		config.setWindowDimension(dimension);
	}

	private void readRecentFiles(Element rootNode, ConfigData config) {
		Element recentFilesNode = (Element) rootNode.getElementsByTagName(TAG_RECENT_FILES).item(0);
		NodeList recentFileNodeList = recentFilesNode.getElementsByTagName(TAG_PATH);
		for (int i = 0; i < recentFileNodeList.getLength(); i++) {
			Element recentFileNode = (Element) recentFileNodeList.item(i);
			String path = recentFileNode.getTextContent();
			config.getRecentFiles().add(path);
		}
	}

	private void readSessions(Element rootNode, ConfigData config) {
		Element sessionsNode = (Element) rootNode.getElementsByTagName(TAG_SESSIONS).item(0);
		NodeList sessionNodeList = sessionsNode.getElementsByTagName(TAG_SESSION);
		for (int i = 0; i < sessionNodeList.getLength(); i++) {
			Element sessionNode = (Element) sessionNodeList.item(i);
			Session session = new Session();
			session.setName(sessionNode.getAttribute(TAG_NAME));
			NodeList pathNodeList = sessionNode.getElementsByTagName(TAG_PATH);
			for (int x = 0; x < pathNodeList.getLength(); x++) {
				Element pathNode = (Element) pathNodeList.item(x);
				String path = pathNode.getTextContent();
				session.getFileNames().add(path);
			}

			config.getSessions().add(session);
		}
	}
}
