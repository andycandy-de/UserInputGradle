package de.andycandy.gradle.user_input

import spock.lang.Specification

class ParamsToObjectMapperTest extends Specification {

    def 'test map param and empty map'() {
        given:
        AnyClass anyClass = new AnyClass()
        Map params = [:]

        when:
        ParamsToObjectMapper.map(params, anyClass) {
            mapParam('anyString') { String s -> anyClass.anyString = s }
            mapParam('anyInteger') { Integer i -> anyClass.anyInteger = i }
        }

        then:
        !anyClass.anyString
        !anyClass.anyInteger
    }

    def 'test map param with empty map'() {
        given:
        AnyClass anyClass = new AnyClass()
        Map params = [:]

        when:
        ParamsToObjectMapper.map(params, anyClass) {
            mapParam('anyString')
            mapParam('anyInteger')
        }

        then:
        !anyClass.anyString
        !anyClass.anyInteger
    }

    def 'test map param'() {
        given:
        AnyClass anyClass = new AnyClass()
        Map params = [anyString: 'asd', anyInteger: 123]

        when:
        ParamsToObjectMapper.map(params, anyClass) {
            mapParam('anyString')
            mapParam('anyInteger')
        }

        then:
        anyClass.anyString == 'asd'
        anyClass.anyInteger == 123
    }

    def 'test map param with fallBack and empty map'() {
        given:
        AnyClass anyClass = new AnyClass()
        Map params = [:]

        when:
        ParamsToObjectMapper.map(params, anyClass) {
            mapParamWithFallback('anyString', 'dsa')
            mapParamWithFallback('anyInteger', 321)
        }

        then:
        anyClass.anyString == 'dsa'
        anyClass.anyInteger == 321
    }

    def 'test map param with fallBack'() {
        given:
        AnyClass anyClass = new AnyClass()
        Map params = [anyString: 'asd', anyInteger: 123]

        when:
        ParamsToObjectMapper.map(params, anyClass) {
            mapParamWithFallback('anyString', 'dsa')
            mapParamWithFallback('anyInteger', 321)
        }

        then:
        anyClass.anyString == 'asd'
        anyClass.anyInteger == 123
    }

    def 'test map param with unknown'() {
        given:
        AnyClass anyClass = new AnyClass()
        Map params = [unknown: 'unknown']

        when:
        ParamsToObjectMapper.map(params, anyClass) {
            mapParam('anyString')
            mapParam('anyInteger')
            checkForUnknownParams()
        }

        then:
        final IllegalArgumentException exception = thrown()
        exception.message == "There are unknown parameters: $params"
    }

    class AnyClass {

        String anyString

        Integer anyInteger
    }
}
