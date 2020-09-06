package de.andycandy.gradle.user_input

import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class YesNoUserInputTest extends Specification {

    def 'plugin extension create yes no user input with yes'() {
        given:
        def project = ProjectBuilder.builder().build()
        def title = null
        def message = null
        YesNoUserInput userInput = null
        boolean onYes = false
        boolean onNo = false
        boolean onInput = false
        boolean onCancel = false
        UserInputsCreator.instance.yesNoUserInputCreator = Mock(YesNoUserInput.ICreator)
        UserInputsCreator.instance.yesNoUserInputCreator.createYesNoUserInput( _ as Map) >> { Map map ->
            userInput = new YesNoUserInput.Creator().createYesNoUserInput(map)
            IDialogHelper dialogHelper = Mock(IDialogHelper)
            dialogHelper.createYesNoDialog( _ as String, _ as String) >> { t, m ->
                title = t
                message = m
                return true
            }
            userInput.dialogHelper = dialogHelper

            return userInput
        }

        when:
        project.plugins.apply('de.andycandy.gradle.user_input')
        project.userInput.createYesNoUserInput title: 'title', message: 'message',
                onYes: { onYes = true }, onNo: { onNo = true},
                onInput: { onInput = it}, onCancel: { onCancel = true }

        then:
        title == 'title'
        message == 'message'
        userInput.title == 'title'
        userInput.message == 'message'
        onYes
        onInput
        !onNo
        !onCancel
    }

    def 'plugin extension create yes no user input with no'() {
        given:
        def project = ProjectBuilder.builder().build()
        def title = null
        def message = null
        YesNoUserInput userInput = null
        boolean onYes = false
        boolean onNo = false
        boolean onInput = false
        boolean onCancel = false
        UserInputsCreator.instance.yesNoUserInputCreator = Mock(YesNoUserInput.ICreator)
        UserInputsCreator.instance.yesNoUserInputCreator.createYesNoUserInput( _ as Map) >> { Map map ->
            userInput = new YesNoUserInput.Creator().createYesNoUserInput(map)
            IDialogHelper dialogHelper = Mock(IDialogHelper)
            dialogHelper.createYesNoDialog( _ as String, _ as String) >> { t, m ->
                title = t
                message = m
                return false
            }
            userInput.dialogHelper = dialogHelper

            return userInput
        }

        when:
        project.plugins.apply('de.andycandy.gradle.user_input')
        project.userInput.createYesNoUserInput title: 'title', message: 'message',
                onYes: { onYes = true }, onNo: { onNo = true},
                onInput: { onInput = it}, onCancel: { onCancel = true }

        then:
        title == 'title'
        message == 'message'
        userInput.title == 'title'
        userInput.message == 'message'
        !onYes
        !onInput
        onNo
        !onCancel
    }

    def 'plugin extension create yes no user input with onCancel'() {
        given:
        def project = ProjectBuilder.builder().build()
        def title = null
        def message = null
        YesNoUserInput userInput = null
        boolean onYes = false
        boolean onNo = false
        boolean onInput = false
        boolean onCancel = false
        UserInputsCreator.instance.yesNoUserInputCreator = Mock(YesNoUserInput.ICreator)
        UserInputsCreator.instance.yesNoUserInputCreator.createYesNoUserInput( _ as Map) >> { Map map ->
            userInput = new YesNoUserInput.Creator().createYesNoUserInput(map)
            IDialogHelper dialogHelper = Mock(IDialogHelper)
            dialogHelper.createYesNoDialog( _ as String, _ as String) >> { t, m ->
                title = t
                message = m
                throw new CancelException()
            }
            userInput.dialogHelper = dialogHelper

            return userInput
        }

        when:
        project.plugins.apply('de.andycandy.gradle.user_input')
        project.userInput.createYesNoUserInput title: 'title', message: 'message',
                onYes: { onYes = true }, onNo: { onNo = true},
                onInput: { onInput = it}, onCancel: { onCancel = true }

        then:
        title == 'title'
        message == 'message'
        userInput.title == 'title'
        userInput.message == 'message'
        !onYes
        !onInput
        !onNo
        onCancel
    }

    def 'plugin extension create yes no user input unknown parameters'() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        project.plugins.apply('de.andycandy.gradle.user_input')
        project.userInput.createYesNoUserInput condition: { false }, unknown1: 'unknown1', unknown2: 'unknown2'

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
