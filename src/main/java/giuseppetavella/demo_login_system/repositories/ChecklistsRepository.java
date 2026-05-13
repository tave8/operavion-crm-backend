package giuseppetavella.demo_login_system.repositories;

import giuseppetavella.demo_login_system.entities.Checklist;
import giuseppetavella.demo_login_system.entities.Company;
import giuseppetavella.demo_login_system.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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


}
