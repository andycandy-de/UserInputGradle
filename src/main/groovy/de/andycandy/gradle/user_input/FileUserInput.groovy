package de.andycandy.gradle.user_input


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

	public static class Creator implements ICreator {

		@Override
		public UserInput createFileUserInput(Map params) {

			FileUserInput userInput = new FileUserInput()

			ParamsToObjectMapper.map(params, userInput) {
				mapParam('title')
				mapParamWithFallback('condition', { true })
				mapParamWithFallback('onInput', {})
				mapParamWithFallback('extensions', [])
				mapParam('extension') { String val -> userInput.extensions << val }
				mapParamWithFallback('fileSelectionMode', JFileChooser.FILES_ONLY)
				mapParamWithFallback('dialogType', JFileChooser.OPEN_DIALOG)
				mapParam('currentDirectory')
				mapParamWithFallback('onCancel', {})
				checkForUnknownParams()
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
