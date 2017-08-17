# Change Log

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/) and this project adheres to [Semantic Versioning](http://semver.org/).

## [Unreleased]

## [1.9.1] - 2017-08-25

- Windows installer now uses javaw instead of java to start the app, so that the background console doesn't show.
- New feature: You can now delete the open file from the filesystem and close the dialog automatically.

## [1.9.0] - 2017-05-25

### Added

- Cross-platform installer using Izpack replaced Nsis.

## [1.8.7] - 2016-11-10

### Added

- Better menubar integration on MacOS
- Validation of number of lines improved (negative numbers are rejected)

## 1.8.6 - 2015-05-20

### Added

- Converting config files to XML format (from .properties)

## 1.8.5 - 2015-02-26

### Added

- Replacement panel to make substitutions on the text displayed.
- The find panel now belongs to the documents and not to the main window. 
- The find functionality now has an option to perform the search in case sensitive or insensitive manner.
- Various bugfixes and improvements.

## 1.8.2 - 2014-04-06

### Added

- New font selection dialog
- Log windows start using a new font immediately after its selection.

## 1.8.1 - 2014-03-09

### Added

- New shortcuts to clear selected log and clear all logs.
- New menu item on the Help menu to suggest new features and/or report bugs.
- Bugfix affecting the number of threads being created.
- Bugfix on the GTK+ look and feels counting frames wrongly. (reported by Carlos SS)
- New icons for some menu items.
- Recent files are ordered on reverse order (recent open files first)
- Small internal refactorings.
- Last modification date shown on title bar (optional).
- New unit tests were added.
- Data folder has been removed and logTypes and sessions folder now are located under the .visualtail folder in the user's home.

## 1.8.0 - 2014-02-09

### Added

- Ability to hide selected lines.
- Application size dropped to 200Kb from almost 2Mb by removing dependencies.
- Some bugfixes and improvements thanks to Carlos SS.
- Internal code refactorings.

## 1.7.9 - 2014-01-26

### Added

- Log rules are now editable
- Bugfix on close all windows option.
- Internal refactorings for a cleaner code
- Smaller program file due to the removal of some not hard dependencies.
- Memory consumption decreased.
- License header updated
- Google analytics tracking code added to the author link.

## 1.7.8 - 2014-01-14

### Added

- Disable open recents on empty list
- Window title shows the current file
- Increase default lines from 64 to 256
- White about window
- Disable copy menu item on empty selections
- Manage sessions dialog shows how many files a session has
- More unit tests

## 1.7.7 - 2013-12-18

### Added

- Fix on find panel not returning results.
- New languages: Russian, Portuguese, Ucranian, German

## 1.7.6 - 2013-12-14

### Fixed

- Minor issues

## 1.7.5 - 2013-12-4

### Fixed

- Minor issues

## 1.7.4 - 2013-11-14

### Added

- Extend the Open file dialog to be able to select multiple files to open at a time.
- Minor bugfixes.
- Performance improvements.

## 1.7.4 - 2013-11-14

### Added

- Extend the Open file dialog to be able to select multiple files to open at a time.
- Minor bugfixes.
- Performance improvements.

## 1.7.3 - 2013-11-10

### Added

- Added an option to limit the last number of lines to show of certain file. (like tail -n)
- Added a checkbox on the Log window which allows to stop/resume watching a file. 
- AUTHORS and CREDITS files have been merged.
- About dialog now includes link to authors.
- Tiny aesthetic improvement in about dialog.
- New Java requirement is 1.7.

## 1.7.2 - 2013-11-07

### Added

- Dialogs are now well centered.

## 1.7.1 - 2013-11-06

### Added

- Improved internal testing
- Small performance improvement

## 1.7 - 2011-09

### Added

- Refactoring of the "Open file" dialog.
- Tray icon.
- More unit tests meaning more robust-stable working.
- Better handling of huge files.
- Replacement of many StringBuffers to StringBuilders.

## 1.6 - 2010-11

### Added

- VCS converted from SVN to GIT
- Ant replaced by Maven

## 1.5 - 2009-11

### Added

- The library commons-configuration was updated to version 1.6.
- The logging facility included with Java it's being used.
- Code is now more 1.5 like (generics, for(iterators), ...)
- Removed start.(sh|bat) and created the README.txt
- Minor bugfixes and optimizations.

## 1.4

### Added

- Minor GUI enhacements on the log types dialog
- Session feature implemented. It allows to load and save several opened files (logs) at the same time.
- More translations
- Minor code improvements
- Web page, http://visualtail.sf.net
- Documentation and license for every class

## 1.3

### Added

- Minor refactorings.
- Nice installer for the Windows platform.
- New translations: ja_JA, nl_NL. (see CREDITS for the people who help us)

## 1.2

### Added

- Color chooser now has a special preview panel.
- Select all/Copy now appears disabled when no internal frame is present.
- New Find/Find next options were added to the Edit menu.
- The preferences dialog allow us to change the default LAF of the application.
- The language option in preferences dialog now show us a better description of each language. (ie: ca_FR by Catalan (France))
- All the dialogs now have a better behaviour when resizes.
- New translations: pt_BR, zh_CN, ru_RU. (see CREDITS for the people who help us)
- The new ANT file allow developers to compile the application without an ide.
- Minor bug fixes.

## 1.1

### Added

- Now all the dialogs close when the ESCAPE key is pressed.
- Better administration of logtypes. Now update a logtype configuration is allowed.
- Almost all the dialogs now have a default button.
- New feature that checks if a new version is available was added.
- The application now remember the position and size of his last execution.
- New translations: pl_DE, in_IN, ca_FR. (see CREDITS for the people who help us)
- Minor bug fixes.

## 1.0

### Added

- Initial version

