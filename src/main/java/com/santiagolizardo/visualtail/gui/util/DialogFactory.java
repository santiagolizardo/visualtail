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
package com.santiagolizardo.visualtail.gui.util;

import java.awt.Component;

import javax.swing.JOptionPane;

import com.santiagolizardo.visualtail.Constants;

public class DialogFactory {

	public static void showInformationMessage(Component parent, String message) {
		JOptionPane.showMessageDialog(parent, message, Constants.APP_NAME,
				JOptionPane.PLAIN_MESSAGE);
	}

	public static void showErrorMessage(Component parent, String message) {
		JOptionPane.showMessageDialog(parent, message, Constants.APP_NAME,
				JOptionPane.ERROR_MESSAGE);
	}

	public static boolean showQuestionDialog(Component parent, String message) {
		return (JOptionPane.showConfirmDialog(parent, message,
				Constants.APP_NAME, JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.YES_OPTION);
	}

	public static String showInputDialog(Component parent, String message) {
		return JOptionPane.showInputDialog(parent, message, Constants.APP_NAME,
				JOptionPane.OK_CANCEL_OPTION);
	}
}
