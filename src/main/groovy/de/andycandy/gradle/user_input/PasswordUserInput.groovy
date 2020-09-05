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
	
	@CompileDynamic
	public static class Creator implements ICreator {
		
		@Override
		public UserInput createPasswordUserInput(Map params) {

			Map map = params.clone()

			PasswordUserInput userInput = new PasswordUserInput()
			
			userInput.title = map.remove('title') ?: ''
			userInput.message = map.remove('message')
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
		UserInput createPasswordUserInput(@NamedParams([
			@NamedParam(value = "title", type = String),
			@NamedParam(value = "message", type = String),
			@NamedParam(value = "condition", type = Closure),
			@NamedParam(value = "onCancel", type = Closure),
			@NamedParam(value = "onInput", type = Closure)
			]) Map map)
	}
}
