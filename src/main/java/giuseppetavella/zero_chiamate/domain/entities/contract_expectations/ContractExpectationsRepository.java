package giuseppetavella.zero_chiamate.domain.entities.contract_expectations;

import giuseppetavella.zero_chiamate.domain.entities.client_addresses.ClientAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContractExpectationsRepository extends JpaRepository<ContractExpectation, UUID> {

    /**
     * Find contract expectation by client address.
     * 
     * @param clientAddress
     * @return
     */
    @Query("""

        SELECT 
            ce
        FROM
            ContractExpectation ce
        WHERE 
            ce.clientAddress = :clientAddress

    """)
    Optional<ContractExpectation> findByClientAddress(ClientAddress clientAddress);

    
    /**
     * Contract expectation exists by client address?
     *
     */
    @Query("""

        SELECT 
            EXISTS (
                SELECT 
                    ce
                FROM
                    ContractExpectation ce
                WHERE 
                    ce.clientAddress = :clientAddress
            )

    """)
    boolean existsByClientAddress(ClientAddress clientAddress);
    
}
