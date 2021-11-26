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
package com.santiagolizardo.visualtail.engine;

import java.util.LinkedList;
import java.util.List;

public class TailNotifier implements Runnable {

    private final Tail tail;
    private final List<TailListener> listeners;

    public TailNotifier(String fileName) {
        tail = new Tail(fileName);
        listeners = new LinkedList<>();
    }

    public Tail getTail() {
        return tail;
    }
    
    @Override
    public void run() {
        if (tail.hasMoreLines()) {
            List<String> lines = tail.readNextLines(10000);
            lines.stream().filter((line) -> (!line.isEmpty())).forEachOrdered(this::notifyListeners);
        }
    }

    public void addListener(TailListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners(String line) {
        listeners.forEach((listener) -> listener.onFileChanges(line));
    }
}
