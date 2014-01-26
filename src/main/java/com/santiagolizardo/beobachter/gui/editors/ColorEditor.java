/**
 * This file is part of Beobachter, a graphical log file monitor.
 *
 * Beobachter is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Beobachter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Beobachter.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.santiagolizardo.beobachter.gui.editors;

import com.santiagolizardo.beobachter.gui.dialogs.components.ColorChooser;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class ColorEditor extends AbstractCellEditor
		implements TableCellEditor,
		ActionListener {

	Color currentColor;
	JButton button;
	protected static final String EDIT = "edit";

	private ColorChooser chooser;

	public ColorEditor(Component parent) {
		button = new JButton();
		button.setActionCommand(EDIT);
		button.addActionListener(this);
		
		button.setBorderPainted(false);
		
		currentColor = Color.WHITE;

		chooser = new ColorChooser(parent, "saasf", currentColor, this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (EDIT.equals(e.getActionCommand())) {
			button.setBackground(currentColor);
			chooser.setVisible(true);

			fireEditingStopped();

		} else {
			Color selectedColor = chooser.getSelectedColor();
			if(null != selectedColor)
				currentColor = selectedColor;
		}
	}

	@Override
	public Object getCellEditorValue() {
		return currentColor;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table,
			Object value,
			boolean isSelected,
			int row,
			int column) {
		currentColor = (Color) value;
		return button;
	}
}
