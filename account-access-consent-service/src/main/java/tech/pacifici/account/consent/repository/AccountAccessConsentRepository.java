package tech.pacifici.account.consent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.pacifici.account.consent.domain.AccountAccessConsent;

/**
 * Spring Data JPA Repository for the AccountAccessConsent entity.
 * It provides standard CRUD operations.
 */
@Repository
public interface AccountAccessConsentRepository extends JpaRepository<AccountAccessConsent, String> {
}
