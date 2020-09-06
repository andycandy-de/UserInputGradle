# UserInputGradle

https://plugins.gradle.org/plugin/de.andycandy.gradle.user_input
```
plugins {
  id "de.andycandy.gradle.user_input" version "1.0.1"
}
```

This plugin adds user input dialogs to a gradle build.

For example to pass git credentials to the build.
Grgit example:
```
task gitLoginInput (type: UserInputTask) {
    onlyIf { // DISABLE USER INPUT (e.g. CI)
        def userInput = project.findProperty('userInput')
        userInput != null ? Boolean.valueOf(userInput) : true
    }

    group 'configuration'
    description 'This task shows an user input if git credentials are missing'

    createTextUserInput title: 'Git User', message: 'Please enter git user',
            condition: { !System.getenv().containsKey('GRGIT_USER') && System.getProperty('org.ajoberstar.grgit.auth.username') == null },
            onInput: { input -> System.setProperty('org.ajoberstar.grgit.auth.username', input) },
            onCancel: { throw new GradleException('No Git User!') }

    createPasswordUserInput title: 'Git Password', message: 'Please enter git password',
            condition: { !System.getenv().containsKey('GRGIT_PASS') && System.getProperty('org.ajoberstar.grgit.auth.password') == null },
            onInput: { input -> System.setProperty('org.ajoberstar.grgit.auth.password', input) },
            onCancel: { throw new GradleException('No Git Password!') }
}

task pull () {
    dependsOn(gitLoginInput)

    doLast {
        grgit.pull()
    }
}
```
