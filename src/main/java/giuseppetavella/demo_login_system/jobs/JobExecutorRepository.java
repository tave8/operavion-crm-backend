package giuseppetavella.demo_login_system.jobs;

import giuseppetavella.demo_login_system.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Business logic specific queries.
 */
@Repository
public interface JobExecutorRepository extends JpaRepository<JobExecution, Long> {
    
    // -- Get the first next item to execute, based on the business logic
    // -- specific filters. I don't care which item it is,
    // -- as long as it's the right time (established by the job-specific filter)
    // -- and has not been already processed by this job.
    //
    // -- All id's of items already processed by this job
    //         -- here go job-specific filters. for now i've written true,
    //         -- just as a reminder that job-specific filters can go at its place
    
    //     -- we only want one item to process
    @Query(nativeQuery = true, value = """
        
        
        WITH Q_processed_items_of_job AS (
            SELECT
                last_processed_item_id
                    AS item_id
            FROM 
                job_executions
            WHERE
                job_name = :jobName
        )
    
        SELECT *
        FROM 
            users
        WHERE 
            true 
            AND user_id NOT IN ( SELECT item_id FROM Q_processed_items_of_job )
        
        LIMIT 1

""")
    Optional<User> getNextEmployeeWhoseContractAboutToExpire(
            @Param("jobName") String jobName
    );
    
}
