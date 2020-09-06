package de.andycandy.gradle.user_input

import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

import javax.swing.*

class DropdownUserInputTest extends Specification {

    def 'plugin extension create dropdown user input'() {
        given:
        def project = ProjectBuilder.builder().build()
        def input = null
        def preselected = null
        DropdownUserInput userInput = null
        UserInputsCreator.instance.dropdownUserInputCreator = Mock(DropdownUserInput.ICreator)
        UserInputsCreator.instance.dropdownUserInputCreator.createDropdownUserInput( _ as Map) >> { Map map ->
            userInput = new DropdownUserInput.Creator().createDropdownUserInput(map)
            IDialogHelper dialogHelper = Mock(IDialogHelper)
            dialogHelper.createAndShowInputDialog( _ as JComboBox, _ as String, _ as String) >> { combobox, title, message ->
                preselected = combobox.selectedItem
                combobox.selectedItem = 'item2'
            }
            userInput.dialogHelper = dialogHelper

            return userInput
        }

        when:
        project.plugins.apply('de.andycandy.gradle.user_input')
        project.userInput.createDropdownUserInput title: 'title', message: 'message',
                items: ['item1', 'item2'], onInput: { input = it },
                onCancel: { 'onCancel' }, condition: { true }

        then:
        input == 'item2'
        preselected == 'item1'
        userInput.title == 'title'
        userInput.message == 'message'
        userInput.items == ['item1', 'item2']
        userInput.selectedItem == null
        userInput.onInput('onInput') == 'onInput'
        userInput.onCancel() == 'onCancel'
        userInput.condition()
    }

    def 'plugin extension create dropdown user input with preselected'() {
        given:
        def project = ProjectBuilder.builder().build()
        def input = null
        def preselected = null
        DropdownUserInput userInput = null
        UserInputsCreator.instance.dropdownUserInputCreator = Mock(DropdownUserInput.ICreator)
        UserInputsCreator.instance.dropdownUserInputCreator.createDropdownUserInput( _ as Map) >> { Map map ->
            userInput = new DropdownUserInput.Creator().createDropdownUserInput(map)
            IDialogHelper dialogHelper = Mock(IDialogHelper)
            dialogHelper.createAndShowInputDialog( _ as JComboBox, _ as String, _ as String) >> { combobox, title, message ->
                preselected = combobox.selectedItem
                combobox.selectedItem = 'item1'
            }
            userInput.dialogHelper = dialogHelper

            return userInput
        }

        when:
        project.plugins.apply('de.andycandy.gradle.user_input')
        project.userInput.createDropdownUserInput title: 'title', message: 'message',
                items: ['item1', 'item2'], selectedItem: 'item2', onInput: { input = it },
                onCancel: { 'onCancel' }, condition: { true }

        then:
        input == 'item1'
        preselected == 'item2'
        userInput.title == 'title'
        userInput.message == 'message'
        userInput.items == ['item1', 'item2']
        userInput.selectedItem == 'item2'
        userInput.onInput('onInput') == 'onInput'
        userInput.onCancel() == 'onCancel'
        userInput.condition()
    }

    def 'plugin extension create dropdown user input with onCancel'() {
        given:
        def project = ProjectBuilder.builder().build()
        def input = null
        DropdownUserInput userInput = null
        def preselected = null
        boolean onCancel = false
        UserInputsCreator.instance.dropdownUserInputCreator = Mock(DropdownUserInput.ICreator)
        UserInputsCreator.instance.dropdownUserInputCreator.createDropdownUserInput( _ as Map) >> { Map map ->
            userInput = new DropdownUserInput.Creator().createDropdownUserInput(map)
            IDialogHelper dialogHelper = Mock(IDialogHelper)
            dialogHelper.createAndShowInputDialog( _ as JComboBox, _ as String, _ as String) >> {combobox, title, message ->
                preselected = combobox.selectedItem
                throw new CancelException()
            }
            userInput.dialogHelper = dialogHelper

            return userInput
        }

        when:
        project.plugins.apply('de.andycandy.gradle.user_input')
        project.userInput.createDropdownUserInput title: 'title', message: 'message',
                items: ['item1', 'item2'], onInput: { input = it },
                onCancel: { onCancel = true }

        then:
        input == null
        preselected == 'item1'
        onCancel
        userInput.title == 'title'
        userInput.message == 'message'
        userInput.items == ['item1', 'item2']
        userInput.onInput('onInput') == 'onInput'
        userInput.onCancel()
        userInput.condition()
    }

    def 'plugin extension create dropdown user input condition false'() {
        given:
        def project = ProjectBuilder.builder().build()
        def execute = false
        DropdownUserInput userInput = null
        UserInputsCreator.instance.dropdownUserInputCreator = Mock(DropdownUserInput.ICreator)
        UserInputsCreator.instance.dropdownUserInputCreator.createDropdownUserInput( _ as Map) >> { Map map ->
            userInput = new DropdownUserInput.Creator().createDropdownUserInput(map)
            IDialogHelper dialogHelper = Mock(IDialogHelper)
            dialogHelper.createAndShowInputDialog( _ as JComboBox, _ as String, _ as String) >> {combobox, title, message ->
                execute = true
            }
            userInput.dialogHelper = dialogHelper
            return userInput
        }

        when:
        project.plugins.apply('de.andycandy.gradle.user_input')
        project.userInput.createDropdownUserInput condition: { false }

        then:
        !execute
        userInput.title == null
        userInput.message == null
        userInput.items == []
        userInput.selectedItem == null
        userInput.condition() == false
        userInput.onInput() == null
        userInput.onCancel() == null
    }

    def 'plugin extension create dropdown user input unknown parameters'() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        project.plugins.apply('de.andycandy.gradle.user_input')
        project.userInput.createDropdownUserInput condition: { false }, unknown1: 'unknown1', unknown2: 'unknown2'

        then:
        final IllegalArgumentException exception = thrown()
        exception.message == "There are unknown parameters: ${[unknown1: 'unknown1', unknown2: 'unknown2']}"
    }

    def cleanup() {
        UserInputsCreator.instance.textUserInputCreator = new TextUserInput.Creator()
        UserInputsCreator.instance.passwordUserInputCreator = new PasswordUserInput.Creator()
        UserInputsCreator.instance.dropdownUserInputCreator = new DropdownUserInput.Creator()
        UserInputsCreator.instance.yesNoUserInputCreator = new YesNoUserInput.Creator()
        UserInputsCreator.instance.fileUserInputCreator = new FileUserInput.Creator()
    }
}
