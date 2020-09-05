package de.andycandy.gradle.user_input

import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

import javax.swing.*

class FileUserInputTest extends Specification {

    def 'plugin extension create file user input'() {
        given:
        def project = ProjectBuilder.builder().build()
        def file = new File('./test.txt')
        def input = null
        def title = null
        def fileSelectionMode = null
        def dialogType = null
        def extensions = null
        def currentDirectory = null
        def onCancel = false
        FileUserInput userInput = null
        UserInputsCreator.instance.fileUserInputCreator = Mock(FileUserInput.ICreator)
        UserInputsCreator.instance.fileUserInputCreator.createFileUserInput( _ as Map) >> { Map map ->
            userInput = new FileUserInput.Creator().createFileUserInput(map)
            IDialogHelper dialogHelper = Mock(IDialogHelper)
            dialogHelper.createAndShowFileChooserDialog( _ , _ , _ , _ , _ ) >> { t, m, d, e, f ->
                title = t
                fileSelectionMode = m
                dialogType = d
                extensions = e
                currentDirectory = f
                return file
            }
            userInput.dialogHelper = dialogHelper

            return userInput
        }

        when:
        project.plugins.apply('de.andycandy.gradle.user_input')
        project.userInput.createFileUserInput title: 'title', condition: { true },
                onInput: { input = it }, onCancel: { onCancel = true }

        then:
        input == file
        title == 'title'
        fileSelectionMode == JFileChooser.FILES_ONLY
        dialogType == JFileChooser.OPEN_DIALOG
        extensions == []
        currentDirectory == null
        !onCancel
        userInput.title == 'title'
        userInput.condition()
        userInput.fileSelectionMode == JFileChooser.FILES_ONLY
        userInput.dialogType == JFileChooser.OPEN_DIALOG
        userInput.currentDirectory == null
    }

    def 'plugin extension create file user input with fileSelectionMode and dialogType'() {
        given:
        def project = ProjectBuilder.builder().build()
        def file = new File('./test.txt')
        def input = null
        def title = null
        def fileSelectionMode = null
        def dialogType = null
        def extensions = null
        def currentDirectory = null
        def onCancel = false
        FileUserInput userInput = null
        UserInputsCreator.instance.fileUserInputCreator = Mock(FileUserInput.ICreator)
        UserInputsCreator.instance.fileUserInputCreator.createFileUserInput( _ as Map) >> { Map map ->
            userInput = new FileUserInput.Creator().createFileUserInput(map)
            IDialogHelper dialogHelper = Mock(IDialogHelper)
            dialogHelper.createAndShowFileChooserDialog( _ , _ , _ , _ , _ ) >> { t, m, d, e, f ->
                title = t
                fileSelectionMode = m
                dialogType = d
                extensions = e
                currentDirectory = f
                return file
            }
            userInput.dialogHelper = dialogHelper

            return userInput
        }

        when:
        project.plugins.apply('de.andycandy.gradle.user_input')
        project.userInput.createFileUserInput title: 'title',
                onInput: { input = it }, onCancel: { onCancel = true },
                fileSelectionMode: project.userInput.directoriesOnly,
                dialogType: project.userInput.saveDialog

        then:
        input == file
        title == 'title'
        fileSelectionMode == JFileChooser.DIRECTORIES_ONLY
        dialogType == JFileChooser.SAVE_DIALOG
        extensions == []
        currentDirectory == null
        !onCancel
        userInput.title == 'title'
        userInput.condition()
        userInput.fileSelectionMode == JFileChooser.DIRECTORIES_ONLY
        userInput.dialogType == JFileChooser.SAVE_DIALOG
        userInput.currentDirectory == null
    }

