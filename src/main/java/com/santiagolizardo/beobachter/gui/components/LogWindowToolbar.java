/**
 * This file is part of Beobachter, a graphical log file monitor.
 *
 * Beobachter is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Beobachter is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Beobachter. If not, see <http://www.gnu.org/licenses/>.
 */
package com.santiagolizardo.beobachter.gui.components;

import com.santiagolizardo.beobachter.beans.LogType;
import com.santiagolizardo.beobachter.engine.TailListener;
import com.santiagolizardo.beobachter.gui.dialogs.LogWindow;
import com.santiagolizardo.beobachter.gui.renderers.LogTypeListRenderer;
import com.santiagolizardo.beobachter.resources.languages.Translator;
import com.santiagolizardo.beobachter.beans.LogTypeManager;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class LogWindowToolbar extends JToolBar implements TailListener {

	private static final Logger logger = Logger.getLogger(LogWindowToolbar.class.getName());

	private LogWindow logWindow;

	private JCheckBox checkForChangesCheckBox;
	private JCheckBox scrollNewLinesCheckBox;

	private JComboBox<LogType> logTypes;
	private JSpinner spNumberDisplayerLines;

	private JButton clearButton;

	public LogWindowToolbar(final LogWindow logWindow, LogType logType) {
		setFloatable(false);

		this.logWindow = logWindow;

		spNumberDisplayerLines = new JSpinner(new SpinnerNumberModel(
				logWindow.getNumberLinesToDisplay(), 1, 9999, 1));
		spNumberDisplayerLines.setToolTipText(Translator.tr("Number of lines to display"));
		spNumberDisplayerLines.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent ev) {
				int numberDisplayedLines = (int) spNumberDisplayerLines.getValue();
				logWindow.setNumberLinesToDisplay(numberDisplayedLines);
				logWindow.trimLines();
			}
		});

		checkForChangesCheckBox = new JCheckBox(Translator.tr("Check for changes"));
		checkForChangesCheckBox.setSelected(true);
		checkForChangesCheckBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ev) {
				logWindow.checkForChanges();
			}
		});

		scrollNewLinesCheckBox = new JCheckBox(Translator.tr("Scroll to new lines"));
		scrollNewLinesCheckBox.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if (scrollNewLinesCheckBox.isSelected()) {
					logWindow.moveToNewLines();
				}
			}
		});

		clearButton = new JButton(Translator.tr("Clear buffer"));
		clearButton.setEnabled(false);
		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				logWindow.clear();
				clearButton.setEnabled(false);
			}
		});

		LogTypeManager logTypesLoader = LogTypeManager.getInstance();
		DefaultComboBoxModel<LogType> logTypesModel = new DefaultComboBoxModel<>(
				logTypesLoader.getAll().toArray(new LogType[]{}));
		logTypes = new JComboBox<>(logTypesModel);
		logTypes.setRenderer(new LogTypeListRenderer());
		logTypes.setToolTipText(Translator.tr("Log type"));
		logTypes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				LogType logType = (LogType) logTypes.getSelectedItem();
				logWindow.loadLogType(logType);
			}
		});
		logTypes.setSelectedItem(logType);

		add(checkForChangesCheckBox);
		add(scrollNewLinesCheckBox);
		add(logTypes);
		add(spNumberDisplayerLines);
		addPrintMenuItem();
		add(clearButton);
	}

	/**
	 * Tries to add the print menu if the desktop supports it.
	 */
	private void addPrintMenuItem() {
		if (!Desktop.isDesktopSupported()) {
			return;
		}

		final Desktop desktop = Desktop.getDesktop();
		if (!desktop.isSupported(Desktop.Action.PRINT)) {
			return;
		}

		JButton btnPrint = new JButton(Translator.tr("Print this file"));
		btnPrint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				try {
					desktop.print(logWindow.getFile());
				} catch (IOException e) {
					logger.warning(e.getMessage());
				}
			}
		});

		add(btnPrint);
	}

	public JCheckBox getCheckForChangesCheckBox() {
		return checkForChangesCheckBox;
	}

	public JCheckBox getScrollNewLinesCheckBox() {
		return scrollNewLinesCheckBox;
	}

	@Override
	public void onFileChanges(String line) {
		if (!clearButton.isEnabled()) {
			clearButton.setEnabled(true);
		}
	}

	public JButton getClearButton() {
		return clearButton;
	}
}
