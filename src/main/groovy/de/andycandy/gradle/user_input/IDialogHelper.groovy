package de.andycandy.gradle.user_input

import javax.swing.*

interface IDialogHelper {

	void createAndShowInputDialog(JComponent component, String title, String message) throws CancelException

	boolean createYesNoDialog(String title, String message) throws CancelException

	File createAndShowFileChooserDialog(String title, Integer fileSelectionMode, Integer dialogType, List extensions, File currentDirectory) throws CancelException
}