    def 'plugin extension create file user input with extension'() {
        given:
        def project = ProjectBuilder.builder().build()
        def file = new File('./test.txt')
        def input = null
        def title = null
        def fileSelectionMode = null
        def dialogType = null
        def extensions = null
        def currentDirectory = null
        def onCancel = false
        FileUserInput userInput = null
        UserInputsCreator.instance.fileUserInputCreator = Mock(FileUserInput.ICreator)
        UserInputsCreator.instance.fileUserInputCreator.createFileUserInput( _ as Map) >> { Map map ->
            userInput = new FileUserInput.Creator().createFileUserInput(map)
            IDialogHelper dialogHelper = Mock(IDialogHelper)
            dialogHelper.createAndShowFileChooserDialog( _ , _ , _ , _ , _ ) >> { t, m, d, e, f ->
                title = t
                fileSelectionMode = m
                dialogType = d
                extensions = e
                currentDirectory = f
                return file
            }
            userInput.dialogHelper = dialogHelper

            return userInput
        }

        when:
        project.plugins.apply('de.andycandy.gradle.user_input')
        project.userInput.createFileUserInput title: 'title',
                onInput: { input = it }, onCancel: { onCancel = true },
                extension: 'asd'

        then:
        input == file
        title == 'title'
        fileSelectionMode == JFileChooser.FILES_ONLY
        dialogType == JFileChooser.OPEN_DIALOG
        extensions == ['asd']
        currentDirectory == null
        !onCancel
        userInput.title == 'title'
        userInput.condition()
        userInput.fileSelectionMode == JFileChooser.FILES_ONLY
        userInput.dialogType == JFileChooser.OPEN_DIALOG
        userInput.currentDirectory == null
    }

    def 'plugin extension create file user input with extensions'() {
        given:
        def project = ProjectBuilder.builder().build()
        def file = new File('./test.txt')
        def input = null
        def title = null
        def fileSelectionMode = null
        def dialogType = null
        def extensions = null
        def currentDirectory = null
        def onCancel = false
        FileUserInput userInput = null
        UserInputsCreator.instance.fileUserInputCreator = Mock(FileUserInput.ICreator)
        UserInputsCreator.instance.fileUserInputCreator.createFileUserInput( _ as Map) >> { Map map ->
            userInput = new FileUserInput.Creator().createFileUserInput(map)
            IDialogHelper dialogHelper = Mock(IDialogHelper)
            dialogHelper.createAndShowFileChooserDialog( _ , _ , _ , _ , _ ) >> { t, m, d, e, f ->
                title = t
                fileSelectionMode = m
                dialogType = d
                extensions = e
                currentDirectory = f
                return file
            }
            userInput.dialogHelper = dialogHelper

            return userInput
        }

        when:
        project.plugins.apply('de.andycandy.gradle.user_input')
        project.userInput.createFileUserInput title: 'title',
                onInput: { input = it }, onCancel: { onCancel = true },
                extension: 'asd', extensions: ['123', 'a1']

        then:
        input == file
        title == 'title'
        fileSelectionMode == JFileChooser.FILES_ONLY
        dialogType == JFileChooser.OPEN_DIALOG
        extensions == ['123', 'a1', 'asd']
        currentDirectory == null
        !onCancel
        userInput.title == 'title'
        userInput.condition()
        userInput.fileSelectionMode == JFileChooser.FILES_ONLY
        userInput.dialogType == JFileChooser.OPEN_DIALOG
        userInput.currentDirectory == null
    }

    def 'plugin extension create file user input with currentDirectory'() {
        given:
        def project = ProjectBuilder.builder().build()
        def file = new File('./test.txt')
        def dir = new File('dir')
        def input = null
        def title = null
        def fileSelectionMode = null
        def dialogType = null
        def extensions = null
        def currentDirectory = null
        def onCancel = false
        FileUserInput userInput = null
        UserInputsCreator.instance.fileUserInputCreator = Mock(FileUserInput.ICreator)
        UserInputsCreator.instance.fileUserInputCreator.createFileUserInput( _ as Map) >> { Map map ->
            userInput = new FileUserInput.Creator().createFileUserInput(map)
            IDialogHelper dialogHelper = Mock(IDialogHelper)
            dialogHelper.createAndShowFileChooserDialog( _ , _ , _ , _ , _ ) >> { t, m, d, e, f ->
                title = t
                fileSelectionMode = m
                dialogType = d
                extensions = e
                currentDirectory = f
                return file
            }
            userInput.dialogHelper = dialogHelper

            return userInput
        }

        when:
        project.plugins.apply('de.andycandy.gradle.user_input')
        project.userInput.createFileUserInput title: 'title',
                onInput: { input = it }, onCancel: { onCancel = true },
                currentDirectory: dir

        then:
        input == file
        title == 'title'
        fileSelectionMode == JFileChooser.FILES_ONLY
        dialogType == JFileChooser.OPEN_DIALOG
        extensions == []
        currentDirectory == dir
        !onCancel
        userInput.title == 'title'
        userInput.condition()
        userInput.fileSelectionMode == JFileChooser.FILES_ONLY
        userInput.dialogType == JFileChooser.OPEN_DIALOG
        userInput.currentDirectory == dir
    }


