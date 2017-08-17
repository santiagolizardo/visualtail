/**
 * This file is part of VisualTail, a graphical log file monitor.
 *
 * VisualTail is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VisualTail is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VisualTail.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.santiagolizardo.visualtail.gui.menu;

import static com.santiagolizardo.visualtail.resources.languages.Translator.tr;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import com.santiagolizardo.visualtail.gui.MainWindow;
import com.santiagolizardo.visualtail.beans.LogType;
import com.santiagolizardo.visualtail.beans.Session;
import com.santiagolizardo.visualtail.gui.actionlisteners.DeleteFileActionListener;
import com.santiagolizardo.visualtail.gui.actions.ExitAction;
import com.santiagolizardo.visualtail.gui.dialogs.LogWindow;
import com.santiagolizardo.visualtail.gui.dialogs.SessionsDialog;
import com.santiagolizardo.visualtail.gui.util.DialogFactory;
import com.santiagolizardo.visualtail.util.FileUtil;
import com.santiagolizardo.visualtail.resources.images.IconFactory;
import static com.santiagolizardo.visualtail.resources.languages.Translator.trn;
import java.awt.Container;
import java.awt.Toolkit;
import java.util.logging.Logger;

public class FileMenu extends JMenu implements ActionListener {

    private static final long serialVersionUID = -9095266179967845006L;

    private static final Logger logger = Logger.getLogger(FileMenu.class.getName());

    private final MainWindow mainWindow;

    private final RecentsMenu recentsMenu;

    private final JMenuItem openMenuItem;
    private final JMenuItem loadSessionMenuItem;
    private final JMenuItem saveSessionMenuItem;
    private final JMenuItem deleteFileMenuItem;

    public FileMenu(final MainWindow mainWindow) {
        setText(tr("File"));
        setMnemonic(KeyEvent.VK_F);

        this.mainWindow = mainWindow;

        openMenuItem = new JMenuItem(tr("Open..."));
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        openMenuItem.addActionListener(this);

        recentsMenu = new RecentsMenu(mainWindow);
        recentsMenu.setEnabled(!mainWindow.getConfigData().getRecentFiles().isEmpty());
        recentsMenu.refresh();

        deleteFileMenuItem = new JMenuItem(tr("Delete file"), IconFactory.getImage("page_delete.png"));
        deleteFileMenuItem.setEnabled(false);
        deleteFileMenuItem.addActionListener(new DeleteFileActionListener(mainWindow));

        loadSessionMenuItem = new JMenuItem(tr("Manage sessions..."));
        loadSessionMenuItem.addActionListener(this);

        saveSessionMenuItem = new JMenuItem(tr("Save current session"));
        saveSessionMenuItem.setIcon(IconFactory.getImage("disk.png"));
        saveSessionMenuItem.setEnabled(false);
        saveSessionMenuItem.addActionListener(this);

        JMenuItem exit = new JMenuItem(new ExitAction(mainWindow));
        exit.setIcon(IconFactory.getImage("exit.png"));

        add(openMenuItem);
        add(recentsMenu);
        addSeparator();
        add(deleteFileMenuItem);
        addSeparator();
        add(loadSessionMenuItem);
        add(saveSessionMenuItem);
        addSeparator();
        add(exit);
    }

    public JMenuItem getSaveSessionMenuItem() {
        return saveSessionMenuItem;
    }

    public JMenuItem getDeleteFileMenuItem() {
        return deleteFileMenuItem;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (openMenuItem == ev.getSource()) {
            File lastSelected = new File(mainWindow.getConfigData().getLastPath());

            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(lastSelected);
            chooser.setMultiSelectionEnabled(true);
            int resp = chooser.showOpenDialog(mainWindow);
            if (resp == JFileChooser.APPROVE_OPTION) {
                File[] files = chooser.getSelectedFiles();
                LogType logType = new LogType("Default");

                List<File> unreadableFiles = new ArrayList<>();

                for (File file : files) {

                    try {
                        FileUtil.tryReading(file);
                    } catch (Exception ex) {
                        unreadableFiles.add(file);
                        continue;
                    }

                    mainWindow.getActionFactory().getOpenAction().openFile(file.getAbsolutePath(),
                            logType);

                    mainWindow.getConfigData().getRecentFiles().remove(file.getAbsolutePath());
                    mainWindow.getConfigData().getRecentFiles().add(file.getAbsolutePath());
                    recentsMenu.refresh();
                    recentsMenu.setEnabled(true);
                }

                mainWindow.getDesktop().setWindowsOnTileHorizontal();

                int numberOfUnreadableFiles = unreadableFiles.size();
                if (numberOfUnreadableFiles > 0) {
                    String text = trn("The file could not be opened for reading:", "These files could not be opened for reading:", numberOfUnreadableFiles);
                    StringBuilder message = new StringBuilder();
                    message.append(text.concat("\n"));
                    unreadableFiles.forEach((file) -> {
                        message.append("    - ")
                                .append(file.getAbsolutePath())
                                .append("\n");

                    });
                    DialogFactory.showErrorMessage(mainWindow,
                            message.toString());
                }
            }
        } else if (loadSessionMenuItem == ev.getSource()) {
            SessionsDialog dialog = new SessionsDialog(mainWindow, recentsMenu);
            dialog.setVisible(true);
        } else if (saveSessionMenuItem
                == ev.getSource()) {
            final Container parentComponent = getParent()
                    .getParent();
            final String name = JOptionPane.showInputDialog(parentComponent, tr("Please enter the session name:"),
                    tr("Input"), JOptionPane.PLAIN_MESSAGE);
            if (name == null) {
                return;
            }

            final String trimmedName = name.trim();
            if (trimmedName.length() == 0) {
                DialogFactory.showErrorMessage(parentComponent,
                        String.format(tr("'%s' is an invalid session name."), trimmedName));
                return;
            }

            List<String> filePaths = new ArrayList<>();
            JInternalFrame[] frames = mainWindow.getDesktop().getAllFrames();
            for (JInternalFrame frame : frames) {
                LogWindow logWindow = (LogWindow) frame;
                filePaths.add(logWindow.getFile()
                        .getAbsolutePath());
            }

            Session session = new Session();
            session.setName(trimmedName);
            session.getFileNames().addAll(filePaths);
            mainWindow.getConfigData().getSessions().add(session);
        }
    }
}
