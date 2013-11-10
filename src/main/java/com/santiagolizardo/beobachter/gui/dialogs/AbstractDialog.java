package com.santiagolizardo.beobachter.gui.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

public abstract class AbstractDialog extends JDialog {

	private static final long serialVersionUID = -8559961506669731891L;

	protected JFrame parentFrame;

	public AbstractDialog(JFrame parentFrame) {
		super();

		this.parentFrame = parentFrame;
	}

	@Override
	protected JRootPane createRootPane() {
		JRootPane rootPane = super.createRootPane();

		rootPane.registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				dispose();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

		return rootPane;
	}
}
