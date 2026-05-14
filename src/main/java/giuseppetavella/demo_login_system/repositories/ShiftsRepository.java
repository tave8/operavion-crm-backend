package giuseppetavella.demo_login_system.repositories;

import giuseppetavella.demo_login_system.entities.Company;
import giuseppetavella.demo_login_system.entities.shifts.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ShiftsRepository extends JpaRepository<Shift, UUID> {

    /**
     * Find shifts of a company between stard and end date.
     * 
     * <pre>
     *     
     *   SELECTED: YES
     *     
     *         start --------- SHIFT --------- end 
     *           |                              |
     *    
     *                |
     *              start
     *   
     *   SELECTED: YES
     *
     *         start --------- SHIFT --------- end 
     *           |                              |
     *
     *                                    |
     *                                   end      
     * 
     *   SELECTED: NO
     *
     *         start --------- SHIFT --------- end 
     *           |                              |
     *
     *       |
     *     start
     *
     *   SELECTED: NO
     *
     *         start --------- SHIFT --------- end 
     *           |                              |
     *
     *                                               | 
     *                                              end                                            
     *
     *   SELECTED: YES
     *
     *         start --------- SHIFT --------- end 
     *           |                              |
     *
     *                             |                 | 
     *                           start              end                                            
     *
     *   SELECTED: NO
     *
     *         start --------- SHIFT --------- end 
     *           |                              |
     *
     *                                                 |                 | 
     *                                               start              end         
     *   
     * </pre>
     * 
     * 
     * @param company
     * @param startDate the lower bound: "a shift is selected if it starts at this date or started in the past"
     * @param endDate 
     * @return
     */
    @Query(""" 
           
    SELECT s
    FROM Shift s
    WHERE
        s.checklist.company = :company
        AND (
            :startDate IS NULL OR s.startDate <= :endDate
        )
        AND (
            :endDate IS NULL OR s.endDate IS NULL OR s.endDate >= :startDate
        )
        
    """)
    List<Shift> findShiftsBetween(
            Company company,
            LocalDate startDate,
            LocalDate endDate
    );
    
}
