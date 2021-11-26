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

import com.santiagolizardo.visualtail.beans.LogType;
import com.santiagolizardo.visualtail.beans.Rule;
import java.io.File;

import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class LogTypeFileWriter extends LogTypeFileCommon {

	private static LogTypeFileWriter singleton;

	public static final LogTypeFileWriter getInstance() {
		if (null == singleton) {
			singleton = new LogTypeFileWriter();
		}
		return singleton;
	}

	public boolean remove(LogType logType) {
		String path = getPath(logType.getName());
		File file = new File(path);
		return file.delete();
	}

	public boolean rename(LogType logType, String newName) {
		String path = getPath(logType.getName());
		File file = new File(path);

		String newPath = getPath(newName);
		File newFile = new File(newPath);

		return file.renameTo(newFile);
	}

	public void write(LogType logType) throws IOException {
		try {
			Document doc = documentBuilderFactory.newDocumentBuilder().newDocument();
			Element rootNode = doc.createElement(TAG_LOG_TYPE);
			rootNode.setAttribute(TAG_REFRESH_INTERVAL, Integer.toString(logType.getRefreshInterval()));
			doc.appendChild(rootNode);

			List<Rule> rules = logType.getRules();
			for (Rule rule : rules) {
				Element ruleNode = doc.createElement(TAG_RULE);
				ruleNode.setAttribute(TAG_PATTERN, rule.getPattern());
				ruleNode.setAttribute(TAG_REGULAR_EXPRESSION, Boolean.toString(rule.isRegularExpression()));
				ruleNode.setAttribute(TAG_IGNORE_CASE, Boolean.toString(rule.isIgnoreCase()));
				ruleNode.setAttribute(TAG_BACKGROUND_COLOR, String.valueOf(rule.getBackgroundColor().getRGB()));
				ruleNode.setAttribute(TAG_FOREGROUND_COLOR, String.valueOf(rule.getForegroundColor().getRGB()));
				rootNode.appendChild(ruleNode);
			}

			String path = getPath(logType.getName());

			TransformerFactory transformerFactory = TransformerFactory.newInstance();

			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			StreamResult file = new StreamResult(new File(path));

			DOMSource source = new DOMSource(doc);

			transformer.transform(source, file);
		} catch (ParserConfigurationException | DOMException | IllegalArgumentException | TransformerException ex) {
			logger.warning(ex.getMessage());
		}
	}
}
