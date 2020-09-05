package de.andycandy.gradle.user_input

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.transform.NamedParam
import groovy.transform.NamedParams

@CompileStatic
class YesNoUserInput extends UserInput {

	IDialogHelper dialogHelper = DialogHelper.instance

	String message
	
	Closure onYes
	
	Closure onNo
	
	Closure onInput
	
	@Override
	public void execute() {

		try {
			if (dialogHelper.createYesNoDialog(title, message)) {
				this.onYes.call()
				this.onInput.call(true)
			}
			else {
				this.onNo.call()
				this.onInput.call(false)
			}
		}
		catch (CancelException e) {
			this.onCancel.call()
		}
	}
	
	@CompileDynamic
	public static class Creator implements ICreator {
		
		@Override
		public UserInput createYesNoUserInput(Map map) {
		
			YesNoUserInput userInput = new YesNoUserInput()
			
			userInput.title = map.remove('title') ?: ''
			userInput.message = map.remove('message')
			userInput.condition = map.remove('condition') ?: { true }
			userInput.onInput = map.remove('onInput') ?: {}
			userInput.onYes = map.remove('onYes') ?: {}
			userInput.onNo = map.remove('onNo') ?: {}
			userInput.onCancel = map.remove('onCancel') ?: {}

			if (!map.isEmpty()) {
				throw new IllegalArgumentException("There are unknown parameters $map")
			}

			return userInput
		}
	}
	
	interface ICreator {
		UserInput createYesNoUserInput(@NamedParams([
			@NamedParam(value = "title", type = String),
			@NamedParam(value = "message", type = String),
			@NamedParam(value = "condition", type = Closure),
			@NamedParam(value = "onCancel", type = Closure),
			@NamedParam(value = "onYes", type = Closure),
			@NamedParam(value = "onNo", type = Closure),
			@NamedParam(value = "onInput", type = Closure)
			]) Map map)
	}
}
