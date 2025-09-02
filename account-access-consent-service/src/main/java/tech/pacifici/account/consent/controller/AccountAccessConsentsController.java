package tech.pacifici.account.consent.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import tech.pacifici.api.AccountAccessConsentsApi;
import tech.pacifici.account.consent.domain.InvalidRequestException;
import tech.pacifici.account.consent.service.AccountAccessConsentService;
import tech.pacifici.model.OBReadConsentResponse5;
import tech.pacifici.model.OBWriteDomesticConsent4;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * Spring Boot controller to implement the AccountAccessConsentsApi.
 * It uses a service layer to handle the business logic and interact with the data layer.
 *
 * The @RestController annotation combines @Controller and @ResponseBody,
 * making it a RESTful controller that returns JSON/XML responses.
 */
@RestController
@RequiredArgsConstructor
public class AccountAccessConsentsController implements AccountAccessConsentsApi {

    private final AccountAccessConsentService service;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return AccountAccessConsentsApi.super.getRequest();
    }

    /**
     * Handles the POST /account-access-consents endpoint to create a new consent.
     * @param obWriteDomesticConsent4 The request body for the new consent.
     * @return A ResponseEntity containing the created consent and HTTP status 201.
     */
    @Override
    public ResponseEntity<OBReadConsentResponse5> createAccountAccessConsents(OBWriteDomesticConsent4 obWriteDomesticConsent4) {
        // The service layer now handles validation and throws an exception on invalid requests.
        OBReadConsentResponse5 createdConsent = service.createConsent(obWriteDomesticConsent4);
        return new ResponseEntity<>(createdConsent, HttpStatus.CREATED);
    }

    /**
     * Handles the DELETE /account-access-consents/{ConsentId} endpoint to delete a consent.
     * @param consentId The ID of the consent to delete.
     * @param xFapiFinancialId The financial ID header (required by the API).
     * @param xFapiInteractionId The interaction ID header (optional).
     * @return A ResponseEntity with HTTP status 204 (No Content).
     */
    @Override
    public ResponseEntity<Void> deleteAccountAccessConsentsConsentId(String consentId, String xFapiFinancialId, String xFapiInteractionId) {
        service.deleteConsentById(consentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Handles the GET /account-access-consents/{ConsentId} endpoint to retrieve a consent.
     * @param consentId The ID of the consent to retrieve.
     * @param xFapiFinancialId The financial ID header (required by the API).
     * @param xFapiInteractionId The interaction ID header (optional).
     * @return A ResponseEntity containing the consent and HTTP status 200, or 404 if not found.
     */
    @Override
    public ResponseEntity<OBReadConsentResponse5> getAccountAccessConsentsConsentId(String consentId, String xFapiFinancialId, String xFapiInteractionId) {
        OBReadConsentResponse5 consent = service.getConsentById(consentId);
        return new ResponseEntity<>(consent, HttpStatus.OK);
    }

    /**
     * Handles InvalidRequestException and returns a 400 Bad Request status with an error message.
     * This is a local exception handler for this controller.
     *
     * @param ex The InvalidRequestException that was thrown.
     * @return A ResponseEntity with an error message and a 400 HTTP status.
     */
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Map<String, String>> handleInvalidRequestException(InvalidRequestException ex) {
        Map<String, String> errorResponse = Collections.singletonMap("error", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
