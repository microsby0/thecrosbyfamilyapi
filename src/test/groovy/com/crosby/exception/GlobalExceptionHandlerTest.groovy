package com.crosby.exception

import org.springframework.http.HttpStatus
import spock.lang.Specification

import javax.persistence.EntityNotFoundException

class GlobalExceptionHandlerTest extends Specification {
    GlobalExceptionHandler exceptionHandler

    void setup() {
        exceptionHandler = new GlobalExceptionHandler()
    }

    def "EntityNotFoundHandler"() {
        given:
            def message = "no found"
            def exception = new EntityNotFoundException(message)
        when:
            def response = exceptionHandler.entityNotFoundHandler(exception)
        then:
            response.getStatusCode() == HttpStatus.NOT_FOUND
            response.getBody() == message
    }

    def "GenericExceptionHandler"() {
        given:
            def exception = new Exception()
        when:
            def response = exceptionHandler.genericExceptionHandler(exception)
        then:
            response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR
            response.getBody() == "An unexpected error has occurred"
    }
}
