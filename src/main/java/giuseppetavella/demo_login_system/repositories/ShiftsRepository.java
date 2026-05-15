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

     * 
     * <pre>
     * shifts belong to that operator 
     *
     * AND (
     *     :endDate > shift.startDate
     *     AND :startDate < shift.endDate
     * )
     * 
     * AND (
     *     :endTime > shift.startTime
     *     AND :startTime < shift.endTime
     * )
     * </pre>
     *
     * 
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
                       :inDate > s.startDate
                       AND :inDate < s.endDate     
                   )
                   AND (
                       :endTime > s.startTime
                       AND :startTime < s.endTime   
                   )
            )      
                
            
    """)
    List<Shift> findShiftsByOperatorInDateBetweenTime(
        User operator,
        LocalDate inDate,
        LocalTime startTime,
        LocalTime endTime
    );
    
    
    /**
     * Find shifts by operator.
     * 
     * @param operator
     * @return
     */
    @Query("""
    
        SELECT s
        FROM Shift s
        WHERE 
            s IN (
                SELECT so.shift
                FROM ShiftOperator so
                WHERE so.operator = :operator   
            )
            
    """)
    List<Shift> findShiftsByOperator(
            @Param("operator") User operator
    );
    
    
    /**
     * Find operators by shift.
     * 
     * @param shift
     * @return
     */
    @Query("""
        
        SELECT 
            DISTINCT u
        FROM 
            User u
        WHERE 
            u IN (
                SELECT so.operator
                FROM ShiftOperator so
                WHERE so.shift = :shift
            ) 
                
    """)
    List<User> findOperatorsByShift(
            @Param("shift") Shift shift
    );
    

    @Query("""
        SELECT s FROM Shift s
        WHERE s.checklist.company = :company
    """)
    List<Shift> findShifts(
            @Param("company") Company company
    );
    

    @Query("""
        SELECT s FROM Shift s
        WHERE s.checklist.company = :company
        AND s.startDate <= :to
        AND (s.endDate IS NULL OR s.endDate >= :from)
    """)
    List<Shift> findShiftsBetween(
            @Param("company") Company company,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );

    @Query("""
        SELECT s FROM Shift s
        WHERE s.checklist.company = :company
        AND (s.endDate IS NULL OR s.endDate >= :from)
    """)
    List<Shift> findShiftsFrom(
            @Param("company") Company company,
            @Param("from") LocalDate from
    );

    @Query("""
        SELECT s FROM Shift s
        WHERE s.checklist.company = :company
        AND s.startDate <= :to
    """)
    List<Shift> findShiftsUntil(
            @Param("company") Company company,
            @Param("to") LocalDate to
    );
    
    
}
