# UserInputGradle
This plugin adds user input dialogs to a gradle build.

For example to pass git credentials to the build.
Grgit example:
```
task gitLoginInput (type: UserInputTask) {
    onlyIf { project.findProject('userInput') ?: true } // DISABLE USER INPUT (e.g. CI)

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
