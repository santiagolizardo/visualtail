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
package com.santiagolizardo.visualtail.gui.dialogs.components;

import static com.santiagolizardo.visualtail.resources.languages.Translator.tr;
import static javax.swing.SpringLayout.EAST;
import static javax.swing.SpringLayout.NORTH;
import static javax.swing.SpringLayout.SOUTH;
import static javax.swing.SpringLayout.WEST;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.santiagolizardo.visualtail.beans.LogType;
import com.santiagolizardo.visualtail.beans.Rule;
import com.santiagolizardo.visualtail.gui.editors.ColorEditor;
import com.santiagolizardo.visualtail.gui.renderers.ColorExampleRenderer;
import com.santiagolizardo.visualtail.gui.renderers.ColorRenderer;
import com.santiagolizardo.visualtail.resources.languages.Translator;
import com.santiagolizardo.visualtail.config.LogTypeFileWriter;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.util.logging.Logger;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;

public class EditionPanel extends JPanel {


	private static final Logger logger = Logger.getLogger(EditionPanel.class.getName());

	private final JSpinner refreshSpinner;
	private final JLabel secondsLabel;

	private RulesTableModel modelRules;
	private JTable rulesTable;
	private final JScrollPane scrollRules;

	private final JButton addRuleButton;
	private JButton removeRuleButton;

	private LogType logType;
	private boolean logTypeLoaded;

	public EditionPanel(final JDialog mainWindow) {
		super();
		setPreferredSize(new Dimension(640, 250));

		SpinnerNumberModel spinnerModel = new SpinnerNumberModel(500, 100,
				10000, 100);
		refreshSpinner = new JSpinner(spinnerModel);
		refreshSpinner.addChangeListener((ChangeEvent) -> {
			updateSeconds();
		});

		secondsLabel = new JLabel();
		secondsLabel.setForeground(Color.GRAY);
		updateSeconds();

		logTypeLoaded = false;

		modelRules = new RulesTableModel();
		modelRules.addTableModelListener((TableModelEvent) -> {
			if (logTypeLoaded) {
				saveChanges();
			}
		});

		rulesTable = new JTable(modelRules);
		rulesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		setColumnWidths();

		rulesTable.getTableHeader().setReorderingAllowed(false);

		rulesTable.setDefaultRenderer(Color.class, new ColorRenderer());
		rulesTable.setDefaultEditor(Color.class, new ColorEditor(this));

		rulesTable.getColumnModel()
				.getColumn(5).setCellRenderer(new ColorExampleRenderer());

		rulesTable.getSelectionModel()
				.addListSelectionListener((ListSelectionEvent) -> {
					removeRuleButton.setEnabled(1 == rulesTable
							.getSelectedRowCount());
		});

		scrollRules = new JScrollPane(rulesTable);

		scrollRules.addComponentListener(
				new ComponentAdapter() {

					@Override
					public void componentResized(ComponentEvent ce
					) {
						super.componentResized(ce);

						setColumnWidths();
					}

				}
		);

		addRuleButton = new JButton(Translator.tr("Add rule"));

		addRuleButton.addActionListener((ActionEvent) -> {
			Rule rule = new Rule();
			modelRules.addRule(rule);
			saveChanges();
		});

		removeRuleButton = new JButton(Translator.tr("Remove rule"));

		removeRuleButton.setEnabled(
				false);
		removeRuleButton.addActionListener((ActionEvent) -> {
			int selectedRow = rulesTable.getSelectedRow();
			modelRules.removeRule(selectedRow);
			saveChanges();
		});

		rulesTable.setMinimumSize(
				new Dimension(500, 500));
		rulesTable.setPreferredSize(
				new Dimension(500, 500));

		placeComponents();
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);

		if (enabled) {
			cardLayout.last(this);
		} else {
			cardLayout.first(this);
		}

