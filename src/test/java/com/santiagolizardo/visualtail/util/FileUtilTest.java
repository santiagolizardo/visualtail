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
package com.santiagolizardo.visualtail.util;

import junit.framework.TestCase;

public class FileUtilTest extends TestCase {
	
	public void testSizeFormatting() {
		assertEquals("0 B", FileUtil.byteCountToDisplaySize(0));
		assertEquals("37 B", FileUtil.byteCountToDisplaySize(37));
		assertEquals("1.00 MB", FileUtil.byteCountToDisplaySize(1024*1024));
	}
}
