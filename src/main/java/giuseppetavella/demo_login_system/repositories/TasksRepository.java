package giuseppetavella.demo_login_system.repositories;

import giuseppetavella.demo_login_system.entities.Company;
import giuseppetavella.demo_login_system.entities.checklists.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TasksRepository extends JpaRepository<Task, UUID> {
    
    /*
     * Find tasks by name
     */
    @Query("""

        SELECT t
        FROM Task t
        WHERE
            t.company = :company
            AND (
                :taskNamePattern IS NULL
                OR LOWER(t.name) LIKE :taskNamePattern
            )

    """)
    Page<Task> findTasksByName(
            Company company,
            String taskNamePattern,
            Pageable pageable
    );
    
}
