package com.example.demo.exception;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomHttpException.class)
    public void handleCustomHttpException(HttpServletResponse response, CustomHttpException httpException)
            throws IOException {
        response.sendError(httpException.getHttpStatus().value(), httpException.getMessage());
    }
}

