package com.github.andre10dias.spring_rest_api.exception.handler;

import com.github.andre10dias.spring_rest_api.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDate;

@RestController
@ControllerAdvice
public class CustomEntityResponseHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({Exception.class, FileStorageException.class, FileExportException.class})
    public final ResponseEntity<ExceptionResponse> handleInternalServerException(Exception ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDate.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR); // 500
    }

    @ExceptionHandler({ResourceNotFoundException.class, FileNotFoundException.class, UnsupportedFileException.class})
    public final ResponseEntity<ExceptionResponse> handleNotFoundException(Exception ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDate.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // 404
    }

    @ExceptionHandler({
            RequiredObjectIsNullException.class,
            InvalidPageRequestException.class,
            FileNotProvidedException.class,
            InvalidCredentialsException.class
    })
    public final ResponseEntity<ExceptionResponse> handleBadRequestException(Exception ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDate.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // 400
    }

    @ExceptionHandler(EmailSendingException.class)
    public final ResponseEntity<ExceptionResponse> handleUnprocessableEntityException(
            Exception ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDate.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY); // 422
    }

    @ExceptionHandler(InvalidJwtAuthenticationException.class)
    public final ResponseEntity<ExceptionResponse> handleForbiddenException(
            Exception ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDate.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN); // 403
    }

    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public final ResponseEntity<ExceptionResponse> handleUnauthorizedException(
            Exception ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDate.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED); // 401
    }

}
