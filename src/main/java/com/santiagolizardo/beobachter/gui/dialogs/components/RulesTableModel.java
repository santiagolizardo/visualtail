/**
 * Beobachter is a logs watcher for the desktop. (a.k.a. full-featured tail)
 * Copyright (C) 2011 Santiago Lizardo (http://www.santiagolizardo.com)
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

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.santiagolizardo.beobachter.beans.Rule;
import com.santiagolizardo.beobachter.resources.languages.Translator;

public class RulesTableModel extends AbstractTableModel {

	private static final long serialVersionUID = -3824360112995729445L;

	private String columns[] = new String[] { Translator._("Pattern"),
			Translator._("Colors"), Translator._("Ignore_case") };

	private List<Rule> rules;

	public RulesTableModel() {
		rules = new ArrayList<Rule>();
	}

	public String getColumnName(int index) {
		return columns[index];
	}

	public int getColumnCount() {
		return columns.length;
	}

	public Object getValueAt(int row, int column) {
		Rule rule = (Rule) rules.get(row);
		switch (column) {
		case 0:
			return rule.getPattern();
		case 1:
			return new Color[] { rule.getBackgroundColor(),
					rule.getForegroundColor() };
		case 2:
			return new Boolean(rule.isIgnoreCase());
		default:
			return null;
		}
	}

	public int getRowCount() {
		return rules.size();
	}

	public void addRule(Object o) {
		if (o instanceof Rule) {
			Rule rule = (Rule) o;
			rules.add(rule);
			fireTableRowsInserted(rules.size() - 1, rules.size() - 1);
		}
	}

	public void removeRule(int index) {
		rules.remove(index);
		fireTableRowsDeleted(index, index);
	}

	public void clear() {
		rules.clear();
		fireTableDataChanged();
	}

	public List<Rule> getRules() {
		return rules;
	}
}
