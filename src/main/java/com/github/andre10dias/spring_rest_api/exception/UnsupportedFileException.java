package com.github.andre10dias.spring_rest_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnsupportedFileException extends RuntimeException {

    public UnsupportedFileException() {
        super("Unsupported file type!");
    }

    public UnsupportedFileException(String message) {
        super(message);
    }

}
