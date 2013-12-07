/**
 * Beobachter is a logs watcher for the desktop. (a.k.a. full-featured tail)
 * Copyright (C) 2013 Santiago Lizardo (http://www.santiagolizardo.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.santiagolizardo.beobachter.gui.dialogs;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.SpringLayout.Constraints;

import com.santiagolizardo.beobachter.beans.Rule;
import com.santiagolizardo.beobachter.gui.dialogs.components.ColorChooser;
import com.santiagolizardo.beobachter.gui.dialogs.components.RulesTableModel;
import com.santiagolizardo.beobachter.gui.util.DialogFactory;
import com.santiagolizardo.beobachter.resources.languages.Translator;
import static com.santiagolizardo.beobachter.resources.languages.Translator._;

public class RuleDialog extends JDialog {

	private static final long serialVersionUID = 3376286156414266562L;

	private JTextField pattern;

	private JCheckBox regularExpression;

	private JLabel backgroundColor;

	private JButton pickBackgroundColor;

	private JLabel foregroundColor;

	private JButton pickForegroundColor;

	private JCheckBox ignoreCase;

	private JButton btnOk;
	private JButton btnCancel;

	public RuleDialog(final RulesTableModel rules) {

		setTitle("Add rule");
		setResizable(false);
		setModal(true);

		pattern = new JTextField(20);
		regularExpression = new JCheckBox(Translator._("Regular expression"));

		pickBackgroundColor = new JButton(Translator._("Pick"));
		pickBackgroundColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				ColorChooser colorChooser = new ColorChooser(RuleDialog.this,
						"Pick the background color", backgroundColor
								.getBackground());
				Color color = colorChooser.pickColor();
				if (color != null) {
					backgroundColor.setBackground(color);
				}
			};
		});
		backgroundColor = new JLabel("     ");
		backgroundColor.setOpaque(true);
		backgroundColor.setBackground(Color.WHITE);
		backgroundColor.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		pickForegroundColor = new JButton(Translator._("Pick"));
		pickForegroundColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				ColorChooser colorChooser = new ColorChooser(RuleDialog.this,
						_("Pick the foreground color"), foregroundColor
								.getBackground());
				Color color = colorChooser.pickColor();
				if (color != null) {
					foregroundColor.setBackground(color);
				}
			};
		});
		foregroundColor = new JLabel("     ");
		foregroundColor.setOpaque(true);
		foregroundColor.setBackground(Color.BLACK);
		foregroundColor.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		ignoreCase = new JCheckBox(Translator._("Ignore case"));

		btnOk = new JButton(Translator._("Ok"));
		getRootPane().setDefaultButton(btnOk);
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if ("".equals(pattern.getText().trim())) {
					DialogFactory.showErrorMessage(RuleDialog.this,
							Translator._("Please complete the field pattern"));
					pattern.requestFocus();
					return;
				}

				Rule rule = new Rule();
				rule.setPattern(pattern.getText());
				rule.setRegularExpression(regularExpression.isSelected());
				rule.setIgnoreCase(ignoreCase.isSelected());
				rule.setBackgroundColor(backgroundColor.getBackground());
				rule.setForegroundColor(foregroundColor.getBackground());
				rules.addRule(rule);

				dispose();
			}
		});

		btnCancel = new JButton(Translator._("Cancel"));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				setVisible(false);
				dispose();
			}
		});

		defineLayout();
		setLocationRelativeTo(null);
	}

	private void defineLayout() {
		SpringLayout layout = new SpringLayout();
		Container contentPane = getContentPane();
		contentPane.setLayout(layout);

		JLabel _pattern = new JLabel(Translator._("Pattern"));
		JLabel _backgroundColor = new JLabel(Translator._("Background color"));
		JLabel _foregroundColor = new JLabel(Translator._("Foreground color"));

		contentPane.add(_pattern);
		contentPane.add(pattern);
		contentPane.add(regularExpression);
		contentPane.add(_backgroundColor);
		contentPane.add(backgroundColor);
		contentPane.add(pickBackgroundColor);
		contentPane.add(_foregroundColor);
		contentPane.add(foregroundColor);
		contentPane.add(pickForegroundColor);
		contentPane.add(ignoreCase);
		contentPane.add(btnOk);
		contentPane.add(btnCancel);

		layout.putConstraint(SpringLayout.NORTH, _pattern, 5,
				SpringLayout.NORTH, contentPane);
		layout.putConstraint(SpringLayout.NORTH, pattern, 5,
				SpringLayout.SOUTH, _pattern);
		layout.putConstraint(SpringLayout.NORTH, regularExpression, 5,
				SpringLayout.SOUTH, pattern);
		layout.putConstraint(SpringLayout.NORTH, ignoreCase, 5,
				SpringLayout.SOUTH, regularExpression);
		layout.putConstraint(SpringLayout.NORTH, _backgroundColor, 5,
				SpringLayout.SOUTH, ignoreCase);
		layout.putConstraint(SpringLayout.NORTH, backgroundColor, 5,
				SpringLayout.SOUTH, _backgroundColor);
		layout.putConstraint(SpringLayout.NORTH, pickBackgroundColor, 5,
				SpringLayout.SOUTH, _backgroundColor);
		layout.putConstraint(SpringLayout.NORTH, _foregroundColor, 5,
				SpringLayout.SOUTH, backgroundColor);
		layout.putConstraint(SpringLayout.NORTH, foregroundColor, 5,
				SpringLayout.SOUTH, _foregroundColor);
		layout.putConstraint(SpringLayout.NORTH, pickForegroundColor, 5,
				SpringLayout.SOUTH, _foregroundColor);
		layout.putConstraint(SpringLayout.NORTH, btnOk, 5, SpringLayout.SOUTH,
				pickForegroundColor);
		layout.putConstraint(SpringLayout.NORTH, btnCancel, 5,
				SpringLayout.SOUTH, pickForegroundColor);

		layout.putConstraint(SpringLayout.WEST, _pattern, 5, SpringLayout.WEST,
				contentPane);
		layout.putConstraint(SpringLayout.WEST, pattern, 5, SpringLayout.WEST,
				contentPane);
		layout.putConstraint(SpringLayout.WEST, regularExpression, 5,
				SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.WEST, ignoreCase, 5,
				SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.WEST, _backgroundColor, 5,
				SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.WEST, backgroundColor, 5,
				SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.WEST, pickBackgroundColor, 5,
				SpringLayout.EAST, backgroundColor);
		layout.putConstraint(SpringLayout.WEST, _foregroundColor, 5,
				SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.WEST, foregroundColor, 5,
				SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.WEST, pickForegroundColor, 5,
				SpringLayout.EAST, foregroundColor);
		layout.putConstraint(SpringLayout.WEST, btnOk, 5, SpringLayout.WEST,
				contentPane);
		layout.putConstraint(SpringLayout.WEST, btnCancel, 5,
				SpringLayout.EAST, btnOk);

		Constraints panelCons = layout.getConstraints(contentPane);
		Constraints okCons = layout.getConstraints(btnOk);
		Constraints patternCons = layout.getConstraints(pattern);

		panelCons.setHeight(Spring.sum(Spring.constant(5),
				okCons.getConstraint(SpringLayout.SOUTH)));
		panelCons.setWidth(Spring.sum(Spring.constant(5),
				patternCons.getConstraint(SpringLayout.EAST)));

		pack();
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
