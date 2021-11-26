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
package com.santiagolizardo.visualtail.gui.editors;

import com.santiagolizardo.visualtail.gui.dialogs.components.ColorChooser;
import static com.santiagolizardo.visualtail.resources.languages.Translator.tr;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class ColorEditor extends AbstractCellEditor
		implements TableCellEditor,
		ActionListener {

	private static final String EDIT = "edit";

	private Color currentColor;
	private final JButton button;

	private final ColorChooser chooser;

	public ColorEditor(Component parent) {
		button = new JButton();
		button.setActionCommand(EDIT);
		button.addActionListener(this);
		
		button.setBorderPainted(false);
		
		currentColor = Color.WHITE;

		chooser = new ColorChooser(parent, tr("Pick a color"), currentColor, this);
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
