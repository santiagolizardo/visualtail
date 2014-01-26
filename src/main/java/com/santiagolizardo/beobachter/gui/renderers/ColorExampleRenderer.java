package com.santiagolizardo.beobachter.gui.renderers;

import com.santiagolizardo.beobachter.resources.languages.Translator;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ColorExampleRenderer extends DefaultTableCellRenderer {

	public ColorExampleRenderer() {
		setOpaque(true);
		setText(Translator._("Testing"));
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus,
			int row, int column) {
		Color[] colors = (Color[]) value;
		setBackground(colors[0]);
		setForeground(colors[1]);
		return this;
	}
}
