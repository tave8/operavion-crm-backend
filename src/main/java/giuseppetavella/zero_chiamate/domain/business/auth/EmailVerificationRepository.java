package giuseppetavella.zero_chiamate.domain.business.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerificationCode, UUID> {

    /**
     * For a code to be valid, it must not have expired, 
     * and it must not have been used.
     */
    @Query("""
        SELECT e
        FROM EmailVerificationCode e
        WHERE e.code = :code
          AND e.used = false
          AND e.createdAt >= :validSince
    """)
    Optional<EmailVerificationCode> getCode(UUID code, OffsetDateTime validSince);
    
}
