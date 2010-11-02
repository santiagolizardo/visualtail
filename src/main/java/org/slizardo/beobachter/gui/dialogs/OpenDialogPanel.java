package org.slizardo.beobachter.gui.dialogs;


import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.slizardo.beobachter.beans.LogType;
import org.slizardo.beobachter.gui.renderers.LogTypeListRenderer;
import org.slizardo.beobachter.resources.languages.Translator;
import org.slizardo.beobachter.util.ArraysUtil;

public class OpenDialogPanel extends JPanel implements PropertyChangeListener {

	private static final long serialVersionUID = 1625880603241292262L;

	private DefaultComboBoxModel logTypesModel;
	private JComboBox logTypes;

	public OpenDialogPanel() {
		logTypesModel = new DefaultComboBoxModel();
		logTypes = new JComboBox(logTypesModel);
		logTypes.setRenderer(new LogTypeListRenderer());

		Iterator<LogType> logTypes = ArraysUtil.arrayLogTypes().iterator();
		while (logTypes.hasNext()) {
			logTypesModel.addElement(logTypes.next());
		}

		setBorder(BorderFactory.createTitledBorder(Translator.t("Options")));
		
		placeComponents();
	}

	public LogType getSelectedLogType() {
		return (LogType) logTypes.getSelectedItem();
	}

	private void placeComponents() {
		JLabel lblLogType = new JLabel("Log type");
		lblLogType.setFont(lblLogType.getFont().deriveFont(Font.BOLD));

		add(lblLogType);
		add(logTypes);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();

		if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(prop)) {
			setEnabled(false);
		} else if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop)) {
			setEnabled(true);
		}
	}
}
