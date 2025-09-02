package tech.pacifici.account.consent.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception to be thrown when a requested resource is not found.
 * The @ResponseStatus annotation tells Spring to return an HTTP 404 (Not Found)
 * status code when this exception is thrown.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
