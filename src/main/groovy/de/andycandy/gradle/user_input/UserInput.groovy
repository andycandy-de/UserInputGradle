package de.andycandy.gradle.user_input

import groovy.transform.CompileStatic

@CompileStatic
abstract class UserInput {
	
	String title
	
	Closure condition
	
	Closure onCancel
	
	boolean evaluateCondition() {
		return condition.call()
	}
	
	void executeIfConditionTrue() {
		if (evaluateCondition()) {
			execute()
		}
	}
	
	abstract void execute()
}
