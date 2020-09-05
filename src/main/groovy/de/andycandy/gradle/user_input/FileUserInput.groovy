package de.andycandy.gradle.user_input

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.transform.NamedParam
import groovy.transform.NamedParams

import javax.swing.*

@CompileStatic
class FileUserInput extends UserInput {

	IDialogHelper dialogHelper = DialogHelper.instance

	Closure onInput
	
	List<String> extensions

	Integer fileSelectionMode

	Integer dialogType

	File currentDirectory
	
	@Override
	public void execute() {

		try {
			this.onInput.call(dialogHelper.createAndShowFileChooserDialog(title, fileSelectionMode, dialogType, extensions, currentDirectory))
		}
		catch(CancelException e) {
			this.onCancel.call()
		}
	}
	
	@CompileDynamic
	public static class Creator implements ICreator {

		@Override
		public UserInput createFileUserInput(Map params) {

			Map map = params.clone()

			FileUserInput userInput = new FileUserInput()
			
			userInput.title = map.remove('title') ?: ''
			userInput.condition = map.remove('condition') ?: { true }
			userInput.onInput = map.remove('onInput') ?: {}
			userInput.extensions = []
			userInput.extensions += map.remove('extensions') ?: []
			if (map.extension != null) {
				userInput.extensions << map.remove('extension')
			}
			userInput.fileSelectionMode = map.remove('fileSelectionMode') ?: JFileChooser.FILES_ONLY
			userInput.dialogType = map.remove('dialogType') ?: JFileChooser.OPEN_DIALOG
			userInput.currentDirectory = map.remove('currentDirectory')
			userInput.onCancel = map.remove('onCancel') ?: {}

			if (!map.isEmpty()) {
				throw new IllegalArgumentException("There are unknown parameters $map")
			}

			return userInput
		}		
	}
	
	interface ICreator {
		UserInput createFileUserInput(@NamedParams([
			@NamedParam(value = "title", type = String),
			@NamedParam(value = "condition", type = Closure),
			@NamedParam(value = "onCancel", type = Closure),
			@NamedParam(value = "onInput", type = Closure),
			@NamedParam(value = "extensions", type = List),
			@NamedParam(value = "extension", type = String),
			@NamedParam(value = "fileSelectionMode", type = Integer),
			@NamedParam(value = "dialogType", type = Integer),
			@NamedParam(value = "currentDirectory", type = File)
			]) Map map)
	}
}
