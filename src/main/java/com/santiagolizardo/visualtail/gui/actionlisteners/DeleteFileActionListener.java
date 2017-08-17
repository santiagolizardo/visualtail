package com.santiagolizardo.visualtail.gui.actionlisteners;

import com.santiagolizardo.visualtail.gui.MainWindow;
import com.santiagolizardo.visualtail.gui.dialogs.LogWindow;
import com.santiagolizardo.visualtail.resources.languages.Translator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class DeleteFileActionListener implements ActionListener {

    private MainWindow mainWindow;

    public DeleteFileActionListener(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LogWindow logWindow = mainWindow.getDesktop().getSelectedWindow();
        File file = logWindow.getFile();
        final String confirmationMessage = Translator.tr(String.format("Do you really want to delete the file \"%s\"?", file.getAbsolutePath()));
        final String dialogTitle = Translator.tr("Delete operation");
        if(JOptionPane.showConfirmDialog(mainWindow, confirmationMessage, dialogTitle, JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }

        if(!file.delete()) {
            JOptionPane.showMessageDialog(mainWindow, Translator.tr("File could not be deleted."), dialogTitle, JOptionPane.WARNING_MESSAGE);
        }

        mainWindow.getDesktop().remove(logWindow);
        mainWindow.getDesktop().updateUI();

        ((JMenuItem)e.getSource()).setEnabled(false);
    }
}
