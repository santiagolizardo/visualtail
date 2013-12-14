package com.santiagolizardo.beobachter.gui.adapters;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import com.santiagolizardo.beobachter.MainGUI;
import com.santiagolizardo.beobachter.gui.menu.EditPopupMenu;

public class LinesMouseAdapter extends MouseAdapter {

	private EditPopupMenu popupMenu;

	public LinesMouseAdapter(MainGUI mainGUI) {
		super();

		popupMenu = new EditPopupMenu(mainGUI);
	}

	@Override
	public void mousePressed(MouseEvent ev) {
		showPopup(ev);
	}

	@Override
	public void mouseReleased(MouseEvent ev) {
		showPopup(ev);
	}

	private void showPopup(MouseEvent ev) {
		if (ev.isPopupTrigger()) {
			popupMenu.show((JComponent) ev.getSource(), ev.getX(), ev.getY());
		}
	}
}
