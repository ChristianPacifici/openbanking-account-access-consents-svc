package tech.pacifici.account.consent.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tech.pacifici.account.consent.domain.ResourceNotFoundException;

import java.util.Collections;
import java.util.Map;

/**
 * Global exception handler using @ControllerAdvice to handle exceptions
 * across the entire application.
 */
@ControllerAdvice
public class AccountAccessConsentExceptionHandler {

    /**
     * Handles the custom ResourceNotFoundException and returns a 404 Not Found status.
     *
     * @param ex The ResourceNotFoundException that was thrown.
     * @return A ResponseEntity with an error message and a 404 HTTP status.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, String> errorResponse = Collections.singletonMap("error", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
