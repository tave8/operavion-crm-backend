package giuseppetavella.zero_chiamate.infrastructure.jobs.jobs.email_operator_tomorrow_shift;

import giuseppetavella.zero_chiamate.domain.entities.users.User;
import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.JobExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Business logic specific queries.
 */
@Repository
public interface EmailOperatorTomorrowShift_Repository extends JpaRepository<JobExecution, Long> {

    /**
     * Get the next operator that:
     * - has not been processed today by this job 
     * 
     * Operator was never processed by this job?
     *      -> get it
     * 
     * Operator was processed by this job but not today?
     *      -> get it
     *      
     * Operator was processed by this job today?
     *      -> skip it
     */
    @Query(nativeQuery = true, value = """
            
        SELECT *
        FROM 
            users u
        WHERE 
            u.role = 'OPERATOR' 
            
            AND NOT EXISTS (
            
                SELECT 1
                FROM 
                    job_executions j
                WHERE
                    j.job_name = :jobName
                    AND 
                        j.last_processed_item_id = u.id
                    AND 
                        DATE(j.started_at) = CURRENT_DATE
                
            )
        
        LIMIT 1

""")
    Optional<User> getNextItem(
            @Param("jobName") String jobName
    );

    
    
    /*
    * 
    * Get one item. 
    * */
    @Query(nativeQuery = true, value = """
    
        SELECT *
        FROM 
            users
        WHERE 
            true
            AND id = :itemId

    """)
    Optional<User> getItemByIdOnIncompleteExecution(
            @Param("itemId") UUID itemId
    );


}
