package com.fraud.detection.exception;

import graphql.GraphQLError;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

    @GraphQlExceptionHandler
    public GraphQLError handleBookError(FraudException exceptionHandler) {
        return GraphQLError.newError()
                .message(exceptionHandler.getMessage())
                .build();
    }
}
