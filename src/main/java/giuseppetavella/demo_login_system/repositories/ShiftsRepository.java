package giuseppetavella.demo_login_system.repositories;

import giuseppetavella.demo_login_system.entities.Company;
import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.entities.shifts.Shift;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShiftsRepository extends JpaRepository<Shift, UUID> {

    /**
     * Find shifts of an operator, in a given date,
     * between a time range.
     * 
     * @return shifts that match the above mentioned criteria, where 
     *          a partial overlap exists
     */
    @Query("""
        
        SELECT 
            s
        FROM 
            Shift s
        WHERE 
            s IN (
                SELECT 
                    so.shift
                FROM 
                    ShiftOperator so
                WHERE 
                   so.operator = :operator
                   AND (
                       :inDate >= s.startDate
                       AND :inDate <= s.endDate     
                   )
                   AND (
                       :endTime >= s.startTime
                       AND :startTime <= s.endTime   
                   )
            )      
                
            
    """)
    List<Shift> findShiftsByOperatorInDateBetweenTimes(
        User operator,
        LocalDate inDate,
        LocalTime startTime,
        LocalTime endTime
    );

    
    /**
     * Find shifts of an operator.
     *
     * @return list of shifts
     */
    @Query("""
        
        SELECT 
            s
        FROM 
            Shift s
        WHERE 
            s IN (
                SELECT 
                    so.shift
                FROM 
                    ShiftOperator so
                WHERE 
                   so.operator = :operator
            )      
            
    """)
    List<Shift> findShiftsByOperator(
            User operator
    );
    

    /**
     * Find shifts of an operator between a date range.
     *
     * @return shifts that match the above mentioned criteria, where 
     *          a partial overlap exists
     */
    @Query("""
        
        SELECT 
            s
        FROM 
            Shift s
        WHERE 
            s IN (
                SELECT 
                    so.shift
                FROM 
                    ShiftOperator so
                WHERE 
                   so.operator = :operator
                   AND (
                       :endDate >= s.startDate
                       AND :startDate <= s.endDate     
                   )
            )      
                
            
    """)
    List<Shift> findShiftsByOperatorBetweenDates(
            User operator,
            LocalDate startDate,
            LocalDate endDate
    );


    /**
     * Find shifts of a company between a date range.
     *
     * @return shifts that match the above mentioned criteria, where 
     *          a partial overlap exists
     */
    @Query("""
        
        SELECT 
            s 
        FROM 
            Shift s
        WHERE 
            s.checklist.company = :company
            AND (
               :endDate >= s.startDate
               AND :startDate <= s.endDate     
           )  
            
    """)
    List<Shift> findShiftsByCompanyBetweenDates(
            Company company,
            LocalDate startDate,
            LocalDate endDate
    );
    
    
    
    /**
     * Find operators that have been assigned to a shift.
     * 
     * @return list of users
     */
    @Query("""
        
        SELECT 
            DISTINCT u
        FROM 
            User u
        WHERE 
            u IN (
                SELECT 
                    so.operator
                FROM 
                    ShiftOperator so
                WHERE 
                    so.shift = :shift
            ) 
                
    """)
    List<User> findOperatorsByShift(
            @Param("shift") Shift shift
    );


    /**
     * Find operators (of a company) with shifts between a date range.
     *
     * 
     * @return list of users
     */
    @Query("""

        SELECT 
            DISTINCT u
        FROM 
            User u
        WHERE 
            u.company = :company
            AND u IN (
                SELECT 
                    so.operator
                FROM 
                    ShiftOperator so
                WHERE 
                    so.shift IN (
                        SELECT 
                            s
                        FROM 
                            Shift s
                        WHERE 
                            :endDate >= s.startDate
                            AND :startDate <= s.endDate    
                    )
            ) 

    """)
    List<User> findOperatorsWithShiftsBetween(
            Company company,
            LocalDate startDate,
            LocalDate endDate
    );

    
}
