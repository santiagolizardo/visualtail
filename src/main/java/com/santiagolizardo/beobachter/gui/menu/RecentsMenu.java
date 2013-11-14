package com.santiagolizardo.beobachter.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.santiagolizardo.beobachter.MainGUI;
import com.santiagolizardo.beobachter.beans.LogType;
import com.santiagolizardo.beobachter.config.ConfigData;
import com.santiagolizardo.beobachter.engine.Controller;
import com.santiagolizardo.beobachter.gui.util.DialogFactory;
import com.santiagolizardo.beobachter.gui.util.EmptyIcon;
import com.santiagolizardo.beobachter.gui.util.FileUtil;
import com.santiagolizardo.beobachter.resources.languages.Translator;
import com.santiagolizardo.beobachter.util.ArraysUtil;

public class RecentsMenu extends JMenu {

	private static final long serialVersionUID = 1L;

	public RecentsMenu(ConfigData configData) {
		super(Translator._("Open_recents"));

		setIcon(EmptyIcon.SIZE_16);

		JMenuItem cleanRecents = new JMenuItem(Translator._("Clean_recents"));
		cleanRecents.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				ArraysUtil.recents = new Vector<String>();
				int count = getMenuComponentCount();
				for (int i = count - 1; i > 1; i--) {
					remove(i);
				}
			}
		});

		add(cleanRecents);
		addSeparator();

		for (String fileName : configData.getRecentFiles()) {
			addRecent(fileName);
		}
	}

	public void addRecent(String fileName) {
		if (!ArraysUtil.recents.contains(fileName)) {
			JMenuItem item = new JMenuItem(fileName);
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					JMenuItem item = (JMenuItem) event.getSource();
					String filePath = item.getText();
					File file = new File(filePath);

					try {
						FileUtil.tryReading(file);
					} catch (Exception e) {
						DialogFactory.showErrorMessage(MainGUI.instance,
								e.getMessage());
						return;
					}

					Controller.openFile(filePath, new LogType("Default"));
				}
			});
			ArraysUtil.recents.add(fileName);
			add(item);
		}
	}
}
