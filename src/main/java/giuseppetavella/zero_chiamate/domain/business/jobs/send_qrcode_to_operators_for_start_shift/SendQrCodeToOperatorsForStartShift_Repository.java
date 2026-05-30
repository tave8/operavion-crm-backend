package giuseppetavella.zero_chiamate.domain.business.jobs.send_qrcode_to_operators_for_start_shift;

import giuseppetavella.zero_chiamate.domain.entities.users.User;
import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.JobExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

/**
 * Business logic specific queries.
 */
@Repository
public interface SendQrCodeToOperatorsForStartShift_Repository extends JpaRepository<JobExecution, Long> {

    /**
     * 
     * <h1>Get next item logic</h1>
     * <pre>
     * -----------------------------------------------
     * STATEMENT                    |    IS NEXT ITEM?
     * -----------------------------------------------
     * operator was never processed          YES
     * by this job 
     * ------------------------------------------------
     * operator has been processed           NO
     * by this job today 
     * -----------------------------------------------
     * operator has not been processed
     * by this today                         YES
     * -----------------------------------------------
     * </pre>
     *
     */
    @Query(nativeQuery = true, value = """
            
        SELECT *
        FROM 
            users u
        WHERE 
            u.role = 'OPERATOR' 
          
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
                         j.started_at >= CURRENT_DATE
                
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
