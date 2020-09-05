package de.andycandy.gradle.user_input

interface IUserInputsCreator extends
	TextUserInput.ICreator,
	PasswordUserInput.ICreator,
	DropdownUserInput.ICreator,
	YesNoUserInput.ICreator,
	FileUserInput.ICreator {
}
