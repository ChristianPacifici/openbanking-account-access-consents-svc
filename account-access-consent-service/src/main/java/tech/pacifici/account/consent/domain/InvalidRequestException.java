package tech.pacifici.account.consent.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception to be thrown when the request body is invalid.
 * The @ResponseStatus annotation tells Spring to return an HTTP 400 (Bad Request)
 * status code when this exception is thrown.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException(String message) {
        super(message);
    }
}
