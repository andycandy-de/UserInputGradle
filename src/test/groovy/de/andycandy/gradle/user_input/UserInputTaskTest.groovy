package de.andycandy.gradle.user_input

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class UserInputTaskTest extends Specification {

    Project project
    UserInputTask userInputTask

    def setup() {
        project = ProjectBuilder.builder().build()
        userInputTask = project.task('userInput', type: UserInputTask)
        UserInputsCreator.instance.textUserInputCreator = Mock(TextUserInput.ICreator)
        UserInputsCreator.instance.passwordUserInputCreator = Mock(PasswordUserInput.ICreator)
        UserInputsCreator.instance.dropdownUserInputCreator = Mock(DropdownUserInput.ICreator)
        UserInputsCreator.instance.yesNoUserInputCreator = Mock(YesNoUserInput.ICreator)
        UserInputsCreator.instance.fileUserInputCreator = Mock(FileUserInput.ICreator)
    }

    def 'test create text user input'() {
        given:
        Map map = [title: 'title']
        UserInput userInput = (new TextUserInput.Creator()).createTextUserInput(map)
        1 * UserInputsCreator.instance.textUserInputCreator.createTextUserInput( _ ) >> { userInput }

        when:
        userInputTask.createTextUserInput(map)

        then:
        userInputTask.userInputs == [ userInput ]
    }

    def 'test create password user input'() {
        given:
        Map map = [title: 'title']
        UserInput userInput = (new PasswordUserInput.Creator()).createPasswordUserInput(map)
        1 * UserInputsCreator.instance.passwordUserInputCreator.createPasswordUserInput( _ ) >> { userInput }

        when:
        userInputTask.createPasswordUserInput(map)

        then:
        userInputTask.userInputs == [ userInput ]
    }

    def 'test create dropdown user input'() {
        given:
        Map map = [title: 'title']
        UserInput userInput = (new DropdownUserInput.Creator()).createDropdownUserInput(map)
        1 * UserInputsCreator.instance.dropdownUserInputCreator.createDropdownUserInput( _ ) >> { userInput }

        when:
        userInputTask.createDropdownUserInput(map)

        then:
        userInputTask.userInputs == [ userInput ]
    }

    def 'test create yes no user input'() {
        given:
        Map map = [title: 'title']
        UserInput userInput = (new YesNoUserInput.Creator()).createYesNoUserInput(map)
        1 * UserInputsCreator.instance.yesNoUserInputCreator.createYesNoUserInput( _ ) >> { userInput }

        when:
        userInputTask.createYesNoUserInput(map)

        then:
        userInputTask.userInputs == [ userInput ]
    }

    def 'test create file user input'() {
        given:
        Map map = [title: 'title']
        UserInput userInput = (new FileUserInput.Creator()).createFileUserInput(map)
        1 * UserInputsCreator.instance.fileUserInputCreator.createFileUserInput( _ ) >> { userInput }

        when:
        userInputTask.createFileUserInput(map)

        then:
        userInputTask.userInputs == [ userInput ]
    }

    def 'test task empty'() {
        when:
        userInputTask.executeUserInputs()

        then:
        userInputTask.userInputs.empty
    }

    def 'test task with input'() {
        given:
        Map map = [title: 'title']
        def executed = false
        UserInput userInput = [executeIfConditionTrue : { executed = true }] as UserInput
        1 * UserInputsCreator.instance.textUserInputCreator.createTextUserInput( _ ) >> { userInput }

        when:
        userInputTask.createTextUserInput(map)
        userInputTask.executeUserInputs()

        then:
        executed
    }

    def cleanup() {
        UserInputsCreator.instance.textUserInputCreator = new TextUserInput.Creator()
        UserInputsCreator.instance.passwordUserInputCreator = new PasswordUserInput.Creator()
        UserInputsCreator.instance.dropdownUserInputCreator = new DropdownUserInput.Creator()
        UserInputsCreator.instance.yesNoUserInputCreator = new YesNoUserInput.Creator()
        UserInputsCreator.instance.fileUserInputCreator = new FileUserInput.Creator()
    }
}
