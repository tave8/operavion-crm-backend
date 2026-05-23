package giuseppetavella.demo_login_system.domain.entities.checklists;

import giuseppetavella.demo_login_system.domain.entities.companies.Company;
import giuseppetavella.demo_login_system.domain.entities.client_addresses.ClientAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ChecklistsRepository extends JpaRepository<Checklist, UUID> {


    /*
     * Find checklists.
     */
    @Query("""

        SELECT c
        FROM Checklist c
        WHERE
            c.company = :company
            AND (
                :searchQueryPattern IS NULL
                OR LOWER(c.name) LIKE :searchQueryPattern
            )

    """)
    Page<Checklist> findChecklists(
            Company company,
            String searchQueryPattern,
            Pageable pageable
    );


    /**
     * Find checklists by client address.
     * 
     * @param clientAddress
     * @return
     */
    @Query("""

        SELECT DISTINCT c
        FROM Checklist c
        WHERE 
            c IN (
                SELECT cac.checklist 
                FROM ClientAddressChecklist cac
                WHERE cac.clientAddress = :clientAddress    
            )

    """)
    List<Checklist> findChecklistsByClientAddress(
            ClientAddress clientAddress
    );

}
