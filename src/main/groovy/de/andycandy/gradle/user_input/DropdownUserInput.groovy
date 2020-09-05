package de.andycandy.gradle.user_input

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.transform.NamedParam
import groovy.transform.NamedParams

import javax.swing.*

@CompileStatic
class DropdownUserInput extends UserInput {
	
	IDialogHelper dialogHelper = DialogHelper.instance
	
	List<Object> items
	
	String message
	
	Object selectedItem
	
	Closure onInput
	
	@Override
	public void execute() {
		
		JComboBox comboBox = new JComboBox(items as Object[])
		if (selectedItem != null) {
			comboBox.selectedItem = selectedItem
		}
		else if (!items.empty){
			comboBox.selectedItem = items[0]
		}

		try {
			dialogHelper.createAndShowInputDialog(comboBox, title, message)
			this.onInput.call(comboBox.selectedItem)
		}
		catch (CancelException e) {
			this.onCancel.call()
		}
	}
	
	@CompileDynamic
	public static class Creator implements ICreator {
		
		@Override
		public UserInput createDropdownUserInput(Map params) {

			Map map = params.clone()

			DropdownUserInput userInput = new DropdownUserInput()
			
			userInput.title = map.remove('title') ?: ''
			userInput.message = map.remove('message')
			userInput.condition = map.remove('condition') ?: { true }
			userInput.items = map.remove('items') ?: []
			userInput.selectedItem = map.remove('selectedItem')
			userInput.onInput = map.remove('onInput') ?: {}
			userInput.onCancel = map.remove('onCancel') ?: {}

			if (!map.isEmpty()) {
				throw new IllegalArgumentException("There are unknown parameters $map")
			}

			return userInput
		}
	}
	
	
	public interface ICreator {
		UserInput createDropdownUserInput(@NamedParams([
			@NamedParam(value = "title", type = String),
			@NamedParam(value = "message", type = String),
			@NamedParam(value = "condition", type = Closure),
			@NamedParam(value = "onCancel", type = Closure),
			@NamedParam(value = "items", type = List),
			@NamedParam(value = "selectedItem", type = Object),
			@NamedParam(value = "onInput", type = Closure)
			]) Map map);
	}
}