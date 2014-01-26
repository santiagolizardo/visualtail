package com.santiagolizardo.beobachter.gui.renderers;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ColorRenderer extends DefaultTableCellRenderer {

	public ColorRenderer() {
		setOpaque(true);
	}

	@Override
	public Component getTableCellRendererComponent(
			JTable table, Object value,
			boolean isSelected, boolean hasFocus,
			int row, int column) {
		Color color = (Color) value;
		setBackground(color);
		return this;
	}
}
