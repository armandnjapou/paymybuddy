package com.project.paymebuddy.backend.controllers;

import com.project.paymebuddy.backend.exceptions.DomainException;
import com.project.paymebuddy.backend.exceptions.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ServerWebInputException;

import java.time.LocalDateTime;

import static com.project.paymebuddy.backend.exceptions.DomainException.Code.BAD_REQUEST;
import static com.project.paymebuddy.backend.utils.Constants.CODE_400;

@ControllerAdvice
public class ControllerExceptionHandler {

    private static final String FATAL = "Fatal";
    private static final String LOGIC = "Logic";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleGenericException(Exception exception, WebRequest request) {

        ErrorMessage message = new ErrorMessage(
                FATAL,
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                exception.getMessage(),
                request.getDescription(false)
        );

        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorMessage> globalExceptionHandler(DomainException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(ex.getSeverity().getText(),
                ex.getCode().getValue(),
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );

        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<ErrorMessage> handleServletException(Exception ex) {
        ErrorMessage message = new ErrorMessage(LOGIC,
                CODE_400,
                LocalDateTime.now(),
                ex.getMessage(),
                BAD_REQUEST.getText()
        );

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}
