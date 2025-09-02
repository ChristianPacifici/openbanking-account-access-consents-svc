package tech.pacifici.account.consent.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.OffsetDateTime;

/**
 * JPA Entity representing an Account Access Consent.
 * This class maps to the `account_access_consents` table in the PostgreSQL database.
 * The getter and setter methods have been explicitly defined instead of using @Data,
 * to prevent any possible build issues.
 */
@Entity
@Table(name = "account_access_consents")
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class AccountAccessConsent {

    @Id
    @Column(name = "consent_id")
    private String consentId;

    private String status;

    @Column(name = "creation_date_time")
    private OffsetDateTime creationDateTime;

    @Column(name = "status_update_date_time")
    private OffsetDateTime statusUpdateDateTime;

    @Column(name = "expiration_date_time")
    private OffsetDateTime expirationDateTime;

    // Storing as a comma-separated string for simplicity.
    // In a production environment, you might use a separate table or a custom converter.
    private String permissions;

    @Column(name = "request_body", columnDefinition = "TEXT")
    private String requestBody; // Storing the full request body as JSON

    public String getConsentId() {
        return consentId;
    }

    public void setConsentId(String consentId) {
        this.consentId = consentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OffsetDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(OffsetDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public OffsetDateTime getStatusUpdateDateTime() {
        return statusUpdateDateTime;
    }

    public void setStatusUpdateDateTime(OffsetDateTime statusUpdateDateTime) {
        this.statusUpdateDateTime = statusUpdateDateTime;
    }

    public OffsetDateTime getExpirationDateTime() {
        return expirationDateTime;
    }

    public void setExpirationDateTime(OffsetDateTime expirationDateTime) {
        this.expirationDateTime = expirationDateTime;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }
}
