/**
 * This file is part of VisualTail, a graphical log file monitor.
 *
 * VisualTail is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * VisualTail is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * VisualTail. If not, see <http://www.gnu.org/licenses/>.
 */
package com.santiagolizardo.visualtail.gui.dialogs;

import com.santiagolizardo.visualtail.beans.SwingLookAndFeel;
import com.santiagolizardo.visualtail.config.ConfigData;
import com.santiagolizardo.visualtail.config.ConfigFileWriter;
import com.santiagolizardo.visualtail.gui.MainWindow;
import com.santiagolizardo.visualtail.gui.renderers.LocaleRender;
import com.santiagolizardo.visualtail.gui.renderers.SwingLAFRenderer;
import com.santiagolizardo.visualtail.gui.util.SwingUtil;
import com.santiagolizardo.visualtail.resources.languages.Translator;
import com.santiagolizardo.visualtail.util.LocaleUtil;
import java.awt.Container;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.SpringLayout.Constraints;
import static javax.swing.SpringLayout.SOUTH;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class PreferencesDialog extends AbstractDialog {

    private static final long serialVersionUID = -4534774464099526206L;

    private static final Logger logger = Logger
            .getLogger(PreferencesDialog.class.getName());

    private JComboBox<SwingLookAndFeel> lookAndFeelComboBox;

    private JComboBox<String> languageComboBox;

    private JButton okButton;
    private JButton cancelButton;

    public PreferencesDialog(final MainWindow mainWindow) {
        super(mainWindow);
    }

    @Override
    protected void dialogInit() {
        super.dialogInit();

        MainWindow mainWindow = (MainWindow)getParent();
        final ConfigData configManager = mainWindow.getConfigData();

        setTitle(Translator.tr("Preferences"));
        setResizable(false);
        setModal(true);

        LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
        SwingLookAndFeel[] lafs = new SwingLookAndFeel[infos.length];
        for (int i = 0; i < infos.length; i++) {
            LookAndFeelInfo info = infos[i];
            lafs[i] = new SwingLookAndFeel(info.getName(), info.getClassName());
        }
        lookAndFeelComboBox = new JComboBox<>(lafs);
        lookAndFeelComboBox.setRenderer(new SwingLAFRenderer());
        try {
            SwingLookAndFeel look = SwingLookAndFeel.forName(configManager
                    .getWindowLookAndFeel());
            lookAndFeelComboBox.setSelectedItem(look);
        } catch (Exception e) {
            logger.warning("Unable to set the selected look&feel.");
        }

        String[] languages = LocaleUtil.getAvailableLocales();

        languageComboBox = new JComboBox<>(languages);
        languageComboBox.setSelectedItem(configManager.getLanguage());
        languageComboBox.setRenderer(new LocaleRender());

        okButton = new JButton(Translator.tr("Save"));
        okButton.addActionListener((ActionEvent) -> {
            setVisible(false);

            SwingLookAndFeel laf = ((SwingLookAndFeel) lookAndFeelComboBox
                    .getSelectedItem());
            configManager.setWindowLookAndFeel(laf.getClassName());
            SwingUtil.setLookAndFeel(laf.getClassName());
            SwingUtilities.updateComponentTreeUI(mainWindow);

            Object selectedLanguage = languageComboBox.getSelectedItem();
            if (null != selectedLanguage) {
                configManager.setLanguage(selectedLanguage.toString());
            }

            ConfigFileWriter configPersistence = new ConfigFileWriter();
            configPersistence.write(configManager);

            dispose();
        });

        cancelButton = new JButton(Translator.tr("Cancel"));
        cancelButton.addActionListener((ActionEvent) -> {
            setVisible(false);
            dispose();
        });

        defineLayout();
    }

    private void defineLayout() {
        Container container = getContentPane();
        SpringLayout layout = new SpringLayout();
        container.setLayout(layout);

        JLabel _lookAndFeel = new JLabel(Translator.tr("Look and feel"));
        JLabel _language = new JLabel(Translator.tr("Language"));

        container.add(_lookAndFeel);
        container.add(lookAndFeelComboBox);

        if (languageComboBox.getModel().getSize() > 0) {
            container.add(_language);
            container.add(languageComboBox);
        }

        container.add(okButton);
        container.add(cancelButton);

        layout.putConstraint(SpringLayout.WEST, _lookAndFeel, 5,
                SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.WEST, lookAndFeelComboBox, 5,
                SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.WEST, _language, 5,
                SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.WEST, languageComboBox, 5,
                SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.EAST, okButton, -5, SpringLayout.WEST,
                cancelButton);
        layout.putConstraint(SpringLayout.EAST, cancelButton, -5,
                SpringLayout.EAST, container);

        layout.putConstraint(SpringLayout.NORTH, _lookAndFeel, 5,
                SpringLayout.NORTH, container);
        layout.putConstraint(SpringLayout.NORTH, lookAndFeelComboBox, 5,
                SpringLayout.SOUTH, _lookAndFeel);
        layout.putConstraint(SpringLayout.NORTH, _language, 5,
                SpringLayout.SOUTH, lookAndFeelComboBox);
        layout.putConstraint(SpringLayout.NORTH, languageComboBox, 5,
                SpringLayout.SOUTH, _language);
        layout.putConstraint(SpringLayout.NORTH, okButton, 15, SpringLayout.SOUTH,
                languageComboBox);
        layout.putConstraint(SpringLayout.NORTH, cancelButton, 15,
                SpringLayout.SOUTH, languageComboBox);

        Constraints containerCons = layout.getConstraints(container);
        Constraints okCons = layout.getConstraints(okButton);

        containerCons.setWidth(Spring.constant(270));
        containerCons.setHeight(Spring.sum(Spring.constant(5),
                okCons.getConstraint(SOUTH)));

        getRootPane().setDefaultButton(okButton);

        pack();
        setLocationRelativeTo(getOwner());
    }
}
