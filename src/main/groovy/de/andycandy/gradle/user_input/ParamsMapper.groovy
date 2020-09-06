package de.andycandy.gradle.user_input

import groovy.transform.CompileDynamic

@CompileDynamic
class ParamsMapper {

    private final Map params

    ParamsMapper(Map params) {
        this.params = params.clone()
    }

    void mapParam(String key, Closure mapper) {
        mapParamIfNotNull(key, mapper)
    }

    void mapParamWithFallback(String key, Object fallback, Closure mapper) {
        if (!mapParamIfNotNull(key, mapper)) {
            mapper.call(fallback)
        }
    }

    void checkForUnknownParams() {
        if (!params.isEmpty()) {
            throw new IllegalArgumentException("There are unknown parameters: $params")
        }
    }

    private boolean mapParamIfNotNull(String key, Closure mapper) {
        def value = params.remove(key)
        if (value != null) {
            mapper.call(value)
            return true
        }
        return false
    }

    static void map(Map params, @DelegatesTo(value = ParamsMapper, strategy = Closure.DELEGATE_FIRST) Closure closure) {

        ParamsMapper paramsMapper = new ParamsMapper(params)

        closure.delegate = paramsMapper
        closure.resolveStrategy = Closure.DELEGATE_FIRST

        closure.call()
    }
}
