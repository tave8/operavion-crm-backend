package giuseppetavella.demo_login_system.jobs;

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
    
}
