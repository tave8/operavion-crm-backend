package giuseppetavella.demo_login_system.jobs;

import giuseppetavella.demo_login_system.entities.User;
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
     * Find last execution of the given job, if it exists.
     */
    @Query("SELECT j FROM JobExecution j WHERE j.jobName = :jobName ORDER BY j.id DESC LIMIT 1")
    Optional<JobExecution> findLastExecutionOfJob(@Param("jobName") String jobName);
    
    
    /**
     * Get next pending job execution for the given job.
     *
     */
    @Query(nativeQuery = true, value = """
    
        SELECT *
        FROM
            job_executions
        WHERE
            job_name = :jobName
            AND state = 'PENDING'
        ORDER BY
            started_at
        LIMIT 1
            
    """)
    Optional<JobExecution> getNextPendingJobExecution(
            @Param("jobName") String jobName
    );
    
}
