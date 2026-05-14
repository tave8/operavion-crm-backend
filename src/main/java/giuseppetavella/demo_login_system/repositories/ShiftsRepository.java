package giuseppetavella.demo_login_system.repositories;

import giuseppetavella.demo_login_system.entities.Company;
import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.entities.shifts.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ShiftsRepository extends JpaRepository<Shift, UUID> {

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
