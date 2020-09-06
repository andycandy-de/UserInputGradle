package de.andycandy.gradle.user_input

import groovy.transform.CompileDynamic

@CompileDynamic
class ParamsToObjectMapper extends ParamsMapper {

    private final Object object

    ParamsToObjectMapper(Map params, Object object) {
        super(params)
        this.object = object
    }

    void mapParam(String key) {
        super.mapParam(key) { val -> object."$key" = val }
    }

    void mapParamWithFallback(String key, Object fallback) {
        super.mapParamWithFallback(key, fallback) { val -> object."$key" = val }
    }

    static void map(Map params, Object object, @DelegatesTo(value = ParamsToObjectMapper, strategy = Closure.DELEGATE_FIRST) Closure closure) {

        ParamsToObjectMapper paramsToObjectMapper = new ParamsToObjectMapper(params, object)

        closure.delegate = paramsToObjectMapper
        closure.resolveStrategy = Closure.DELEGATE_FIRST

        closure.call()
    }
}
