package giuseppetavella.demo_login_system.jobs.send_admin_discrepancies;

import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.job_library.JobExecution;
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
public interface SendAdminDiscrepancies_Repository extends JpaRepository<JobExecution, Long> {

    /**
     * Get admins that follow this logic.
     * 
     * <pre>
     * -----------------------------------------------
     * STATEMENT                    |    IS NEXT ITEM?
     * -----------------------------------------------
     * admin was never processed          YES
     * by this job
     * ------------------------------------------------
     * admin has been processed           NO
     * by this job this week 
     * -----------------------------------------------
     * admin has not been processed
     * by this job this week             YES
     * -----------------------------------------------
     * </pre>
     *
     */
    @Query(nativeQuery = true, value = """
            
        SELECT *
        FROM 
            users u
        WHERE 
            u.role = 'ADMIN' 
            -- the admin must have verified their email  
            AND u.verified_email = true  
          
            AND NOT EXISTS (
                -- the logic inside here must be positive
                -- it will then be negated with NOT EXISTS
                SELECT 1
                FROM 
                    job_executions j
                WHERE
                    j.job_name = :jobName
                    AND 
                        j.last_processed_item_id = u.id
                    AND 
                        -- Filters for executions within the current week (last 7 days)
                        j.started_at >= CURRENT_DATE - INTERVAL '7 day'
                
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
