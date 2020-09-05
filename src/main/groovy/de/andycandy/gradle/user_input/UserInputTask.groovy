package de.andycandy.gradle.user_input

import groovy.transform.CompileStatic
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction

@CompileStatic
class UserInputTask extends DefaultTask implements IUserInputsCreator {
	
	private List<UserInput> userInputs = []
	
	@Override
	public UserInput createTextUserInput(Map map) {
		
		UserInput userInput = UserInputsCreator.instance.createTextUserInput(map)
		userInputs << userInput
		
		return userInput
	}
	
	@Override
	public UserInput createPasswordUserInput(Map map) {
		
		UserInput userInput = UserInputsCreator.instance.createPasswordUserInput(map)
		userInputs << userInput
		
		return userInput
	}
	
	@Override
	public UserInput createDropdownUserInput(Map map) {
		
		UserInput userInput = UserInputsCreator.instance.createDropdownUserInput(map)
		userInputs << userInput
		
		return userInput
	}
	
	@Override
	public UserInput  createYesNoUserInput(Map map) {
		
		UserInput userInput = UserInputsCreator.instance.createYesNoUserInput(map)
		userInputs << userInput
		
		return userInput
	}
	
	@Override
	public UserInput createFileUserInput(Map map) {
		
		UserInput userInput = UserInputsCreator.instance.createFileUserInput(map)
		userInputs << userInput
		
		return userInput
	}

	@Internal
	List<UserInput> getUserInputs() {
		return userInputs
	}
	
	@TaskAction
	void executeUserInputs() {
		userInputs.each { userInput -> userInput.executeIfConditionTrue() }
	}
}