		Component[] components = getComponents();
		for (Component component : components) {
			component.setEnabled(enabled);
		}
	}

	private void updateSeconds() {
		DecimalFormat formatter = new DecimalFormat("#0.0");
		double _refresh = Double.parseDouble(refreshSpinner.getValue().toString());
		double _seconds = _refresh / 1000;
		secondsLabel.setText(formatter.format(_seconds)
				.concat(" " + tr("seconds")));
	}

	public void setLogType(LogType logType) {
		this.logType = logType;

		refreshSpinner.setValue(logType.getRefreshInterval());

		logTypeLoaded = false;
		modelRules.clear();
		List<Rule> rules = logType.getRules();
		for (Rule rule : rules) {
			modelRules.addRule(rule);
		}
		logTypeLoaded = true;
	}

	private CardLayout cardLayout;

	private void placeComponents() {
		JLabel lblRefresh = new JLabel(
				Translator.tr("Refresh interval in milliseconds"));
		JLabel lblRules = new JLabel(Translator.tr("Formatting rules"));

		cardLayout = new CardLayout();
		setLayout(cardLayout);

		JPanel panelA = new JPanel();
		panelA.add(
				new JLabel(Translator
						.tr("Select a log type on the left to edit it")),
				BorderLayout.CENTER);

		add(panelA, "A");

		JPanel panel = new JPanel();

		SpringLayout layout = new SpringLayout();
		panel.setLayout(layout);

		layout.putConstraint(NORTH, lblRefresh, 5, NORTH, panel);
		layout.putConstraint(NORTH, refreshSpinner, 5, SOUTH, lblRefresh);
		layout.putConstraint(NORTH, secondsLabel, 5, SOUTH, refreshSpinner);
		layout.putConstraint(NORTH, lblRules, 5, SOUTH, secondsLabel);
		layout.putConstraint(NORTH, scrollRules, 5, SOUTH, lblRules);
		layout.putConstraint(SOUTH, scrollRules, -5, NORTH, addRuleButton);
		layout.putConstraint(SOUTH, addRuleButton, -5, SOUTH, panel);
		layout.putConstraint(SOUTH, removeRuleButton, -5, SOUTH, panel);

		layout.putConstraint(WEST, lblRefresh, 5, WEST, panel);
		layout.putConstraint(WEST, refreshSpinner, 5, WEST, panel);
		layout.putConstraint(WEST, secondsLabel, 5, WEST, panel);
		layout.putConstraint(WEST, lblRules, 5, WEST, panel);
		layout.putConstraint(WEST, scrollRules, 5, WEST, panel);
		layout.putConstraint(EAST, scrollRules, -5, EAST, panel);
		layout.putConstraint(WEST, addRuleButton, 5, WEST, panel);
		layout.putConstraint(WEST, removeRuleButton, 5, EAST, addRuleButton);

		panel.add(lblRefresh);
		panel.add(refreshSpinner);
		panel.add(secondsLabel);
		panel.add(lblRules);
		panel.add(scrollRules);
		panel.add(addRuleButton);
		panel.add(removeRuleButton);

		add(panel);
	}

	public void setColumnWidths() {
		(((DefaultTableCellRenderer) rulesTable.getTableHeader().getDefaultRenderer())).setHorizontalAlignment(SwingConstants.CENTER);

		Dimension tableSize = rulesTable.getSize();
		double w = tableSize.getWidth();
		for (int i = 0; i < rulesTable.getColumnCount(); i++) {
			rulesTable.getColumnModel().getColumn(i).setWidth(modelRules.getColumnWidth(w, i));
			rulesTable.getColumnModel().getColumn(i).setPreferredWidth(modelRules.getColumnWidth(w, i));
		}
	}

	public void saveChanges() {
		short interval = Short
				.valueOf(refreshSpinner.getValue().toString());
		logType.setRefreshInterval(interval);
		logType.setRules(modelRules.getRules());

		try {
			LogTypeFileWriter logTypes = LogTypeFileWriter.getInstance();
			logTypes.write(logType);
		} catch (IOException ex) {
			logger.severe(ex.getMessage());
		}
	}
}
