package de.andycandy.gradle.user_input

import groovy.transform.CompileStatic

import javax.swing.*

@CompileStatic
class UserInputExtension implements IUserInputsCreator {
	
	@Override
	public UserInput createTextUserInput(Map map) {
		
		UserInput userInput = UserInputsCreator.instance.createTextUserInput(map)
		userInput.executeIfConditionTrue()
		
		return userInput
	}
	
	@Override
	public UserInput createPasswordUserInput(Map map) {
		
		UserInput userInput = UserInputsCreator.instance.createPasswordUserInput(map)
		userInput.executeIfConditionTrue()
		
		return userInput
	}
	
	@Override
	public UserInput createDropdownUserInput(Map map) {
		
		UserInput userInput = UserInputsCreator.instance.createDropdownUserInput(map)
		userInput.executeIfConditionTrue()
		
		return userInput
	}
	
	@Override
	public UserInput createYesNoUserInput(Map map) {
		
		UserInput userInput = UserInputsCreator.instance.createYesNoUserInput(map)
		userInput.executeIfConditionTrue()
		
		return userInput
	}
	
	@Override
	public UserInput createFileUserInput(Map map) {
		
		UserInput userInput = UserInputsCreator.instance.createFileUserInput(map)
		userInput.executeIfConditionTrue()
		
		return userInput
	}

	public Integer getFilesOnly() {
		return JFileChooser.FILES_ONLY
	}

	public Integer getDirectoriesOnly() {
		return JFileChooser.DIRECTORIES_ONLY
	}

	public Integer getFilesAndDirectories() {
		return JFileChooser.FILES_AND_DIRECTORIES
	}

	public Integer getOpenDialog() {
		return JFileChooser.OPEN_DIALOG
	}

	public Integer getSaveDialog() {
		return JFileChooser.SAVE_DIALOG
	}
}
