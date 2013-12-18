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
package com.santiagolizardo.beobachter.gui.dialogs.components;

import static com.santiagolizardo.beobachter.resources.languages.Translator._;
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
import javax.swing.table.TableCellRenderer;

import com.santiagolizardo.beobachter.beans.LogType;
import com.santiagolizardo.beobachter.beans.Rule;
import com.santiagolizardo.beobachter.config.EntitiesConfiguration;
import com.santiagolizardo.beobachter.gui.dialogs.RuleDialog;
import com.santiagolizardo.beobachter.resources.languages.Translator;

public class EditionPanel extends JPanel {

	private static final long serialVersionUID = 6788587076275461146L;

	private LogType logType;

	private JSpinner spnRefresh;
	private JLabel lblSeconds;

	private RulesTableModel modelRules;
	private JTable tblRules;
	private JScrollPane scrollRules;

	private JButton btnAddRule;
	private JButton btnRemoveRule;

	private JButton btnApply;

	public EditionPanel(final JDialog mainGUI) {
		setPreferredSize(new Dimension(400, 300));

		SpinnerNumberModel spinnerModel = new SpinnerNumberModel(500, 100,
				10000, 100);
		spnRefresh = new JSpinner(spinnerModel);
		spnRefresh.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				btnApply.setEnabled(true);
				updateSeconds();
			}
		});

		lblSeconds = new JLabel();
		lblSeconds.setForeground(Color.GRAY);
		updateSeconds();

		modelRules = new RulesTableModel();
		tblRules = new JTable(modelRules);
		tblRules.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		TableCellRenderer renderer = new TableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				Color[] colors = (Color[]) value;
				JLabel label = new JLabel(Translator._("Example"));
				label.setOpaque(true);
				label.setBackground(colors[0]);
				label.setForeground(colors[1]);
				return label;
			}
		};
		tblRules.getColumnModel().getColumn(1).setCellRenderer(renderer);
		tblRules.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {

					@Override
					public void valueChanged(ListSelectionEvent ev) {
						btnRemoveRule.setEnabled(1 == tblRules
								.getSelectedRowCount());
					}
				});

		scrollRules = new JScrollPane(tblRules);

		btnAddRule = new JButton(Translator._("Add rule"));
		btnAddRule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				btnApply.setEnabled(true);
				RuleDialog dialog = new RuleDialog(mainGUI, modelRules);
				dialog.setVisible(true);
			};
		});

		btnRemoveRule = new JButton(Translator._("Remove rule"));
		btnRemoveRule.setEnabled(false);
		btnRemoveRule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				btnApply.setEnabled(true);
				int selectedRow = tblRules.getSelectedRow();
				modelRules.removeRule(selectedRow);
			}
		});

		btnApply = new JButton(Translator._("Apply changes"));
		btnApply.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				short interval = Short
						.valueOf(spnRefresh.getValue().toString()).shortValue();
				logType.setRefreshInterval(interval);
				logType.setRules(modelRules.getRules());

				try {
					EntitiesConfiguration.saveToFile(logType);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		scrollRules.setPreferredSize(new Dimension(300, 130));

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
				.concat(" " + _("seconds")));
	}

	public void setLogType(LogType logType) {
		btnApply.setEnabled(false);

		this.logType = logType;

		spnRefresh.setValue(logType.getRefreshInterval());

		modelRules.clear();
		List<Rule> rules = logType.getRules();
		for (Rule rule : rules) {
			modelRules.addRule(rule);
		}
	}

	private CardLayout cardLayout;

	private void placeComponents() {
		JLabel lblRefresh = new JLabel(
				Translator._("Refresh interval in milliseconds"));
		JLabel lblRules = new JLabel(Translator._("Formatting rules"));

		cardLayout = new CardLayout();
		setLayout(cardLayout);

		JPanel panelA = new JPanel();
		panelA.add(
				new JLabel(Translator
						._("Select a log type on the left to edit it")),
				BorderLayout.CENTER);

		add(panelA, "A");

		JPanel panel = new JPanel();

		SpringLayout layout = new SpringLayout();
		panel.setLayout(layout);

		layout.putConstraint(NORTH, lblRefresh, 5, NORTH, this);
		layout.putConstraint(NORTH, spnRefresh, 5, SOUTH, lblRefresh);
		layout.putConstraint(NORTH, lblSeconds, 5, SOUTH, spnRefresh);
		layout.putConstraint(NORTH, lblRules, 5, SOUTH, lblSeconds);
		layout.putConstraint(NORTH, scrollRules, 5, SOUTH, lblRules);
		layout.putConstraint(NORTH, btnAddRule, 5, SOUTH, scrollRules);
		layout.putConstraint(NORTH, btnRemoveRule, 5, SOUTH, scrollRules);
		layout.putConstraint(NORTH, btnApply, 5, SOUTH, btnAddRule);

		layout.putConstraint(WEST, lblRefresh, 5, WEST, this);
		layout.putConstraint(WEST, spnRefresh, 5, WEST, this);
		layout.putConstraint(WEST, lblSeconds, 5, WEST, this);
		layout.putConstraint(WEST, lblRules, 5, WEST, this);
		layout.putConstraint(WEST, scrollRules, 5, WEST, this);
		layout.putConstraint(EAST, scrollRules, -5, EAST, this);
		layout.putConstraint(WEST, btnAddRule, 5, WEST, this);
		layout.putConstraint(WEST, btnRemoveRule, 5, EAST, btnAddRule);
		layout.putConstraint(EAST, btnApply, -5, EAST, this);

		panel.add(lblRefresh);
		panel.add(spnRefresh);
		panel.add(lblSeconds);
		panel.add(lblRules);
		panel.add(scrollRules);
		panel.add(btnAddRule);
		panel.add(btnRemoveRule);
		panel.add(btnApply);

		add(panel, "B");
	}
}
