package de.andycandy.gradle.user_input

import de.andycandy.gradle.user_input.*

@Singleton
class UserInputsCreator implements IUserInputsCreator {
	
	@Delegate
	TextUserInput.ICreator textUserInputCreator = new TextUserInput.Creator()
	
	@Delegate
	PasswordUserInput.ICreator passwordUserInputCreator = new PasswordUserInput.Creator()
	
	@Delegate
	DropdownUserInput.ICreator dropdownUserInputCreator = new DropdownUserInput.Creator()
	
	@Delegate
	YesNoUserInput.ICreator yesNoUserInputCreator = new YesNoUserInput.Creator()
	
	@Delegate
	FileUserInput.ICreator fileUserInputCreator = new FileUserInput.Creator()
}
