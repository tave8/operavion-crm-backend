package giuseppetavella.demo_login_system.infrastructure.jobs.job_library;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Internal logic.
 */
@Repository
public interface JobManagerRepository extends JpaRepository<JobExecution, Long> {
    
    
    /**
     * Get next incomplete job execution for the given job.
     *
     */
    @Query(nativeQuery = true, value = """
    
        SELECT *
        FROM
            job_executions
        WHERE
            job_name = :jobName
            AND state = 'INCOMPLETE'
        ORDER BY
            started_at
        LIMIT 1
            
    """)
    Optional<JobExecution> getNextIncompleteJobExecution(
            @Param("jobName") String jobName
    );
    
}
