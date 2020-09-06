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

	public static class Creator implements ICreator {
		
		@Override
		public UserInput createTextUserInput(Map params) {

			TextUserInput userInput = new TextUserInput()

			ParamHelper.create(params, userInput) {
				mapParam('title')
				mapParam('message')
				mapParamWithFallback('defaultText', '')
				mapParamWithFallback('condition') { true }
				mapParamWithFallback('onInput') {}
				mapParamWithFallback('onCancel') {}
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
