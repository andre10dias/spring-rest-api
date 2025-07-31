package com.github.andre10dias.spring_rest_api.exception;

import java.time.LocalDate;

public record ExceptionResponse(
        LocalDate timestamp,
        String message,
        String details
) {
}
