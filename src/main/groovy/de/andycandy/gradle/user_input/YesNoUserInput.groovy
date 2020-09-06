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

	public static class Creator implements ICreator {
		
		@Override
		public UserInput createYesNoUserInput(Map params) {

			YesNoUserInput userInput = new YesNoUserInput()

			ParamHelper.create(params, userInput) {
				mapParam('title')
				mapParam('message')
				mapParamWithFallback('condition') { true }
				mapParamWithFallback('onInput') {}
				mapParamWithFallback('onYes') {}
				mapParamWithFallback('onNo') {}
				mapParamWithFallback('onCancel') {}
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
