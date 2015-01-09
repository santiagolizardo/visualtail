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
package com.santiagolizardo.visualtail.gui.dialogs.components;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.santiagolizardo.visualtail.beans.Rule;
import static com.santiagolizardo.visualtail.resources.languages.Translator.tr;

public class RulesTableModel extends AbstractTableModel {

	private static final long serialVersionUID = -3824360112995729445L;

	private String columns[] = new String[]{
		tr("Pattern"),
		tr("Regular expression"),
		tr("Ignore case"),
		tr("Background color"),
		tr("Foreground color"),
		tr("Example")
	};

	private List<Rule> rules;

	public RulesTableModel() {
		rules = new ArrayList<>();
	}

	@Override
	public String getColumnName(int col) {
		String name = columns[col];
		if (name.contains(" ")) {
			String[] lines = name.split(" ");
			return String.format("<html>%s<br />%s</html>", lines[0], lines[1]);
		} else {
			return String.format("<html><br />%s</html>", name);
		}

	}

	@Override
	public Class getColumnClass(int col) {
		if (rules.isEmpty()) {
			return null;
		}

		return getValueAt(0, col).getClass();
	}

	@Override
	public int getColumnCount() {
		return columns.length;
	}

	@Override
	public int getRowCount() {
		return rules.size();
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return (col != 5);
	}

	public int getColumnWidth(double tableWidth, int col) {
		switch (col) {
			case 0:
				return (int) (tableWidth * .3);
			case 1:
				return (int) (tableWidth * .15);
			case 2:
				return (int) (tableWidth * .15);
			case 3:
				return (int) (tableWidth * .15);
			case 4:
				return (int) (tableWidth * .15);
			case 5:
				return (int) (tableWidth * .1);
			default:
				return 0;
		}
	}

	@Override
	public Object getValueAt(int row, int col) {
		Rule rule = (Rule) rules.get(row);
		switch (col) {
			case 0:
				return rule.getPattern();
			case 1:
				return rule.isRegularExpression();
			case 2:
				return rule.isIgnoreCase();
			case 3:
				return rule.getBackgroundColor();
			case 4:
				return rule.getForegroundColor();
			case 5:
				return new Color[]{
					rule.getBackgroundColor(),
					rule.getForegroundColor()
				};
			default:
				return "?";
		}
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		Rule rule = rules.get(row);
		switch (col) {
			case 0:
				rule.setPattern((String) value);
				break;
			case 1:
				rule.setRegularExpression((boolean) value);
				break;
			case 2:
				rule.setIgnoreCase((boolean) value);
				break;
			case 3:
				rule.setBackgroundColor((Color) value);
				fireTableCellUpdated(row, 5);
				break;
			case 4:
				rule.setForegroundColor((Color) value);
				fireTableCellUpdated(row, 5);
				break;
			default:
				return;
		}
		fireTableCellUpdated(row, col);
	}

	public void addRule(Rule rule) {
		rules.add(rule);
		fireTableRowsInserted(rules.size() - 1, rules.size() - 1);
	}

	public void removeRule(int row) {
		rules.remove(row);
		fireTableRowsDeleted(row, row);
	}

	public List<Rule> getRules() {
		return rules;
	}

	public void clear() {
		rules.clear();
		fireTableDataChanged();
	}
}
