package com.github.datnm23.acountservice.exception.handler;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.github.datnm23.acountservice.exception.*;
import com.github.datnm23.acountservice.model.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse("NOT_FOUND", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeignException(FeignException ex) {
        ErrorResponse errorResponse = new ErrorResponse("FEIGN_ERROR", "Error communicating with security service");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({
            ExistedUserException.class,
            InvalidRefreshTokenException.class,
            ObjectNotFoundException.class,
            ExpiredEmailActivationUrlException.class,
            ExpiredPasswordForgottenUrlException.class
    })
    public ResponseEntity<ErrorResponse> handleValidationExceptions(Exception ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .build();

        if (ex instanceof ExistedUserException || ex instanceof PasswordNotMatchedException) {
            errorResponse.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } else if (ex instanceof InvalidRefreshTokenException || ex instanceof ObjectNotFoundException) {
            errorResponse.setCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } else if (ex instanceof ExpiredEmailActivationUrlException || ex instanceof ExpiredPasswordForgottenUrlException) {
            errorResponse.setCode(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()));
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
        }

        errorResponse.setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

}
