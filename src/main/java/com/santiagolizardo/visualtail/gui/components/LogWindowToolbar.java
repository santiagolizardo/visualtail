/**
 * This file is part of VisualTail, a graphical log file monitor.
 *
 * VisualTail is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VisualTail is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VisualTail.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.santiagolizardo.visualtail.gui.components;

import com.santiagolizardo.visualtail.beans.LogType;
import com.santiagolizardo.visualtail.engine.TailListener;
import com.santiagolizardo.visualtail.gui.dialogs.LogWindow;
import com.santiagolizardo.visualtail.gui.renderers.LogTypeListRenderer;
import com.santiagolizardo.visualtail.resources.languages.Translator;
import com.santiagolizardo.visualtail.beans.LogTypeManager;
import com.santiagolizardo.visualtail.gui.components.buttons.ClearWindowButton;
import com.santiagolizardo.visualtail.gui.components.buttons.PrintFileButton;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class LogWindowToolbar extends JToolBar implements TailListener {

	private static final Logger logger = Logger.getLogger(LogWindowToolbar.class.getName());

	private LogWindow logWindow;

	private JToggleButton checkForChangesCheckBox;
	private JToggleButton scrollNewLinesCheckBox;

	private JComboBox<LogType> logTypes;
	private JSpinner numberLinesToDisplaySpinner;

	private JButton showPreviousLinesButton;
	private JButton clearButton;

	public LogWindowToolbar(final LogWindow logWindow, LogType logType) {
		setFloatable(false);

		this.logWindow = logWindow;
		
		setMaximumSize(new Dimension(Integer.MAX_VALUE, getPreferredSize().height));

		numberLinesToDisplaySpinner = new JSpinner(new SpinnerNumberModel(
				logWindow.getNumberLinesToDisplay(), 1, 9999, 1));
		numberLinesToDisplaySpinner.setToolTipText(Translator.tr("Number of lines to display"));
		numberLinesToDisplaySpinner.setMaximumSize(numberLinesToDisplaySpinner.getPreferredSize());
		numberLinesToDisplaySpinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent ev) {
				int numberDisplayedLines = (int) numberLinesToDisplaySpinner.getValue();
				logWindow.setNumberLinesToDisplay(numberDisplayedLines);
				logWindow.trimLines();
			}
		});

		checkForChangesCheckBox = new JToggleButton(Translator.tr("Check for changes"));
		checkForChangesCheckBox.setSelected(true);
		checkForChangesCheckBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ev) {
				logWindow.checkForChanges();
			}
		});

		scrollNewLinesCheckBox = new JToggleButton(Translator.tr("Scroll to new lines"));
		scrollNewLinesCheckBox.setSelected(true);
		scrollNewLinesCheckBox.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if (scrollNewLinesCheckBox.isSelected()) {
					logWindow.moveToNewLines();
				}
			}
		});

		clearButton = new ClearWindowButton(logWindow);

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

		showPreviousLinesButton = new JButton(Translator.tr("Show previous lines"));
		showPreviousLinesButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int limit = 100;
				Integer[] options = new Integer[limit];
				for (int i = 0; i < limit; i++) {
					options[i] = i;
				}
				Object input = JOptionPane.showInternalInputDialog(logWindow, Translator.tr("Enter the number of previous lines to display:"), Translator.tr("Input"), JOptionPane.OK_CANCEL_OPTION, null, (Object[])options, options[5]);
				if (null == input) {
					return;
				}

				try {
					int count = (Integer) input;
					clearButton.setEnabled(logWindow.loadPreviousLines(count) > 0);
				} catch (NumberFormatException nfe) {
					logger.warning(nfe.getMessage());
					JOptionPane.showMessageDialog(logWindow, Translator.tr("Invalid number of lines entered."), Translator.tr("Error"), JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		add(checkForChangesCheckBox);
		add(scrollNewLinesCheckBox);
		add(logTypes);
		add(numberLinesToDisplaySpinner);
		add(showPreviousLinesButton);
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

		JButton printButton = new PrintFileButton(logWindow);
		add(printButton);
	}

	public JToggleButton getCheckForChangesCheckBox() {
		return checkForChangesCheckBox;
	}

	public JToggleButton getScrollNewLinesCheckBox() {
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
