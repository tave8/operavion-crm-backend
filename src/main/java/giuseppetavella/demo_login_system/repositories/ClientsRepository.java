package giuseppetavella.demo_login_system.repositories;

import giuseppetavella.demo_login_system.entities.Client;
import giuseppetavella.demo_login_system.entities.Company;
import giuseppetavella.demo_login_system.entities.Notification;
import giuseppetavella.demo_login_system.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ClientsRepository extends JpaRepository<Client, UUID> {

    /**
     * Find clients of the given company.
     */
    @Query("""

        SELECT c
        FROM Client c
        WHERE
            c.company = :company

    """)
    Page<Client> findClientsByCompany(
            Company company,
            Pageable pageable
    );

}
