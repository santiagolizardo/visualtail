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
