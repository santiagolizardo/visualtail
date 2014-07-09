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
package com.santiagolizardo.beobachter.gui.dialogs.components;

import static com.santiagolizardo.beobachter.resources.languages.Translator.tr;
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

import com.santiagolizardo.beobachter.beans.LogType;
import com.santiagolizardo.beobachter.beans.Rule;
import com.santiagolizardo.beobachter.gui.editors.ColorEditor;
import com.santiagolizardo.beobachter.gui.renderers.ColorExampleRenderer;
import com.santiagolizardo.beobachter.gui.renderers.ColorRenderer;
import com.santiagolizardo.beobachter.resources.languages.Translator;
import com.santiagolizardo.beobachter.beans.LogTypeManager;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.util.logging.Logger;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;

public class EditionPanel extends JPanel {

	private static final long serialVersionUID = 6788587076275461146L;

	private static final Logger logger = Logger.getLogger(EditionPanel.class.getName());

	private JSpinner spnRefresh;
	private JLabel lblSeconds;

	private RulesTableModel modelRules;
	private JTable tblRules;
	private JScrollPane scrollRules;

	private JButton btnAddRule;
	private JButton btnRemoveRule;

	private LogType logType;
	private boolean logTypeLoaded;

	public EditionPanel(final JDialog mainWindow) {
		super();
		setPreferredSize(new Dimension(640, 250));

		SpinnerNumberModel spinnerModel = new SpinnerNumberModel(500, 100,
				10000, 100);
		spnRefresh = new JSpinner(spinnerModel);
		spnRefresh.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ev) {
				updateSeconds();
			}
		});

		lblSeconds = new JLabel();
		lblSeconds.setForeground(Color.GRAY);
		updateSeconds();

		logTypeLoaded = false;

		modelRules = new RulesTableModel();
		modelRules.addTableModelListener(new TableModelListener() {

			@Override
			public void tableChanged(TableModelEvent tme) {
				if (logTypeLoaded) {
					saveChanges();
				}
			}
		});

		tblRules = new JTable(modelRules);
		tblRules.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		setColumnWidths();

		tblRules.getTableHeader().setReorderingAllowed(false);

		tblRules.setDefaultRenderer(Color.class, new ColorRenderer());
		tblRules.setDefaultEditor(Color.class, new ColorEditor(this));

		tblRules.getColumnModel()
				.getColumn(5).setCellRenderer(new ColorExampleRenderer());

		tblRules.getSelectionModel()
				.addListSelectionListener(
						new ListSelectionListener() {

							@Override
							public void valueChanged(ListSelectionEvent ev
							) {
								btnRemoveRule.setEnabled(1 == tblRules
										.getSelectedRowCount());
							}
						}
				);

		scrollRules = new JScrollPane(tblRules);

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

		btnAddRule = new JButton(Translator.tr("Add rule"));

		btnAddRule.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent ev
					) {
						Rule rule = new Rule();
						modelRules.addRule(rule);
						saveChanges();
					}
				;
		}
	);

		btnRemoveRule = new JButton(Translator.tr("Remove rule"));

		btnRemoveRule.setEnabled(
				false);
		btnRemoveRule.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent event
					) {
						int selectedRow = tblRules.getSelectedRow();
						modelRules.removeRule(selectedRow);
						saveChanges();
					}
				}
		);

		tblRules.setMinimumSize(
				new Dimension(500, 500));
		tblRules.setPreferredSize(
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
		double _refresh = Double.parseDouble(spnRefresh.getValue().toString());
		double _seconds = _refresh / 1000;
		lblSeconds.setText(formatter.format(_seconds)
				.concat(" " + tr("seconds")));
	}

	public void setLogType(LogType logType) {
		this.logType = logType;

		spnRefresh.setValue(logType.getRefreshInterval());

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
		layout.putConstraint(NORTH, spnRefresh, 5, SOUTH, lblRefresh);
		layout.putConstraint(NORTH, lblSeconds, 5, SOUTH, spnRefresh);
		layout.putConstraint(NORTH, lblRules, 5, SOUTH, lblSeconds);
		layout.putConstraint(NORTH, scrollRules, 5, SOUTH, lblRules);
		layout.putConstraint(SOUTH, scrollRules, -5, NORTH, btnAddRule);
		layout.putConstraint(SOUTH, btnAddRule, -5, SOUTH, panel);
		layout.putConstraint(SOUTH, btnRemoveRule, -5, SOUTH, panel);

		layout.putConstraint(WEST, lblRefresh, 5, WEST, panel);
		layout.putConstraint(WEST, spnRefresh, 5, WEST, panel);
		layout.putConstraint(WEST, lblSeconds, 5, WEST, panel);
		layout.putConstraint(WEST, lblRules, 5, WEST, panel);
		layout.putConstraint(WEST, scrollRules, 5, WEST, panel);
		layout.putConstraint(EAST, scrollRules, -5, EAST, panel);
		layout.putConstraint(WEST, btnAddRule, 5, WEST, panel);
		layout.putConstraint(WEST, btnRemoveRule, 5, EAST, btnAddRule);

		panel.add(lblRefresh);
		panel.add(spnRefresh);
		panel.add(lblSeconds);
		panel.add(lblRules);
		panel.add(scrollRules);
		panel.add(btnAddRule);
		panel.add(btnRemoveRule);

		add(panel);
	}

	public void setColumnWidths() {
		(((DefaultTableCellRenderer) tblRules.getTableHeader().getDefaultRenderer())).setHorizontalAlignment(SwingConstants.CENTER);

		Dimension tableSize = tblRules.getSize();
		double w = tableSize.getWidth();
		for (int i = 0; i < tblRules.getColumnCount(); i++) {
			tblRules.getColumnModel().getColumn(i).setWidth(modelRules.getColumnWidth(w, i));
			tblRules.getColumnModel().getColumn(i).setPreferredWidth(modelRules.getColumnWidth(w, i));
		}
	}

	public void saveChanges() {
		short interval = Short
				.valueOf(spnRefresh.getValue().toString()).shortValue();
		logType.setRefreshInterval(interval);
		logType.setRules(modelRules.getRules());

		try {
			LogTypeManager logTypes = LogTypeManager.getInstance();
			logTypes.saveToFile(logType);
		} catch (IOException ex) {
			logger.severe(ex.getMessage());
		}
	}
}
