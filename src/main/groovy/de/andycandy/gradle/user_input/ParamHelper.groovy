package de.andycandy.gradle.user_input

import groovy.transform.CompileDynamic

@CompileDynamic
class ParamHelper {

    private Map params

    private Object object

    void mapParam(String key) {
        def value = params.remove(key)
        if (value != null) {
            object."$key" = value
        }
    }

    void mapParamWithFallback(String key, Object fallBack) {
        def value = params.remove(key)
        if (value != null) {
            object."$key" = value
        }
        else {
            object."$key" = fallBack
        }
    }

    void mapParam(String key, Closure mapper) {
        def value = params.remove(key)
        if (value != null) {
            mapper.call(value)
        }
    }

    void mapParamWithFallback(String key, Object fallBack, Closure mapper) {
        def value = params.remove(key)
        if (value != null) {
            mapper.call(value)
        }
        else {
            mapper.call(fallBack)
        }
    }

    static void create(Map params, Object object, @DelegatesTo(value = ParamHelper, strategy = Closure.DELEGATE_FIRST) Closure closure) {
        ParamHelper paramHelper = new ParamHelper()
        paramHelper.params = params.clone()
        paramHelper.object = object

        closure.delegate = paramHelper
        closure.resolveStrategy = Closure.DELEGATE_FIRST

        closure.call()

        if (!paramHelper.params.isEmpty()) {
            throw new IllegalArgumentException("There are unknown parameters: ${paramHelper.params}")
        }
    }
}
