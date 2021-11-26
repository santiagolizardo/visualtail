/*
  This file is part of VisualTail, a graphical log file monitor.

  VisualTail is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  VisualTail is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with VisualTail.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.santiagolizardo.visualtail.gui.models;

import java.util.regex.Pattern;
import javax.swing.DefaultListModel;

public class LogListModel extends DefaultListModel<String> {
	
	private Pattern replacePattern;
	private String replacementString;
	
	public void updateReplacerValues(Pattern replacePattern, String replacementString) {
		this.replacePattern = replacePattern;
		this.replacementString = replacementString;
		fireContentsChanged(this, 0, getSize());
	}

	@Override
	public String get(int index) {
		String line = super.get(index);
		
		if( null != replacePattern ) {
			return replacePattern.matcher(line).replaceAll(replacementString);
		}
		
		return line;
	}
}