    def 'plugin extension create file user input wit onCancel'() {
        given:
        def project = ProjectBuilder.builder().build()
        def file = new File('./test.txt')
        def onInput = false
        def title = null
        def fileSelectionMode = null
        def dialogType = null
        def extensions = null
        def currentDirectory = null
        def onCancel = false
        FileUserInput userInput = null
        UserInputsCreator.instance.fileUserInputCreator = Mock(FileUserInput.ICreator)
        UserInputsCreator.instance.fileUserInputCreator.createFileUserInput( _ as Map) >> { Map map ->
            userInput = new FileUserInput.Creator().createFileUserInput(map)
            IDialogHelper dialogHelper = Mock(IDialogHelper)
            dialogHelper.createAndShowFileChooserDialog( _ , _ , _ , _ , _ ) >> { t, m, d, e, f ->
                title = t
                fileSelectionMode = m
                dialogType = d
                extensions = e
                currentDirectory = f
                throw new CancelException()
            }
            userInput.dialogHelper = dialogHelper

            return userInput
        }

        when:
        project.plugins.apply('de.andycandy.gradle.user_input')
        project.userInput.createFileUserInput title: 'title',
                onInput: { onInput = true }, onCancel: { onCancel = true }

        then:
        !onInput
        title == 'title'
        fileSelectionMode == JFileChooser.FILES_ONLY
        dialogType == JFileChooser.OPEN_DIALOG
        extensions == []
        currentDirectory == null
        onCancel
        userInput.title == 'title'
        userInput.condition()
        userInput.fileSelectionMode == JFileChooser.FILES_ONLY
        userInput.dialogType == JFileChooser.OPEN_DIALOG
        userInput.currentDirectory == null
    }

    def 'plugin extension create file user input condition false'() {
        given:
        def project = ProjectBuilder.builder().build()
        def onInput = false
        def show = false
        def onCancel = false
        FileUserInput userInput = null
        UserInputsCreator.instance.fileUserInputCreator = Mock(FileUserInput.ICreator)
        UserInputsCreator.instance.fileUserInputCreator.createFileUserInput( _ as Map) >> { Map map ->
            userInput = new FileUserInput.Creator().createFileUserInput(map)
            IDialogHelper dialogHelper = Mock(IDialogHelper)
            dialogHelper.createAndShowFileChooserDialog( _ , _ , _ , _ , _ ) >> { t, m, d, e, f ->
                show = true
                return null
            }
            userInput.dialogHelper = dialogHelper

            return userInput
        }

        when:
        project.plugins.apply('de.andycandy.gradle.user_input')
        project.userInput.createFileUserInput title: 'title', condition: { false },
                onInput: { onInput = true }, onCancel: { onCancel = true }

        then:
        !onInput
        !show
        !onCancel
        userInput.title == 'title'
        userInput.condition() == false
        userInput.fileSelectionMode == JFileChooser.FILES_ONLY
        userInput.dialogType == JFileChooser.OPEN_DIALOG
        userInput.currentDirectory == null
    }

    def 'plugin extension create text file input unknown parameters'() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        project.plugins.apply('de.andycandy.gradle.user_input')
        project.userInput.createFileUserInput condition: { false }, unknown1: 'unknown1', unknown2: 'unknown2'

        then:
        final IllegalArgumentException exception = thrown()
        exception.message == "There are unknown parameters ${[unknown1: 'unknown1', unknown2: 'unknown2']}"
    }

    def cleanup() {
        UserInputsCreator.instance.textUserInputCreator = new TextUserInput.Creator()
        UserInputsCreator.instance.passwordUserInputCreator = new PasswordUserInput.Creator()
        UserInputsCreator.instance.dropdownUserInputCreator = new DropdownUserInput.Creator()
        UserInputsCreator.instance.yesNoUserInputCreator = new YesNoUserInput.Creator()
        UserInputsCreator.instance.fileUserInputCreator = new FileUserInput.Creator()
    }
}
