package tech.pacifici.account.consent.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tech.pacifici.account.consent.domain.ResourceNotFoundException;
import tech.pacifici.account.consent.service.AccountAccessConsentService;
import tech.pacifici.model.OBReadConsentResponse5;
import tech.pacifici.model.OBReadConsentResponse5Data;
import tech.pacifici.model.OBWriteDomesticConsent4;
import tech.pacifici.model.OBWriteDomesticConsent4Data;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the AccountAccessConsentsController.
 * These tests focus on the controller's logic, ensuring it interacts correctly with
 * the service layer and returns the appropriate HTTP responses.
 */
@ExtendWith(MockitoExtension.class)
class AccountAccessConsentsControllerTest {

    @Mock
    private AccountAccessConsentService service;

    @InjectMocks
    private AccountAccessConsentsController controller;

    private OBReadConsentResponse5 mockConsentResponse;
    private OBWriteDomesticConsent4 mockConsentRequest;
    private String mockConsentId;
    private String xFapiFinancialId;

    @BeforeEach
    void setUp() {
        mockConsentId = "ACC-" + UUID.randomUUID();
        xFapiFinancialId = "some-financial-id";

        // Create a mock response object for successful GET and POST tests
        mockConsentResponse = new OBReadConsentResponse5();
        OBReadConsentResponse5Data
 data = new OBReadConsentResponse5Data
();
        data.setConsentId(mockConsentId);
        data.setStatus(OBReadConsentResponse5Data
.StatusEnum.AUTHORISED);
        data.setCreationDateTime(OffsetDateTime.now());
        data.setStatusUpdateDateTime(OffsetDateTime.now());
        data.setPermissions(Collections.emptyList());
        mockConsentResponse.setData(data);

        // Create a mock request object for successful POST tests
        mockConsentRequest = new OBWriteDomesticConsent4();
        OBWriteDomesticConsent4Data requestData = new OBWriteDomesticConsent4Data();
        requestData.setPermissions(Collections.emptyList());
        mockConsentRequest.setData(requestData);
    }

    @Test
    void testCreateAccountAccessConsents_Success() {
        // Arrange
        when(service.createConsent(any(OBWriteDomesticConsent4.class)))
                .thenReturn(mockConsentResponse);

        // Act
        ResponseEntity<OBReadConsentResponse5> response = controller.createAccountAccessConsents(mockConsentRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockConsentResponse, response.getBody());
        verify(service, times(1)).createConsent(any(OBWriteDomesticConsent4.class));
    }

    @Test
    void testGetAccountAccessConsentsConsentId_Success() {
        // Arrange
        when(service.getConsentById(mockConsentId)).thenReturn(mockConsentResponse);

        // Act
        ResponseEntity<OBReadConsentResponse5> response = controller.getAccountAccessConsentsConsentId(
                mockConsentId, xFapiFinancialId, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockConsentResponse, response.getBody());
        verify(service, times(1)).getConsentById(mockConsentId);
    }

    @Test
    void testGetAccountAccessConsentsConsentId_NotFound() {
        // Arrange
        when(service.getConsentById(mockConsentId))
                .thenThrow(new ResourceNotFoundException("Consent not found"));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            controller.getAccountAccessConsentsConsentId(mockConsentId, xFapiFinancialId, null);
        });

        verify(service, times(1)).getConsentById(mockConsentId);
    }

    @Test
    void testDeleteAccountAccessConsentsConsentId_Success() {
        // Arrange
        doNothing().when(service).deleteConsentById(mockConsentId);

        // Act
        ResponseEntity<Void> response = controller.deleteAccountAccessConsentsConsentId(
                mockConsentId, xFapiFinancialId, null);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service, times(1)).deleteConsentById(mockConsentId);
    }

    @Test
    void testDeleteAccountAccessConsentsConsentId_NotFound() {
        // Arrange
        doThrow(new ResourceNotFoundException("Consent not found"))
                .when(service).deleteConsentById(mockConsentId);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            controller.deleteAccountAccessConsentsConsentId(mockConsentId, xFapiFinancialId, null);
        });

        verify(service, times(1)).deleteConsentById(mockConsentId);
    }
}
