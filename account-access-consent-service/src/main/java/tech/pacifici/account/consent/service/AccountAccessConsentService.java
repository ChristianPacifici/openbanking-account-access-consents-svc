package tech.pacifici.account.consent.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.pacifici.account.consent.domain.AccountAccessConsent;
import tech.pacifici.account.consent.domain.InvalidRequestException;
import tech.pacifici.account.consent.domain.ResourceNotFoundException;
import tech.pacifici.account.consent.repository.AccountAccessConsentRepository;
import tech.pacifici.model.OBReadConsentResponse5;
import tech.pacifici.model.OBReadConsentResponse5Data;
import tech.pacifici.model.OBWriteDomesticConsent4;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service layer for managing account access consents.
 * It contains the business logic for creating, retrieving, and deleting consents.
 * It also handles the validation of incoming requests and manages consent states.
 */
@Service
@RequiredArgsConstructor
public class AccountAccessConsentService {

    private final AccountAccessConsentRepository repository;

    /**
     * Creates and stores a new account access consent.
     * This method validates the request body against business rules before creating the consent.
     *
     * @param requestBody The request body containing the consent details.
     * @return The response object for the created consent.
     */
    public OBReadConsentResponse5 createConsent(OBWriteDomesticConsent4 requestBody) {
        // Business Logic: Validate the request before creating the consent.
        validateCreateConsentRequest(requestBody);

        // Business Logic: Generate a unique, immutable consent ID.
        String consentId = "ACC-" + UUID.randomUUID().toString();

        // Map the request to a JPA entity.
        AccountAccessConsent newConsent = new AccountAccessConsent();
        newConsent.setConsentId(consentId);
        // Business Logic: Set initial status based on Open Banking UK rules.
        newConsent.setStatus("AwaitingAuthorisation");
        newConsent.setCreationDateTime(OffsetDateTime.now());
        newConsent.setStatusUpdateDateTime(OffsetDateTime.now());
        newConsent.setExpirationDateTime(requestBody.getData().getExpirationDateTime());

        // Assuming permissions are a list of strings
        String permissions = requestBody.getData().getPermissions().stream()
                .map(Enum::toString)
                .collect(Collectors.joining(","));
        newConsent.setPermissions(permissions);

        // Storing the full request body as JSON for immutability and record-keeping.
        newConsent.setRequestBody(requestBody.toString());

        // Save the new consent to the database.
        repository.save(newConsent);

        // Map the created entity back to the response DTO.
        OBReadConsentResponse5 response = new OBReadConsentResponse5();
        OBReadConsentResponse5Data
                responseData = new OBReadConsentResponse5Data
                ();
        responseData.setConsentId(newConsent.getConsentId());
        responseData.setStatus(OBReadConsentResponse5Data
                .StatusEnum.AWAITING_AUTHORISATION);
        responseData.setCreationDateTime(newConsent.getCreationDateTime());
        responseData.setStatusUpdateDateTime(newConsent.getStatusUpdateDateTime());
        responseData.setExpirationDateTime(newConsent.getExpirationDateTime());

        List<OBReadConsentResponse5Data
                .PermissionsEnum> permissionsList =
                requestBody.getData().getPermissions().stream()
                        .map(p -> OBReadConsentResponse5Data
                                .PermissionsEnum.fromValue(p.getValue()))
                        .collect(Collectors.toList());
        responseData.setPermissions(permissionsList);

        response.setData(responseData);

        return response;
    }

    /**
     * Retrieves an account access consent by its ID.
     *
     * @param consentId The ID of the consent to retrieve.
     * @return The response object for the found consent.
     * @throws ResourceNotFoundException if the consent is not found.
     */
    public OBReadConsentResponse5 getConsentById(String consentId) {
        return repository.findById(consentId)
                .map(this::mapEntityToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Consent not found with ID: " + consentId));
    }

    /**
     * Deletes an account access consent by its ID.
     * Business Logic: This method supports the revocation of a consent.
     *
     * @param consentId The ID of the consent to delete.
     * @throws ResourceNotFoundException if the consent is not found.
     */
    public void deleteConsentById(String consentId) {
        if (!repository.existsById(consentId)) {
            throw new ResourceNotFoundException("Consent not found with ID: " + consentId);
        }
        repository.deleteById(consentId);
    }

    /**
     * Helper method to map a JPA entity to the OpenAPI response DTO.
     *
     * @param entity The AccountAccessConsent entity.
     * @return The OBReadConsentResponse5 DTO.
     */
    private OBReadConsentResponse5 mapEntityToResponse(AccountAccessConsent entity) {
        OBReadConsentResponse5 response = new OBReadConsentResponse5();
        OBReadConsentResponse5Data
                responseData = new OBReadConsentResponse5Data
                ();
        responseData.setConsentId(entity.getConsentId());
        responseData.setStatus(OBReadConsentResponse5Data
                .StatusEnum.fromValue(entity.getStatus()));
        responseData.setCreationDateTime(entity.getCreationDateTime());
        responseData.setStatusUpdateDateTime(entity.getStatusUpdateDateTime());
        responseData.setExpirationDateTime(entity.getExpirationDateTime());

        List<OBReadConsentResponse5Data
                .PermissionsEnum> permissions =
                Arrays.stream(entity.getPermissions().split(","))
                        .map(OBReadConsentResponse5Data
                                .PermissionsEnum::fromValue)
                        .collect(Collectors.toList());
        responseData.setPermissions(permissions);

        response.setData(responseData);

        return response;
    }

    /**
     * Private helper method to validate the incoming request for creating a consent.
     * This ensures that required fields are present and valid according to business rules.
     *
     * @param requestBody The request body to validate.
     * @throws InvalidRequestException if the request is not valid.
     */
    private void validateCreateConsentRequest(OBWriteDomesticConsent4 requestBody) {
        if (requestBody == null || requestBody.getData() == null) {
            throw new InvalidRequestException("The request body and data field cannot be null.");
        }

        if (requestBody.getData().getExpirationDateTime() == null) {
            throw new InvalidRequestException("ExpirationDateTime is a mandatory field and must be provided.");
        }
        if (requestBody.getData().getExpirationDateTime().isBefore(OffsetDateTime.now())) {
            throw new InvalidRequestException("ExpirationDateTime must be in the future.");
        }

        if (requestBody.getData().getPermissions() == null || requestBody.getData().getPermissions().isEmpty()) {
            throw new InvalidRequestException("At least one permission must be provided.");
        }
    }
}
