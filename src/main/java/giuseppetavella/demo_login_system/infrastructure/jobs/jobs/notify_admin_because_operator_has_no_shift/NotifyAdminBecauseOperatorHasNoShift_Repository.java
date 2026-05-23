package giuseppetavella.demo_login_system.infrastructure.jobs.jobs.notify_admin_because_operator_has_no_shift;

import giuseppetavella.demo_login_system.domain.entities.users.User;
import giuseppetavella.demo_login_system.infrastructure.jobs.job_library.JobExecution;
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
public interface NotifyAdminBecauseOperatorHasNoShift_Repository extends JpaRepository<JobExecution, Long> {

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
            id = :itemId

    """)
    Optional<User> getItemByIdOnIncompleteExecution(
            @Param("itemId") UUID itemId
    );


}
