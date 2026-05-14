package giuseppetavella.demo_login_system.repositories;

import giuseppetavella.demo_login_system.entities.clients.Client;
import giuseppetavella.demo_login_system.entities.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ClientsRepository extends JpaRepository<Client, UUID> {

    /**
     * Find clients of the given company.
     * 
     * If we specificy a legal name pattern: either the legal name pattern exists (is not null)
     * or it exists and it matches the legal name of a client
     */
    @Query("""

        SELECT c
        FROM Client c
        WHERE
            c.company = :company
            AND (
                :legalNamePattern IS NULL
                OR LOWER(c.legalName) LIKE :legalNamePattern
            )

    """)
    Page<Client> findClientsByCompany(
            Company company,
            String legalNamePattern,
            Pageable pageable
    );

}
