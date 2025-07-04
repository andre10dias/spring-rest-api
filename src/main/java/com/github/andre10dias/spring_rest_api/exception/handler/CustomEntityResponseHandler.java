package com.github.andre10dias.spring_rest_api.exception.handler;

import com.github.andre10dias.spring_rest_api.exception.ExceptionResponse;
import com.github.andre10dias.spring_rest_api.exception.InvalidPageRequestException;
import com.github.andre10dias.spring_rest_api.exception.RequiredObjectIsNullException;
import com.github.andre10dias.spring_rest_api.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDate;

@RestController
@ControllerAdvice
public class CustomEntityResponseHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllException(Exception ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDate.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleNotFoundException(Exception ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDate.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({RequiredObjectIsNullException.class, InvalidPageRequestException.class})
    public final ResponseEntity<ExceptionResponse> handleBadRequestException(Exception ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDate.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
