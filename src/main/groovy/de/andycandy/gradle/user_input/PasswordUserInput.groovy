package de.andycandy.gradle.user_input

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.transform.NamedParam
import groovy.transform.NamedParams

import javax.swing.*

@CompileStatic
class PasswordUserInput extends UserInput {
	
	IDialogHelper dialogHelper = DialogHelper.instance
	
	String message
	
	Closure onInput

	@Override
	public void execute() {
		
		JPasswordField passwordField = new JPasswordField()
		passwordField.selectAll()

		try {
			dialogHelper.createAndShowInputDialog(passwordField, title, message)
			this.onInput.call(String.valueOf(passwordField.password))
		}
		catch (CancelException e){
			this.onCancel.call()
		}
	}

	public static class Creator implements ICreator {
		
		@Override
		public UserInput createPasswordUserInput(Map params) {

			PasswordUserInput userInput = new PasswordUserInput()

			ParamHelper.create(params, userInput) {
				mapParam('title')
				mapParam('message')
				mapParamWithFallback('condition') { true }
				mapParamWithFallback('onInput') {}
				mapParamWithFallback('onCancel') {}
			}

			return userInput
		}
	}
	
	public interface ICreator {
		UserInput createPasswordUserInput(@NamedParams([
			@NamedParam(value = "title", type = String),
			@NamedParam(value = "message", type = String),
			@NamedParam(value = "condition", type = Closure),
			@NamedParam(value = "onCancel", type = Closure),
			@NamedParam(value = "onInput", type = Closure)
			]) Map map)
	}
}
