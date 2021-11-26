/*
  This file is part of VisualTail, a graphical log file monitor.

  VisualTail is free software: you can redistribute it and/or modify it under
  the terms of the GNU General Public License as published by the Free Software
  Foundation, either version 3 of the License, or (at your option) any later
  version.

  VisualTail is distributed in the hope that it will be useful, but WITHOUT ANY
  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
  A PARTICULAR PURPOSE. See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with
  VisualTail. If not, see <http://www.gnu.org/licenses/>.
 */
package com.santiagolizardo.visualtail.config;

import com.santiagolizardo.visualtail.beans.Session;
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ConfigFileWriter extends ConfigFileCommon {

	public void write(ConfigData config) {
		prepareConfigFolder();

		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

			Document doc = documentBuilderFactory.newDocumentBuilder().newDocument();
			Element rootNode = doc.createElement(TAG_CONFIG_DATA);
			rootNode.setAttribute(TAG_LANGUAGE, config.getLanguage());
			doc.appendChild(rootNode);

			writeWindow(doc, rootNode, config);
			writeFont(doc, rootNode, config);
			writeRecentFiles(doc, rootNode, config);
			writeSessions(doc, rootNode, config);

			DOMSource source = new DOMSource(doc);
			StreamResult file = new StreamResult(new File(CONFIG_FILE_PATH));
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, file);
			
		} catch (ParserConfigurationException | DOMException | IllegalArgumentException | TransformerException e) {
			logger.warning(e.getMessage());
		}
	}

	private void prepareConfigFolder() {
		File file = new File(CONFIG_FILE_PATH);
		File dir = file.getParentFile();
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	private void writeWindow(Document doc, Element rootNode, ConfigData config) {
		Element windowNode = doc.createElement(TAG_WINDOW);
		windowNode.setAttribute(TAG_LOOK_AND_FEEL, config.getWindowLookAndFeel());
		windowNode.setAttribute(TAG_X, config.getWindowPosition().x + "");
		windowNode.setAttribute(TAG_Y, config.getWindowPosition().y + "");
		windowNode.setAttribute(TAG_W, config.getWindowDimension().width + "");
		windowNode.setAttribute(TAG_H, config.getWindowDimension().height + "");
		rootNode.appendChild(windowNode);
	}

	private void writeFont(Document doc, Element rootNode, ConfigData config) {
		Element fontNode = doc.createElement(TAG_FONT);
		fontNode.setAttribute(TAG_FAMILY, config.getFont().getFamily());
		fontNode.setAttribute(TAG_SIZE, config.getFont().getSize() + "");
		rootNode.appendChild(fontNode);
	}

	private void writeRecentFiles(Document doc, Element rootNode, ConfigData config) {
		Element recentFilesNode = doc.createElement(TAG_RECENT_FILES);
		for (String recentFile : config.getRecentFiles()) {
			Element recentFileNode = doc.createElement(TAG_PATH);
			recentFileNode.appendChild(doc.createTextNode(recentFile));
			recentFilesNode.appendChild(recentFileNode);
		}
		rootNode.appendChild(recentFilesNode);
	}

	private void writeSessions(Document doc, Element rootNode, ConfigData config) {
		Element sessionsNode = doc.createElement(TAG_SESSIONS);
		for (Session session : config.getSessions()) {
			Element sessionNode = doc.createElement(TAG_SESSION);
			sessionNode.setAttribute(TAG_NAME, session.getName());
			for (String path : session.getFileNames()) {
				Element pathNode = doc.createElement(TAG_PATH);
				pathNode.appendChild(doc.createTextNode(path));
				sessionNode.appendChild(pathNode);
			}
			sessionsNode.appendChild(sessionNode);
		}
		rootNode.appendChild(sessionsNode);
	}
}
