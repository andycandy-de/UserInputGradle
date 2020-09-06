package de.andycandy.gradle.user_input

import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

import javax.swing.JTextField

class TextUserInputTest extends Specification {

    def 'plugin extension create text user input'() {
        given:
        def project = ProjectBuilder.builder().build()
        def input = null
        TextUserInput userInput = null
        UserInputsCreator.instance.textUserInputCreator = Mock(TextUserInput.ICreator)
        UserInputsCreator.instance.textUserInputCreator.createTextUserInput( _ as Map) >> { Map map ->
            userInput = new TextUserInput.Creator().createTextUserInput(map)
            IDialogHelper dialogHelper = Mock(IDialogHelper)
            dialogHelper.createAndShowInputDialog( _ as JTextField, _ as String, _ as String) >> { textField, title, message ->
                textField.text = 'test'
            }
            userInput.dialogHelper = dialogHelper

            return userInput
        }

        when:
        project.plugins.apply('de.andycandy.gradle.user_input')
        project.userInput.createTextUserInput title: 'title', message: 'message',
                defaultText: 'defaultText', onInput: { input = it },
                onCancel: { 'onCancel' }, condition: { true }

        then:
        input == 'test'
        userInput.title == 'title'
        userInput.message == 'message'
        userInput.defaultText == 'defaultText'
        userInput.onInput('onInput') == 'onInput'
        userInput.onCancel() == 'onCancel'
        userInput.condition()
    }

    def 'plugin extension create text user input with onCancel'() {
        given:
        def project = ProjectBuilder.builder().build()
        def input = null
        TextUserInput userInput = null
        boolean onCancel = false
        UserInputsCreator.instance.textUserInputCreator = Mock(TextUserInput.ICreator)
        UserInputsCreator.instance.textUserInputCreator.createTextUserInput( _ as Map) >> { Map map ->
            userInput = new TextUserInput.Creator().createTextUserInput(map)
            IDialogHelper dialogHelper = Mock(IDialogHelper)
            dialogHelper.createAndShowInputDialog( _ as JTextField, _ as String, _ as String) >> {textField, title, message ->
                textField.text = 'test'
                throw new CancelException()
            }
            userInput.dialogHelper = dialogHelper

            return userInput
        }

        when:
        project.plugins.apply('de.andycandy.gradle.user_input')
        project.userInput.createTextUserInput title: 'title', message: 'message',
                defaultText: 'defaultText', onInput: { input = it },
                onCancel: { onCancel = true }

        then:
        input == null
        onCancel
        userInput.title == 'title'
        userInput.message == 'message'
        userInput.defaultText == 'defaultText'
        userInput.onInput('onInput') == 'onInput'
        userInput.onCancel()
        userInput.condition()
    }

    def 'plugin extension create text user input condition false'() {
        given:
        def project = ProjectBuilder.builder().build()
        def execute = false
        TextUserInput userInput = null
        UserInputsCreator.instance.textUserInputCreator = Mock(TextUserInput.ICreator)
        UserInputsCreator.instance.textUserInputCreator.createTextUserInput( _ as Map) >> { Map map ->
            userInput = new TextUserInput.Creator().createTextUserInput(map)
            IDialogHelper dialogHelper = Mock(IDialogHelper)
            dialogHelper.createAndShowInputDialog( _ as JTextField, _ as String, _ as String) >> {textField, title, message ->
                execute = true
            }
            userInput.dialogHelper = dialogHelper
            return userInput
        }

        when:
        project.plugins.apply('de.andycandy.gradle.user_input')
        project.userInput.createTextUserInput condition: { false }

        then:
        !execute
        userInput.title == null
        !userInput.condition()
    }

    def 'plugin extension create text user input unknown parameters'() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        project.plugins.apply('de.andycandy.gradle.user_input')
        project.userInput.createTextUserInput condition: { false }, unknown1: 'unknown1', unknown2: 'unknown2'

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
