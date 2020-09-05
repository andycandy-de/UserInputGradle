package de.andycandy.gradle.user_input

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.transform.NamedParam
import groovy.transform.NamedParams

import javax.swing.*

@CompileStatic
class TextUserInput extends UserInput {
	
	IDialogHelper dialogHelper = DialogHelper.instance
	
	String message
	
	String defaultText
	
	Closure onInput
	
	@Override
	public void execute() {
		
		JTextField textField = new JTextField()
		textField.text = defaultText
		textField.selectAll()

		try {
			dialogHelper.createAndShowInputDialog(textField, title, message)
			this.onInput.call(textField.text)
		}
		catch (CancelException e) {
			this.onCancel.call()
		}
	}

	@CompileDynamic
	public static class Creator implements ICreator {
		
		@Override
		public UserInput createTextUserInput(Map params) {

			Map map = params.clone()

			TextUserInput userInput = new TextUserInput()
			
			userInput.title = map.remove('title') ?: ''
			userInput.message = map.remove('message')
			userInput.defaultText = map.remove('defaultText') ?: ''
			userInput.condition = map.remove('condition') ?: { true }
			userInput.onInput = map.remove('onInput') ?: {}
			userInput.onCancel = map.remove('onCancel') ?: {}

			if (!map.isEmpty()) {
				throw new IllegalArgumentException("There are unknown parameters $map")
			}

			return userInput
		}
	}
	
	public interface ICreator {
		UserInput createTextUserInput(@NamedParams([
			@NamedParam(value = "title", type = String),
			@NamedParam(value = "message", type = String),
			@NamedParam(value = "condition", type = Closure),
			@NamedParam(value = "onCancel", type = Closure),
			@NamedParam(value = "defaultText", type = String),
			@NamedParam(value = "onInput", type = Closure)
			]) Map map);
	}
}
